package com.devon.building.exception;

import com.devon.building.model.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(DataBuildingInvalidException.class)
    public ResponseEntity<Object> handleDataBuildingInvalidException(DataBuildingInvalidException e){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(e.getMessage());
        List<String> details = new ArrayList<>();
        responseDTO.setDetail(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }
}
