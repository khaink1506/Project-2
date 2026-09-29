package com.devon.building.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequest {

    @NotBlank(message = "")
    private String fullName;

    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại bắt đầu bằng số 0 và có 10 chữ số")
    private String phone;

    private String demand;
}
