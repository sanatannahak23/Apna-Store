package com.apnaStore.user_service.service;

import com.apnaStore.user_service.constants.ExceptionMessages;
import com.apnaStore.user_service.dto.request.CredentialRequest;
import com.apnaStore.user_service.dto.request.UserRequest;
import com.apnaStore.user_service.dto.response.AddressResponse;
import com.apnaStore.user_service.dto.response.AuthResponse;
import com.apnaStore.user_service.dto.response.UserResponse;
import com.apnaStore.user_service.entities.Address;
import com.apnaStore.user_service.entities.Credential;
import com.apnaStore.user_service.entities.User;
import com.apnaStore.user_service.entities.enums.Role;
import com.apnaStore.user_service.entities.enums.Status;
import com.apnaStore.user_service.exception.DataFoundException;
import com.apnaStore.user_service.exception.DataNotFound;
import com.apnaStore.user_service.exception.InvalidUserNameOrPassword;
import com.apnaStore.user_service.helper.RequestToEntity;
import com.apnaStore.user_service.kafka.KafkaProducerService;
import com.apnaStore.user_service.repository.CredentialRepository;
import com.apnaStore.user_service.repository.RefreshTokenRepository;
import com.apnaStore.user_service.repository.UserRepository;
import com.apnaStore.user_service.security.JwtTokenService;
import com.apnaStore.user_service.service.service.RefreshTokenService;
import com.apnaStore.user_service.service.service.UserService;
import com.apnaStore.user_service.utils.EntityToResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CredentialRepository credentialRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    private final RefreshTokenService refreshTokenService;

    private final RefreshTokenRepository refreshTokenRepository;

    private final KafkaProducerService kafkaProducerService;

    @Override
    public UserResponse register(UserRequest userRequest) {
        userRepository.findByEmail(userRequest.getEmail())
                .ifPresent(e -> {
                    throw new DataFoundException(ExceptionMessages.EMAIL_ALREADY_EXIST);
                });

        User user = RequestToEntity.userRequestToUser(userRequest);
        user.getCredential().setPassword(passwordEncoder.encode(user.getCredential().getPassword()));
        user = userRepository.save(user);
        return EntityToResponse.userToUserResponse(user);
    }

    @Override
    public AuthResponse login(CredentialRequest credentialRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentialRequest.getUserName(),
                credentialRequest.getPassword(),
                List.of(new SimpleGrantedAuthority(credentialRequest.getRole().getRole()))));
        if (!authenticate.isAuthenticated())
            throw new InvalidUserNameOrPassword(ExceptionMessages.INVALID_CREDENTIAL);
        Credential credential = credentialRepository.findByUserName(credentialRequest.getUserName())
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.DATA_NOT_FOUND));

        refreshTokenRepository.findByCredential(credential).ifPresent(refreshTokenService::delete);
        return AuthResponse
                .builder()
                .accessToken(jwtTokenService.getToken(credentialRequest.getUserName(),
                        credentialRequest.getRole().getRole(),
                        credential.getUser().getUserRef()))
                .refreshToken(refreshTokenService.createToken(credential.getUserName()).getToken())
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.INVALID_USER_ID));
        UserResponse userResponse = EntityToResponse.userToUserResponse(user);

        if (null != user.getAddresses() && !user.getAddresses().isEmpty()) {
            ArrayList<AddressResponse> addressResponses = new ArrayList<>();
            for (Address address : user.getAddresses()) {
                AddressResponse addressResponse = EntityToResponse.addressToAddressReponse(address);
                addressResponses.add(addressResponse);
            }
            userResponse.setAddressResponse(addressResponses);
        }

        return userResponse;
    }

    @Override
    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.INVALID_USER_ID));
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setGender(userRequest.getGender());
        user.setPhoneNum(userRequest.getPhoneNum());
        user.getCredential().setUserName(userRequest.getName());

        user = userRepository.save(user);
        return EntityToResponse.userToUserResponse(user);
    }

    @Override
    public List<UserResponse> findAll(String order, Integer size, Integer page, String sortBy) {
        Sort.Direction direction = order.equals("ascending") ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageAble = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return userRepository.findAll(pageAble)
                .stream()
                .map(EntityToResponse::userToUserResponse)
                .toList();
    }

    @Override
    public UserResponse deleteUser(Long id) {
        log.info("Delete method get invoked :: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.USER_NOT_FOUND));

        log.info("user details :: {}", user.getId());
        log.info("credential details :: {}", user.getCredential().getCredentialId());
        credentialRepository.delete(user.getCredential());
        userRepository.delete(user);
        kafkaProducerService.messageForDeleteCart(id);
        return EntityToResponse.userToUserResponse(user);
    }

    @Override
    public UserResponse findByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.USER_NOT_FOUND));
        UserResponse userResponse = EntityToResponse.userToUserResponse(user);

        if (null != user.getAddresses() && !user.getAddresses().isEmpty()) {
            ArrayList<AddressResponse> addressResponses = new ArrayList<>();
            for (Address address : user.getAddresses()) {
                AddressResponse addressResponse = EntityToResponse.addressToAddressReponse(address);
                addressResponses.add(addressResponse);
            }
            userResponse.setAddressResponse(addressResponses);
        }

        return userResponse;
    }

    @Override
    public List<User> searchUser(String search, String order, Integer size, Integer page, String sortBy) {
        // need to work
        return List.of();
    }

    @Override
    public UserResponse updateStatus(Long userId, Integer status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.USER_NOT_FOUND));

        if (!user.getUserStatus().getValue().equals(status)) {
            user.setUserStatus(Status.getByValue(status));
        }

        return EntityToResponse.userToUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateRole(Long userId, Integer role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.USER_NOT_FOUND));
        user.getCredential().setRole(Role.getByValue(role));
        return EntityToResponse.userToUserResponse(userRepository.save(user));
    }
}
