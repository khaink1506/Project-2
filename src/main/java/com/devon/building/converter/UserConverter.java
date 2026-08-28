package com.devon.building.converter;


import com.devon.building.entity.User;
import com.devon.building.model.request.RegisterRequest;
import com.devon.building.model.response.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final ModelMapper modelMapper;

    public User toUser(RegisterRequest request) {
        return modelMapper.map(request, User.class);
    }

    public RegisterResponse toResponse(User user){
        return modelMapper.map(user, RegisterResponse.class);
    }
}
