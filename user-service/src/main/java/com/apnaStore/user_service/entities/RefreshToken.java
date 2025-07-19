package com.apnaStore.user_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RefreshToken extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    @Column(nullable = false, name = "issue_at")
    private Instant issuedAt;

    @Column(nullable = false, name = "exp_date")
    private Instant expiryData;

    @OneToOne
    private Credential credential;
}
