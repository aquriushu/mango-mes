package com.mango.common.dto.bkl;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MaterialUpdateDto {

    @NotBlank(message = "物料ID不能为空")
    @Positive(message = "ID必须为正数")
    @Digits(integer = 19, fraction = 0, message = "物料ID长度不对")
    private String id;

    @Size(min = 2, max = 50, message = "物料编码长度必须在 {min} 到 {max} 之间")
    private String number;

    @Size(min = 2, max = 20, message = "物料名称长度必须在 {min} 到 {max} 之间")
    private String name;

    private Boolean enable;

}



