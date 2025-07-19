package com.apnaStore.user_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {

    private String name;

    private String address;

    private String city;

    private String state;

    private String country;

    private Integer pinCode;

    private String phoneNum;

}
