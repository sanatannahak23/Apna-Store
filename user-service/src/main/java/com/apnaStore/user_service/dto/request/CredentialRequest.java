package com.apnaStore.user_service.dto.request;

import com.apnaStore.user_service.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CredentialRequest {

    private String userName;

    private Role role = Role.CUSTOMER;

    private String password;

    private Boolean isActive;

    private Boolean isAccountNonExpired;

    private Boolean isAccountNOtLocked;

    private Boolean isCredentialsNonExpired;

}
