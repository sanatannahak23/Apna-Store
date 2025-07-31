package com.apnaStore.user_service.service.service;

import com.apnaStore.user_service.dto.request.RefreshTokenRequest;
import com.apnaStore.user_service.entities.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createToken(String userName);

    Boolean validateToken(RefreshToken refreshToken);

    RefreshToken findByToken(RefreshTokenRequest tokenRequest);

    void delete(RefreshToken refreshToken);
}
