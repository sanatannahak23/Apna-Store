package com.apnaStore.user_service.entities;

import com.apnaStore.user_service.entities.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_details",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone", columnList = "phoneNum")
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_ref")
    private String userRef;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(name = "phone_num", unique = true)
    private Long phoneNum;

    private String gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Address> addresses;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "credential_id")
    private Credential credential;

    @Enumerated(EnumType.STRING)
    private Status userStatus;

    @PrePersist
    private void setUserRef() {
        this.userRef = UUID.randomUUID().toString() + System.currentTimeMillis();
    }
}
