package com.devon.building.model.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSearchResponse {

    private Long id;

    private String fullName;

    private String phone;

    private String email;

    private String demand;

    private Date createdDate;

    private String createdBy;

    private String status;
}
