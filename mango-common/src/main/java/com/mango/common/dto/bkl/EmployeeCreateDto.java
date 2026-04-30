package com.mango.common.dto.bkl;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeCreateDto {

    @NotBlank(message = "员工编码不能为空") // 不能为 null、空字符串或纯空格
    @Size(min = 2, max = 50, message = "员工编码长度必须在 {min} 到 {max} 之间")
    private String number;

    @NotBlank(message = "员工姓名不能为空") // 不能为 null、空字符串或纯空格
//    @Size(min = 2, max = 10, message = "员工姓名长度必须在 {min} 到 {max} 之间")  // 这种校验只能校验字符长度，并非中文
    @Pattern(regexp = "^[\u4e00-\u9fa5]{2,5}$", message = "员工名称必须是2到5个汉字，且不能包含特殊符号")
    private String name;

    private Boolean enable;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String openId;

}
