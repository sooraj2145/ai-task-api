package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.LoginRequest;
import com.sooraj.aitaskapi.dto.LoginResponse;
import com.sooraj.aitaskapi.dto.RegisterRequest;
import com.sooraj.aitaskapi.entity.User;
import com.sooraj.aitaskapi.exception.EmailAlreadyExistsException;
import com.sooraj.aitaskapi.exception.InvalidCredentialsException;
import com.sooraj.aitaskapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        String hashPassword = passwordEncoder.encode(request.getPassword());

        user.setPassword(hashPassword);

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!passwordMatches) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                "Bearer"
        );
    }


}
