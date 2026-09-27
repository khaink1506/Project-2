package com.devon.building.model.request;

import com.devon.building.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {

    private Long id;

    @NotBlank(message = "Tên khách hàng không được để trống ")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại bắt đầu bằng số 0 và có 10 chữ số")
    private String phone;

    private String email;

    private String companyName;

    private String demand;

    @NotNull(message = "Trạng thái không được để trống")
    private Status status;
}
