package com.devon.building.model.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Tên không được để trống")
    private String fullName;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Pattern(
            regexp = "^[a-zA-Z0-9@]+$",
            message = "Tên đăng nhập không hợp lệ"
    )
    private String userName;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "Mật khẩu xác nhận không được để trống")
    private String confirmPassword;

    @AssertTrue(message = "Bạn phải đồng ý điều khoản dịch vụ bảo mật")
    private Boolean isAgreeTerms;


}
