package com.mango.common.dto.bkl;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialCreateDto {

    @NotBlank(message = "物料编码不能为空") // 不能为 null、空字符串或纯空格
    @Size(min = 2, max = 50, message = "物料编码长度必须在 {min} 到 {max} 之间")
    private String number;

    @NotBlank(message = "物料名称不能为空") // 不能为 null、空字符串或纯空格
    @Size(min = 2, max = 20, message = "物料名称长度必须在 {min} 到 {max} 之间")
    private String name;

    @NotNull
    @DecimalMin(value = "0.0001", inclusive = true)  // 确保数字大于0
    @Digits(integer = 20, fraction = 4)  // 限制整数和小数位数
    private BigDecimal inventory;

    @NotNull(message = "启用/禁用不能为空")
    private Boolean enable;

}
