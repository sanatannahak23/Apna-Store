package com.apnaStore.user_service.dto.response;

import lombok.Data;

@Data
public class AddressResponse {

    private Long id;

    private String address;

    private String city;

    private String state;

    private String country;

    private Integer pinCode;

    private String name;

    private String phoneNum;

    private Boolean isDefault;
}
