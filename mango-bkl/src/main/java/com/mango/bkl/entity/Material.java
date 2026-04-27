package com.mango.bkl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hu_jie
 * @since 2026-04-27
 */
@Getter
@Setter
@TableName("t_material")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("number")
    private String number;

    @TableField("name")
    private String name;

    @TableField("inventory")
    private BigDecimal inventory;
}
