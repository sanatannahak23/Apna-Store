package com.apnaStore.user_service.repository;

import com.apnaStore.user_service.entities.Credential;
import com.apnaStore.user_service.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByCredential(Credential credential);
}
