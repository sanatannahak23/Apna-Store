package com.apnaStore.user_service.security;

import com.apnaStore.user_service.constants.ExceptionMessages;
import com.apnaStore.user_service.entities.Credential;
import com.apnaStore.user_service.entities.User;
import com.apnaStore.user_service.entities.enums.Status;
import com.apnaStore.user_service.repository.CredentialRepository;
import com.apnaStore.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final CredentialRepository credentialRepository;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credential credential = credentialRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.DATA_NOT_FOUND));

        User user = credential.getUser();
        if (user.getUserStatus().equals(Status.INACTIVE)) {
            throw new UsernameNotFoundException(ExceptionMessages.YOU_HAVE_ALREADY_DELETED_THIS_ACCOUNT);
        }
        return new CustomUserDetails(credential.getUserName(),
                credential.getPassword(),
                credential.getRole().getRole(),
                credential.getIsAccountNonExpired(),
                credential.getIsAccountNOtLocked(),
                credential.getIsCredentialsNonExpired(),
                credential.getIsActive());
    }
}
