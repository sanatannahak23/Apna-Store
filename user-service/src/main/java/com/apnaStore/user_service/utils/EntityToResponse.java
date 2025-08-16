package com.apnaStore.user_service.utils;

import com.apnaStore.user_service.dto.response.AddressResponse;
import com.apnaStore.user_service.dto.response.UserResponse;
import com.apnaStore.user_service.entities.Address;
import com.apnaStore.user_service.entities.User;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;

public interface EntityToResponse {

    static UserResponse userToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        userResponse.setRole(user.getCredential().getRole().toString());
        ArrayList<AddressResponse> addressResponses = new ArrayList<>();
        if (null != user.getAddresses() && !user.getAddresses().isEmpty()) {
            for (Address address : user.getAddresses()) {
                AddressResponse addressResponse = addressToAddressReponse(address);
                addressResponses.add(addressResponse);
            }
            userResponse.setAddressResponse(addressResponses);
        }
        userResponse.setStatus(user.getUserStatus().toString());
        return userResponse;
    }

    static AddressResponse addressToAddressReponse(Address address) {
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setId(address.getId());
        addressResponse.setAddress(address.getAddress());
        addressResponse.setCity(address.getCity());
        addressResponse.setState(address.getState());
        addressResponse.setCountry(address.getCountry());
        addressResponse.setPinCode(address.getPinCode());
        addressResponse.setName(address.getName());
        addressResponse.setPhoneNum(address.getPhoneNum());
        addressResponse.setIsDefault(address.getIsDefault());
        return addressResponse;
    }
}
