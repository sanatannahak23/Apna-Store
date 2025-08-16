package com.apnaStore.user_service.service.service;

import com.apnaStore.user_service.dto.request.CredentialRequest;
import com.apnaStore.user_service.dto.request.UserRequest;
import com.apnaStore.user_service.dto.response.AuthResponse;
import com.apnaStore.user_service.dto.response.UserResponse;
import com.apnaStore.user_service.entities.User;

import java.util.List;

public interface UserService {

    UserResponse register(UserRequest userRequest);

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long userId, UserRequest userRequest);

    List<UserResponse> findAll(String order, Integer size, Integer page, String sortBy);

    UserResponse deleteUser(Long id);

    UserResponse findByUserEmail(String email);

    List<User> searchUser(String search, String order, Integer size, Integer page, String sortBy);

    UserResponse updateStatus(Long userId, Integer status);

    UserResponse updateRole(Long userId, Integer role);

    AuthResponse login(CredentialRequest credentialRequest);
}
