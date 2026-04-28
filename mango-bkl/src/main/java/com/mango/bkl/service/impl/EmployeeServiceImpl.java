package com.mango.bkl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mango.bkl.entity.Employee;
import com.mango.bkl.mapper.EmployeeMapper;
import com.mango.bkl.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.common.dto.bkl.EmployeeCreateDto;
import com.mango.common.exception.BizException;
import com.mango.common.vo.bkl.EmployeeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Hu_jie
 * @since 2026-04-27
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public EmployeeVo insertOne(EmployeeCreateDto createDto) {
        // 数据库中手机号不能相同
        LambdaQueryWrapper<Employee> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(Employee::getPhone, createDto.getPhone());
        long phoneCount = this.count(phoneWrapper);
        if (phoneCount != 0) {
            throw new BizException("手机号已存在");
        }

        // 不能有相同的number+name 的员工
        LambdaQueryWrapper<Employee> numberNameWrapper = new LambdaQueryWrapper<>();
        numberNameWrapper.eq(Employee::getNumber, createDto.getNumber());
        numberNameWrapper.eq(Employee::getName, createDto.getName());
        long count = this.count(numberNameWrapper);
        if (count > 0) {
            throw new BizException("已存在相同【编码、姓名】的员工");
        }

        // 转换
        Employee employee = BeanUtil.copyProperties(createDto, Employee.class);
        // 直接保存，这里的异常不要手动获取，直接使用MyBatis-Plus 的 save 方法内部抛出的异常，会由全局异常捕获。
        // 因为这里如果保存报错，并不是BizException可预测的异常（如：用户已存在）
        this.save(employee);

        return null;
    }

    @Override
    public EmployeeVo queryOneById(String id) {
        return null;
    }

}
