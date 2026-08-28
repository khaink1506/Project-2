package com.devon.building.service;

import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.LoginRequest;
import com.devon.building.model.request.RegisterRequest;
import com.devon.building.model.response.LoginResponse;


public interface AuthenticationService {

    ResponseDTO register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
