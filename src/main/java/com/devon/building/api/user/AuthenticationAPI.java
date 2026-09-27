package com.devon.building.api.user;

import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.LoginRequest;
import com.devon.building.model.request.RegisterRequest;
import com.devon.building.model.response.LoginResponse;
import com.devon.building.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthenticationAPI {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult){
        return ResponseEntity.ok().body(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        return ResponseEntity.ok().body(authenticationService.login(request));
    }
}
