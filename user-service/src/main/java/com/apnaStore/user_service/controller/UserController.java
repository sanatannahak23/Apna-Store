package com.apnaStore.user_service.controller;

import com.apnaStore.user_service.constants.HttpMessages;
import com.apnaStore.user_service.dto.request.CredentialRequest;
import com.apnaStore.user_service.dto.request.RefreshTokenRequest;
import com.apnaStore.user_service.dto.request.UserRequest;
import com.apnaStore.user_service.dto.response.ApiResponse;
import com.apnaStore.user_service.dto.response.AuthResponse;
import com.apnaStore.user_service.entities.Credential;
import com.apnaStore.user_service.entities.RefreshToken;
import com.apnaStore.user_service.security.JwtTokenService;
import com.apnaStore.user_service.service.service.RefreshTokenService;
import com.apnaStore.user_service.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final RefreshTokenService refreshTokenService;

    private final JwtTokenService jwtTokenService;

    //    /users/register  (Register user)
    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRequest userRequest) {
        log.info("User Details ::{} ", userRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(
                        Boolean.FALSE,
                        userService.register(userRequest),
                        HttpMessages.USER_SAVED_SUCCESSFULLY
                ));
    }

    // /users/login
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse> login(@RequestBody CredentialRequest credentialRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        userService.login(credentialRequest),
                        HttpMessages.USER_LOGIN_SUCCESSFULLY));
    }

    // /users/refresh-token
    @GetMapping("/auth/refresh-token")
    public ResponseEntity<ApiResponse> getRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken token = refreshTokenService.findByToken(refreshTokenRequest);
        refreshTokenService.validateToken(token);
        Credential credential = token.getCredential();

        String jwtToken = jwtTokenService.getToken(credential.getUserName(),
                credential.getRole().getRole(),
                credential.getUser().getUserRef());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        AuthResponse.builder()
                                .accessToken(jwtToken)
                                .refreshToken(token.getToken())
                                .build(),
                        HttpMessages.TOKEN_REFRESHED));
    }

    //    /users/{id}	Get user by
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(
                        Boolean.FALSE,
                        userService.getUserById(userId),
                        HttpMessages.DATA_FETCHED_SUCCESSFULLY
                ));
    }

    //    /users/email/{email}	Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse> getUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(new ApiResponse(
                        Boolean.FALSE,
                        userService.findByUserEmail(email),
                        HttpMessages.DATA_FETCHED_SUCCESSFULLY
                ));
    }

    //    /users/{id}	Update user info (name/role/status)
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("userId") Long userId, @RequestBody UserRequest userRequest) {
        return ResponseEntity
                .ok()
                .body(new ApiResponse(
                        Boolean.FALSE,
                        userService.updateUser(userId, userRequest),
                        HttpMessages.USER_UPDATED_SUCCESSFULLY
                ));
    }

    //    /users	Get all users (for admin dashboard)
    @GetMapping
    public ResponseEntity<ApiResponse> getAll(@RequestParam(defaultValue = "ascending") String order,
                                              @RequestParam(defaultValue = "10") Integer size,
                                              @RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "name") String sortBy) {
        return ResponseEntity
                .ok()
                .body(new ApiResponse(
                        Boolean.FALSE,
                        userService.findAll(order, size, page, sortBy),
                        HttpMessages.DATA_FETCHED_SUCCESSFULLY
                ));
    }

    //    /users/{id}/status	Update user status
    @PutMapping("/{userId}/status/{status}")
    public ResponseEntity<ApiResponse> changeUserStatus(@PathVariable("userId") Long userId,
                                                        @PathVariable("status") Integer status) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        userService.updateStatus(userId, status),
                        HttpMessages.USER_STATUS_UPDATED_SUCCESSFULLY));
    }

    //  /users/{id}/role/{role} update role of the user
    @PutMapping("/{userId}/role/{role}")
    public ResponseEntity<ApiResponse> changeRole(@PathVariable("userId") Long userId, @PathVariable("role") Integer role) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        userService.updateRole(userId, role),
                        HttpMessages.USER_ROLE_UPDATED));
    }

    //     delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") Long id) {
        // When user deleted cart is also need to be deleted (kafka)
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        userService.deleteUser(id),
                        HttpMessages.USER_REMOVED_SUCCESSFULLY));
    }

    //    search user
    public void searchUser() {
    }

}
