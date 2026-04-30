package com.mango.common.dto.bkl;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeQueryDto {

    @Positive(message = "ID必须为正数")
    @Digits(integer = 19, fraction = 0, message = "员工ID长度不对")
    private String id;

    private String number;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    private Boolean enable;
    private String phone;
    private String openId;
    private String email;

}
