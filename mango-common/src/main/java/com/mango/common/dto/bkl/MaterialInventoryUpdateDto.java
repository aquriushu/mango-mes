package com.mango.common.dto.bkl;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialInventoryUpdateDto {

    @NotBlank(message = "物料ID不能为空")
    @Positive(message = "ID必须为正数")
    @Digits(integer = 19, fraction = 0, message = "物料ID长度不对")
    private String id;

    @NotNull
    @DecimalMin(value = "0.0001", inclusive = true)  // 确保数字大于0
    @Digits(integer = 20, fraction = 4)  // 限制整数和小数位数
    private BigDecimal inventory;

}


