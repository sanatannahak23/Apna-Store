package com.apnaStore.user_service.repository;

import com.apnaStore.user_service.entities.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Optional<Credential> findByUserName(String userName);
}
