package com.apnaStore.user_service.service;

import com.apnaStore.user_service.constants.ExceptionMessages;
import com.apnaStore.user_service.dto.request.RefreshTokenRequest;
import com.apnaStore.user_service.entities.RefreshToken;
import com.apnaStore.user_service.exception.DataNotFound;
import com.apnaStore.user_service.exception.InvalidTokenException;
import com.apnaStore.user_service.exception.RefreshTokenExpireException;
import com.apnaStore.user_service.repository.CredentialRepository;
import com.apnaStore.user_service.repository.RefreshTokenRepository;
import com.apnaStore.user_service.service.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final CredentialRepository credentialRepository;

    @Override
    public RefreshToken createToken(String userName) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID() + "" + System.currentTimeMillis())
                .credential(credentialRepository.findByUserName(userName)
                        .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.USER_NOT_FOUND)))
                .issuedAt(Instant.now())
                .expiryData(Instant.now().plusSeconds(3600))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Boolean validateToken(RefreshToken refreshToken) {
        if (Instant.now().compareTo(refreshToken.getExpiryData()) >= 0) {
            delete(refreshToken);
            throw new RefreshTokenExpireException(ExceptionMessages.REFRESH_TOKEN_EXPIRED);
        }
        return Boolean.TRUE;
    }

    @Override
    public RefreshToken findByToken(RefreshTokenRequest tokenRequest) {
        return refreshTokenRepository.findByToken(tokenRequest.getToken())
                .orElseThrow(() -> new InvalidTokenException(ExceptionMessages.INVALID_TOKEN));
    }

    @Override
    public void delete(RefreshToken refreshToken) {
        RefreshToken refreshToken1 = refreshTokenRepository.findByToken(refreshToken.getToken())
                .orElseThrow(() -> new DataNotFound(ExceptionMessages.DATA_NOT_FOUND));
        refreshTokenRepository.delete(refreshToken1);
    }
}
