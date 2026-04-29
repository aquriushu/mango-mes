package com.mango.bkl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.bkl.constant.RedisConst;
import com.mango.bkl.entity.Employee;
import com.mango.bkl.mapper.EmployeeMapper;
import com.mango.bkl.service.EmployeeService;
import com.mango.common.dto.bkl.EmployeeCreateDto;
import com.mango.common.dto.bkl.EmployeeDto;
import com.mango.common.dto.bkl.EmployeeUpdateDto;
import com.mango.common.exception.BizException;
import com.mango.common.util.HutoolUtils;
import com.mango.common.vo.bkl.EmployeeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public EmployeeVo insertOne(EmployeeCreateDto createDto) {
        // 数据库中手机号不能相同
        boolean exists = this.lambdaQuery().eq(Employee::getPhone, createDto.getPhone()).exists();
        if (exists) {
            throw new BizException("手机号已存在");
        }

        // 不能有相同的number+name 的员工
        exists = this.lambdaQuery().eq(Employee::getNumber, createDto.getNumber()).eq(Employee::getName, createDto.getName()).exists();
        if (exists) {
            throw new BizException("已存在相同【编码、姓名】的员工");
        }

        // 转换
        Employee employee = BeanUtil.copyProperties(createDto, Employee.class);
        // 设置默认值
        employee.setCreateTime(LocalDateTime.now());
        employee.setModifyTime(LocalDateTime.now());
        employee.setEnable(true);

        // 直接保存，这里的异常不要手动获取，直接使用MyBatis-Plus 的 save 方法内部抛出的异常，会由全局异常捕获。
        // 因为这里如果保存报错，并不是BizException可预测的异常（如：用户已存在）
        super.save(employee);
        EmployeeDto employeeDto = queryOneById(String.valueOf(employee.getId()));
        return BeanUtil.copyProperties(employeeDto, EmployeeVo.class);
    }

    @Override
    public EmployeeDto queryOneById(String id) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConst.EMPLOYEE_HASH_KEY_PREFIX + id);

        if (CollectionUtils.isEmpty(map)) {
            Employee employee = this.getById(id);
            if (employee == null) {
                throw new BizException("用户不存在");
            } else {
                EmployeeDto employeeDto = BeanUtil.copyProperties(employee, EmployeeDto.class);

                // 这两步不是原子操作，可改为lua脚本。当前使用降级方法：为redis操作添加try...catch，捕获异常，回滚
                try {
                    redisTemplate.opsForHash().putAll(RedisConst.EMPLOYEE_HASH_KEY_PREFIX + id, BeanUtil.beanToMap(employeeDto));
                    redisTemplate.expire(RedisConst.EMPLOYEE_HASH_KEY_PREFIX + id, RedisConst.EMPLOYEE_EXPIRE_TIME, TimeUnit.SECONDS);
                } catch (Exception e) {
                    // todo 记录日志
                    redisTemplate.delete(RedisConst.EMPLOYEE_HASH_KEY_PREFIX + id);
                    // 无需抛出异常，让结果从数据库正确返回
                    e.printStackTrace();
                }

                return BeanUtil.copyProperties(employee, EmployeeDto.class);
            }
        } else {
            // 缓存续期
            redisTemplate.expire(RedisConst.EMPLOYEE_HASH_KEY_PREFIX + id, RedisConst.EMPLOYEE_EXPIRE_TIME, TimeUnit.SECONDS);
            return BeanUtil.toBean(map, EmployeeDto.class);
        }
    }

    @Override
    public Boolean updateOneById(EmployeeUpdateDto updateDto) {
        // 判断是否存在相同手机号
        LambdaQueryWrapper<Employee> phoneWrapper = new LambdaQueryWrapper<>();
        // 排除自己
        phoneWrapper.ne(Employee::getId, updateDto.getId());
        if (StringUtils.isNotBlank(updateDto.getPhone())) {
            phoneWrapper.eq(Employee::getPhone, updateDto.getPhone());
            boolean exists = this.exists(phoneWrapper);
            if (exists) {
                throw new BizException("已存在相同手机号");
            }
        }

        // 判断是否存在相同number+name
        LambdaQueryWrapper<Employee> numberNameWrapper = new LambdaQueryWrapper<>();
        // 排除自己
        numberNameWrapper.ne(Employee::getId, updateDto.getId());
        if (StringUtils.isNotBlank(updateDto.getNumber()) && StringUtils.isNotBlank(updateDto.getName())) {
            numberNameWrapper.eq(Employee::getNumber, updateDto.getNumber()).eq(Employee::getName, updateDto.getName());
            boolean exists = this.exists(numberNameWrapper);
            if (exists) {
                throw new BizException("已存在相同员工编码和姓名");
            }
        }

        // 更新员工
        Employee newEmployee = new Employee();
        BeanUtil.copyProperties(updateDto, newEmployee, HutoolUtils.excludeNullBlankCopyOptions());
        this.updateById(newEmployee);
        // 删除缓存
        redisTemplate.delete(RedisConst.EMPLOYEE_HASH_KEY_PREFIX + newEmployee.getId());

        return true;
    }

    @Override
    public Boolean deleteOneById(String id) {
        // 判断是否存在
        boolean exists = this.lambdaQuery().eq(Employee::getId, id).exists();
        if (!exists) {
            throw new BizException("用户不存在");
        }

        redisTemplate.delete(RedisConst.EMPLOYEE_HASH_KEY_PREFIX + id);
        return this.removeById(id);
    }

}


