package com.mango.bkl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Hu_jie
 * @since 2026-04-30
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

    @TableField("enable")
    private Boolean enable;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("modify_time")
    private LocalDateTime modifyTime;

}
