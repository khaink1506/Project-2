package com.devon.building.model.request;

import com.devon.building.enums.Status;
import lombok.*;


@Getter
@Setter
public class CustomerSearchRequest {
    private String fullName;
    private String phone;
    private String email;
    private Long staffId;
    private Status status;
    private Integer page = 1;
}
