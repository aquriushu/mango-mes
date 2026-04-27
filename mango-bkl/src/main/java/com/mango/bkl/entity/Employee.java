package com.mango.bkl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
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
@TableName("t_employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "employee_id", type = IdType.ASSIGN_ID)
    private Long employeeId;

    @TableField("number")
    private String number;

    @TableField("name")
    private String name;

    @TableField("create_time")
    private Date createTime;

    @TableField("modify_time")
    private Date modifyTime;

    @TableField("enable")
    private Boolean enable;

    @TableField("phone")
    private String phone;

    @TableField("open_id")
    private String openId;

    @TableField("email")
    private String email;
}
