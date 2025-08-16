package com.apnaStore.user_service.repository;

import com.apnaStore.user_service.entities.Credential;
import com.apnaStore.user_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCredential(Credential credential);
}
