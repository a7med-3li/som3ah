package com.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PassengerRegisterRequest(
		@NotBlank @Size(min = 2, max = 50) String firstName,
		@NotBlank @Size(min = 2, max = 50) String lastName,
		@NotBlank @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") String email,
		@NotBlank @Size(min = 6, max = 128) String password
) {}
