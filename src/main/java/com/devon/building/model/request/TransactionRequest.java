package com.devon.building.model.request;

import com.devon.building.enums.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private Transaction transaction;

    @NotBlank(message = "Chi tiết giao dịch không được để trống")
    private String note;
}
