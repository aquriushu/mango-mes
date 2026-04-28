package com.mango.bkl.controller;

import com.mango.bkl.service.EmployeeService;
import com.mango.common.domain.ApiResult;
import com.mango.common.dto.bkl.EmployeeCreateDto;
import com.mango.common.vo.bkl.EmployeeVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/*
 * JavaDoc 解释
 * 第一行：在Apifox中会成为接口列表的分组
 * 第二行：组的描述信息
 * 第三行：@module 标签：用于指定该接口所属的项目模块，便于在 Apifox 中按模块筛选
 *
 * @author Hu_jie
 * @since 2026-04-27
 */

/**
 * 用户管理
 * 处理用户相关的操作
 *
 * @module mango-bkl
 */
@RestController
@RequestMapping("/employee")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /*
     * JavaDoc 解释
     * 第一行（api名称）：在 Apifox 中会作为接口名称
     * 第二行（api描述）：作为接口描述，说明接口的功能、场景等
     *
     * @param ：employee 用于描述方法参数的名称和含义，Apifox 会据此生成请求参数说明
     * @return ：用于描述接口的响应信息
     */

    /**
     * 插入单个员工
     *
     * @param
     * @return
     */
    // @Valid支持嵌套，是java标准。@Validated不支持嵌套，是spring增强版
    @PostMapping("/v1/insertOne")
    public ApiResult<EmployeeVo> insertOne(@RequestBody @Valid EmployeeCreateDto createDto) {
        return ApiResult.success(employeeService.insertOne(createDto));
    }

    /**
     * 根据ID查询单个员工
     *
     * @param id
     * @return
     */
    @GetMapping("/v1/queryOneById")
    public ApiResult<EmployeeVo> queryOneById(
            @RequestParam(required = false) // 去掉springmvc的校验，使用下面的NotBlank校验
            @NotBlank(message = "id不能为空")
            @Digits(integer = 19, fraction = 0, message = "员工ID长度不对")
                    String id) {
        return ApiResult.success(employeeService.queryOneById(id));
    }

}
