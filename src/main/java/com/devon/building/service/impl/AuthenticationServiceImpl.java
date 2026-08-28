package com.devon.building.service.impl;

import com.devon.building.converter.UserConverter;
import com.devon.building.entity.Role;
import com.devon.building.entity.User;
import com.devon.building.exception.InvalidRequestException;
import com.devon.building.exception.ResourceAlreadyExists;
import com.devon.building.exception.ResourceNotFoundException;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.LoginRequest;
import com.devon.building.model.request.RegisterRequest;
import com.devon.building.model.response.LoginResponse;
import com.devon.building.repository.RoleRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.AuthenticationService;
import com.devon.building.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public ResponseDTO register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidRequestException("Password and confirm password do not match");
        }
        if(userRepository.existsByUserName(request.getUserName())){
            throw new ResourceAlreadyExists("Username already exists");
        }
        Role role = roleRepository.findByCode("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role is required"));

        User newUser = userConverter.toUser(request);
        newUser.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setUserRole(role);
        newUser.setActive(true);
        User savedUser = userRepository.save(newUser);
        return ResponseDTO.builder()
                .message("Register Successfully")
                .data(userConverter.toResponse(savedUser))
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        String accessToken = jwtService.generateAccessToken(authentication);
        return LoginResponse.builder()
                .message("Login SuccessFully")
                .token(accessToken)
                .build();
    }
}
