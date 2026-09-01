package com.backend.auth.dto;

public record TokenRefreshResponse(
	String accessToken,
	String refreshToken,
	String tokenType
){}
