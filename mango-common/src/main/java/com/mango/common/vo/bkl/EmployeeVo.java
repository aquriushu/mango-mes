package com.mango.common.vo.bkl;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeVo {

    private Long id;
    private String number;
    private String name;
    private Date createTime;
    private Date modifyTime;
    private Boolean enable;
    private String phone;
    private String openId;
    private String email;

}
