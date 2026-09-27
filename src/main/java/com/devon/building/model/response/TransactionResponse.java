package com.devon.building.model.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;

    private Long customerId;

    private String transactionType;

    private String note;

    private Date createdDate;

    private String createdBy;

    private Date modifiedDate;

    private String modifiedBy;
}
