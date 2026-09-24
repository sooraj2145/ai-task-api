package com.sooraj.aitaskapi.controller;


import com.sooraj.aitaskapi.dto.LoginRequest;
import com.sooraj.aitaskapi.dto.LoginResponse;
import com.sooraj.aitaskapi.dto.RegisterRequest;
import com.sooraj.aitaskapi.dto.UserResponse;
import com.sooraj.aitaskapi.entity.User;
import com.sooraj.aitaskapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {

        User user = authService.register(request);

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }
}
