package com.devon.building.exception;

import com.devon.building.model.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException e){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(e.getMessage());
        List<String> details = new ArrayList<>();
        responseDTO.setDetail(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }
}
