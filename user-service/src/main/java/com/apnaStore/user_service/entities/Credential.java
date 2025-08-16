package com.apnaStore.user_service.entities;

import com.apnaStore.user_service.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_credential",
        indexes = @Index(name = "idx_user_name", columnList = "userName")
)
public class Credential extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long credentialId;

    @Column(unique = true)
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    private String password;

    private Boolean isActive;

    private Boolean isAccountNonExpired;

    private Boolean isAccountNOtLocked;

    private Boolean isCredentialsNonExpired;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
