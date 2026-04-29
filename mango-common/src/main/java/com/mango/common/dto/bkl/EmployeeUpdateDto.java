package com.mango.common.dto.bkl;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeUpdateDto {

    @NotBlank(message = "员工ID不能为空")
    @Digits(integer = 19, fraction = 0, message = "员工ID长度不对")
    private String id;

    private String number;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    private Boolean enable;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    private String openId;

    @Email(message = "邮箱格式不正确")
    private String email;

}
