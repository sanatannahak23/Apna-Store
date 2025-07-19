package com.apnaStore.user_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private String name;

    private String email;

    private Long phoneNum;

    private String gender;

    @JsonProperty("credential")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private CredentialRequest credentialRequest;
}
