package com.apnaStore.user_service.service;

import com.apnaStore.user_service.constants.ExceptionMessages;
import com.apnaStore.user_service.dto.request.AddressRequest;
import com.apnaStore.user_service.dto.response.AddressResponse;
import com.apnaStore.user_service.entities.Address;
import com.apnaStore.user_service.entities.User;
import com.apnaStore.user_service.exception.DataNotFound;
import com.apnaStore.user_service.helper.RequestToEntity;
import com.apnaStore.user_service.repository.AddressRepository;
import com.apnaStore.user_service.repository.UserRepository;
import com.apnaStore.user_service.service.service.AddressService;
import com.apnaStore.user_service.utils.EntityToResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    @Override
    public AddressResponse addAddress(Long userId, AddressRequest addressRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.INVALID_USER_ID));
        Address address = RequestToEntity.addressRequestToAddress(addressRequest);
        address.setUser(user);
        return EntityToResponse.addressToAddressReponse(addressRepository.save(address));
    }

    @Override
    public List<AddressResponse> getAllAddresses(Long userId, String order, Integer size, Integer page, String sortBy) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.INVALID_USER_ID));
        Sort sort = order.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Address> addresses = addressRepository.findByUser(pageable, user);
        return addresses
                .stream()
                .map(EntityToResponse::addressToAddressReponse)
                .toList();
    }

    @Override
    public AddressResponse getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.DATA_NOT_FOUND));
        return EntityToResponse.addressToAddressReponse(address);
    }

    @Override
    public AddressResponse updateAddress(Long addressId, AddressRequest addressRequest) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.DATA_NOT_FOUND));
        boolean check = false;
        if (addressRequest.getAddress() != null && !address.getAddress().equalsIgnoreCase(addressRequest.getAddress())) {
            address.setAddress(addressRequest.getAddress());
            check = true;
        }

        if (addressRequest.getCity() != null && !address.getCity().equalsIgnoreCase(addressRequest.getCity())) {
            address.setCity(addressRequest.getCity());
            check = true;
        }

        if (addressRequest.getState() != null && !address.getState().equalsIgnoreCase(addressRequest.getState())) {
            address.setState(addressRequest.getState());
            check = true;
        }

        if (addressRequest.getCountry() != null && !address.getCountry().equalsIgnoreCase(addressRequest.getCountry())) {
            address.setCountry(addressRequest.getCountry());
            check = true;
        }

        if (addressRequest.getName() != null && !address.getName().equalsIgnoreCase(addressRequest.getName())) {
            address.setName(addressRequest.getName());
            check = true;
        }

        if (addressRequest.getPhoneNum() != null && !address.getPhoneNum().equalsIgnoreCase(addressRequest.getPhoneNum())) {
            address.setPhoneNum(addressRequest.getPhoneNum());
            check = true;
        }

        if (addressRequest.getPinCode() != null && !address.getPinCode().equals(addressRequest.getPinCode())) {
            address.setPinCode(addressRequest.getPinCode());
            check = true;
        }
        if (check)
            addressRepository.save(address);
        return EntityToResponse.addressToAddressReponse(address);
    }

    @Override
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.DATA_NOT_FOUND));
        addressRepository.delete(address);
    }

    @Override
    public void setDefaultAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.DATA_NOT_FOUND));
        boolean isDefault = address.getIsDefault();
        if (isDefault) return;
        User user = address.getUser();

        for (Address adds : user.getAddresses()) {
            isDefault = adds.getIsDefault();
            if (isDefault) adds.setIsDefault(Boolean.FALSE);
            if (adds.getId().equals(addressId)) adds.setIsDefault(Boolean.TRUE);
        }

        userRepository.save(user);
    }
}
