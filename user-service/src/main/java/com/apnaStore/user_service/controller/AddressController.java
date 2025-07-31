package com.apnaStore.user_service.controller;

import com.apnaStore.user_service.constants.HttpMessages;
import com.apnaStore.user_service.dto.request.AddressRequest;
import com.apnaStore.user_service.dto.response.ApiResponse;
import com.apnaStore.user_service.service.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // 1. Add address for a user
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> addAddress(@PathVariable Long userId,
                                                  @RequestBody AddressRequest addressRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        addressService.addAddress(userId, addressRequest),
                        HttpMessages.ADDRESS_ADDED_SUCCESSFULLY));
    }

    // 2. Get all addresses of a user
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getAddresses(@PathVariable Long userId,
                                                    @RequestParam(defaultValue = "ascending") String order,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    @RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "createdDate") String sortBy) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        addressService.getAllAddresses(userId,order,size,page,sortBy),
                        HttpMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    // 3. Update address by ID
    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable Long addressId,
                                                     @RequestBody AddressRequest addressRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        addressService.updateAddress(addressId, addressRequest),
                        HttpMessages.DATA_UPDATED_SUCCESSFULLY));
    }

    // 4. Delete address
    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        null,
                        HttpMessages.DATA_REMOVED_SUCCESSFULLY));
    }

    // 5. Set a default address
    @PutMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse> setDefaultAddress(@PathVariable Long addressId) {
        addressService.setDefaultAddress(addressId);
        return ResponseEntity.ok().build();
    }
}
