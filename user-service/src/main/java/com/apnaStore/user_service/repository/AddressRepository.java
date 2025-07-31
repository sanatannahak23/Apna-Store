package com.apnaStore.user_service.repository;

import com.apnaStore.user_service.entities.Address;
import com.apnaStore.user_service.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(Pageable pageable, User user);
}
