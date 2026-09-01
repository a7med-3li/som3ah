package com.backend.user.dto;

import com.backend.user.enums.UserRole;

public record UserInfo(
        String displayName,
        String email,
        UserRole role
) {}
