package com.apnaStore.user_service.dto.response;

import com.apnaStore.user_service.dto.request.UserRequest;
import com.apnaStore.user_service.entities.Auditable;
import com.apnaStore.user_service.entities.enums.Role;
import lombok.*;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CredentialResponse extends Auditable {

    private Long credentialId;

    private String userName;

    private Role role;

    private String password;

    private Boolean isActive;

    private Boolean isAccountNonExpired;

    private Boolean isAccountNOtLocked;

    private Boolean isCredentialsNonExpired;

    private UserRequest userRequest;
}
