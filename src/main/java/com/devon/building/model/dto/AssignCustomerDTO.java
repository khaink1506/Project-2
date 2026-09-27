package com.devon.building.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignCustomerDTO {

    @NotNull(message = "Customer id not found")
    Long customerId;
    List<Long> staffIds = new ArrayList<>();
}
