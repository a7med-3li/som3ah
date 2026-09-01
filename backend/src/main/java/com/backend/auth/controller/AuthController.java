package com.backend.auth.controller;

import com.backend.auth.dto.AuthResponse;
import com.backend.auth.dto.LoginRequest;
import com.backend.auth.dto.PassengerRegisterRequest;
import com.backend.auth.dto.TokenRefreshRequest;
import com.backend.auth.dto.TokenRefreshResponse;
import com.backend.auth.entity.RefreshToken;
import com.backend.auth.security.SecurityUser;
import com.backend.auth.service.AuthService;
import com.backend.auth.service.RefreshTokenService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register/passenger")
    public ResponseEntity<AuthResponse> registerPassenger(@Valid @RequestBody PassengerRegisterRequest request) {
        authService.registerUser(request);
        return login(new LoginRequest(request.email(), request.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        SecurityUser securityUser = authService.authenticate(
                loginRequest.email(),
                loginRequest.password()
        );
        String tokenValue = authService.generateToken(securityUser);
        refreshTokenService.deleteRefreshToken(securityUser.user().getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(securityUser.user().getId());
        
        return ResponseEntity.ok(new AuthResponse(tokenValue, refreshToken.getToken()));
    }
    
    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal String userId) {
        // Invalidate the refresh token on logout
        refreshTokenService.deleteRefreshToken(UUID.fromString(userId));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();
        
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    SecurityUser securityUser = new SecurityUser(user);
                    
                    String newAccessToken = authService.generateToken(securityUser);
                    
                    return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, requestRefreshToken, "Bearer"));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

}
