package com.apnaStore.user_service.helper;

import com.apnaStore.user_service.dto.request.AddressRequest;
import com.apnaStore.user_service.dto.request.UserRequest;
import com.apnaStore.user_service.entities.Address;
import com.apnaStore.user_service.entities.Credential;
import com.apnaStore.user_service.entities.User;
import com.apnaStore.user_service.entities.enums.Status;
import org.springframework.beans.BeanUtils;

public interface RequestToEntity {

    static User userRequestToUser(UserRequest userRequest) {
        User user = new User();
        BeanUtils.copyProperties(userRequest, user);

        Credential credential = new Credential();
        BeanUtils.copyProperties(userRequest.getCredentialRequest(), credential);
        credential.setUser(user);
        user.setCredential(credential);
        user.setUserStatus(Status.ACTIVE);
        return user;
    }

    static Address addressRequestToAddress(AddressRequest addressRequest) {
        Address address = new Address();
        address.setAddress(addressRequest.getAddress());
        address.setCity(addressRequest.getCity());
        address.setName(addressRequest.getName());
        address.setCountry(addressRequest.getCountry());
        address.setState(addressRequest.getState());
        address.setPhoneNum(addressRequest.getPhoneNum());
        address.setPinCode(addressRequest.getPinCode());
        return address;
    }

}
