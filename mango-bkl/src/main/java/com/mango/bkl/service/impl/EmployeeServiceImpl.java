package com.mango.bkl.service.impl;

import com.mango.bkl.entity.Employee;
import com.mango.bkl.mapper.EmployeeMapper;
import com.mango.bkl.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.common.dto.bkl.EmployeeCreateDto;
import com.mango.common.vo.bkl.EmployeeVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hu_jie
 * @since 2026-04-27
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public EmployeeVo insertOne(EmployeeCreateDto createDto) {
        return null;
    }
}
