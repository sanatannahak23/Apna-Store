package com.apna_store.cart_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProductResponse {

    private Long id;

    private String prodRef;

    private String name;

    private String description;

    private BigDecimal price;

}
