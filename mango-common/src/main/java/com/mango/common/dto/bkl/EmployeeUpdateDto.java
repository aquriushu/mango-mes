package com.mango.common.dto.bkl;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeUpdateDto {

    @NotBlank(message = "员工ID不能为空")
    @Positive(message = "ID必须为正数")
    @Digits(integer = 19, fraction = 0, message = "员工ID长度不对")
    private String id;

    @Size(min = 2, max = 50, message = "员工编码长度必须在 {min} 到 {max} 之间")
    private String number;

    //    @Size(min = 2, max = 10, message = "员工姓名长度必须在 {min} 到 {max} 之间")  // 这种校验只能校验字符长度，并非中文
    @Pattern(regexp = "^[\u4e00-\u9fa5]{2,5}$", message = "员工名称必须是2到5个汉字，且不能包含特殊符号")
    private String name;

    private Boolean enable;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private String openId;

    @Email(message = "邮箱格式不正确")
    private String email;

}
