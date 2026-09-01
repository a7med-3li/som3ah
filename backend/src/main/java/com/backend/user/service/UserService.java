package com.backend.user.service;


import com.backend.auth.dto.PassengerRegisterRequest;
import com.backend.common.exception.UserNotFoundException;
import com.backend.user.dto.UserResponse;
import com.backend.user.entity.User;
import com.backend.user.enums.UserRole;
import com.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.time.Instant;
import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    
    public void registerUser(PassengerRegisterRequest req) {
        userRepository.findByEmail(req.email())
	            .orElseGet(() -> createBaseUser(req.email(), req.firstName(),req.lastName(), UserRole.USER, req.password()));
    }
    
    private User createBaseUser(String email, String firstName, String lastName, UserRole role, String password) {
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        user.setDeleted(false);
        user.setCreatedAt(Instant.now());
        user.setPasswordHash(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (user.isDeleted()) { return;}
        
        userRepository.delete(user);
    }

    // note: needs logic review
    public UserResponse restoreUser(UUID id) {
        return null;
    }

}
