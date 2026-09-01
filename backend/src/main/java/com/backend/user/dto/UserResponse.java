package com.backend.user.dto;

import java.time.Instant;
import java.util.UUID;
import com.backend.user.enums.UserRole;

public record UserResponse(
        UUID userId,
        String displayName,
        String phoneNumber,
        UserRole role,
        Instant createdAt,
        boolean deleted
) {}
