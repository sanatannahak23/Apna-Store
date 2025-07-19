package com.apnaStore.user_service.service.service;

import com.apnaStore.user_service.dto.request.AddressRequest;
import com.apnaStore.user_service.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressResponse addAddress(Long userId, AddressRequest addressRequest);

    AddressResponse updateAddress(Long addressId, AddressRequest addressRequest);

    void deleteAddress(Long addressId);

    void setDefaultAddress(Long addressId);

    List<AddressResponse> getAllAddresses(Long userId, String order, Integer size, Integer page, String sortBy);
}
