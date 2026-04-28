package com.mango.bkl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.bkl.entity.Employee;
import com.mango.common.dto.bkl.EmployeeCreateDto;
import com.mango.common.vo.bkl.EmployeeVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Hu_jie
 * @since 2026-04-27
 */
public interface EmployeeService extends IService<Employee> {

    EmployeeVo insertOne(EmployeeCreateDto createDto);

    EmployeeVo queryOneById(String id);

}
