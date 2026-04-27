package com.mango.common.dto.bkl;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeCreateDto {

    @NotBlank(message = "员工编码不能为空") // 不能为 null、空字符串或纯空格
    @Size(min = 2, max = 50, message = "员工编码长度必须在 {min} 到 {max} 之间")
    private String number;

    @NotBlank(message = "员工姓名不能为空") // 不能为 null、空字符串或纯空格
    @Size(min = 2, max = 10, message = "员工姓名长度必须在 {min} 到 {max} 之间")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;
    private Boolean enable;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

}
