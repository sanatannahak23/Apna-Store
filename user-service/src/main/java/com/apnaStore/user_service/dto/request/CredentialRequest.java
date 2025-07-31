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

    private Boolean isActive = Boolean.TRUE;

    private Boolean isAccountNonExpired = Boolean.TRUE;

    private Boolean isAccountNOtLocked = Boolean.TRUE;

    private Boolean isCredentialsNonExpired = Boolean.TRUE;

}
