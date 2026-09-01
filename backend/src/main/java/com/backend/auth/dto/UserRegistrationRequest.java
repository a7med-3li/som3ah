package com.backend.auth.dto;

import com.backend.user.enums.UserRole;

public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String password,
        UserRole role
) {}
