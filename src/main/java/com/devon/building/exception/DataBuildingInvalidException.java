package com.devon.building.exception;

import java.io.Serial;

public class DataBuildingInvalidException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public DataBuildingInvalidException(String message) {
        super(message);
    }
}
