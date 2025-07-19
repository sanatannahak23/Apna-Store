package com.apnaStore.inventory_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse {

    private Boolean error;

    private Object data;

    private String message;

}
