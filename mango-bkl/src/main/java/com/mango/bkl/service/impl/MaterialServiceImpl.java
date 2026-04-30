package com.mango.bkl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.bkl.constant.RedisConst;
import com.mango.bkl.entity.Material;
import com.mango.bkl.mapper.MaterialMapper;
import com.mango.bkl.service.MaterialService;
import com.mango.common.dto.bkl.MaterialCreateDto;
import com.mango.common.dto.bkl.MaterialDto;
import com.mango.common.dto.bkl.MaterialUpdateDto;
import com.mango.common.exception.BizException;
import com.mango.common.util.HutoolUtils;
import com.mango.common.vo.bkl.MaterialVo;
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
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public MaterialVo insertOne(MaterialCreateDto createDto) {
        // 不能有相同的number+name 的员工
        boolean exists = this.lambdaQuery().eq(Material::getNumber, createDto.getNumber()).eq(Material::getName, createDto.getName()).exists();
        if (exists) {
            throw new BizException("已存在相同【编码、姓名】的物料");
        }

        // 转换
        Material material = BeanUtil.copyProperties(createDto, Material.class);
        // 设置默认值
        material.setCreateTime(LocalDateTime.now());
        material.setModifyTime(LocalDateTime.now());
        material.setEnable(true);

        // 直接保存，这里的异常不要手动获取，直接使用MyBatis-Plus 的 save 方法内部抛出的异常，会由全局异常捕获。
        // 因为这里如果保存报错，并不是BizException可预测的异常（如：用户已存在）
        super.save(material);
        MaterialDto materialDto = queryOneById(String.valueOf(material.getId()));
        return BeanUtil.copyProperties(materialDto, MaterialVo.class);
    }

    @Override
    public MaterialDto queryOneById(String id) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConst.MATERIAL_HASH_KEY_PREFIX + id);

        if (CollectionUtils.isEmpty(map)) {
            Material material = this.getById(id);
            if (material == null) {
                throw new BizException("物料不存在");
            } else {
                MaterialDto materialDto = BeanUtil.copyProperties(material, MaterialDto.class);

                // 这两步不是原子操作，可改为lua脚本。当前使用降级方法：为redis操作添加try...catch，捕获异常，回滚
                try {
                    redisTemplate.opsForHash().putAll(RedisConst.MATERIAL_HASH_KEY_PREFIX + id, BeanUtil.beanToMap(materialDto));
                    redisTemplate.expire(RedisConst.MATERIAL_HASH_KEY_PREFIX + id, RedisConst.MATERIAL_EXPIRE_TIME, TimeUnit.SECONDS);
                } catch (Exception e) {
                    // todo 记录日志
                    redisTemplate.delete(RedisConst.MATERIAL_HASH_KEY_PREFIX + id);
                    // 无需抛出异常，让结果从数据库正确返回
                    e.printStackTrace();
                }

                return BeanUtil.copyProperties(material, MaterialDto.class);
            }
        } else {
            // 缓存续期
            redisTemplate.expire(RedisConst.MATERIAL_HASH_KEY_PREFIX + id, RedisConst.MATERIAL_EXPIRE_TIME, TimeUnit.SECONDS);
            return BeanUtil.toBean(map, MaterialDto.class);
        }
    }

    @Override
    public Boolean deleteOneById(String id) {
        // 判断是否存在
        boolean exists = this.lambdaQuery().eq(Material::getId, id).exists();
        if (!exists) {
            throw new BizException("物料不存在");
        }

        redisTemplate.delete(RedisConst.MATERIAL_HASH_KEY_PREFIX + id);
        return this.removeById(id);
    }

    @Override
    public Boolean updateOneById(MaterialUpdateDto updateDto) {
        // 判断是否存在
        boolean exists = this.lambdaQuery().eq(Material::getId, updateDto.getId()).exists();
        if (!exists) {
            throw new BizException("物料不存在");
        }

        // 判断是否存在相同number+name
        LambdaQueryWrapper<Material> numberNameWrapper = new LambdaQueryWrapper<>();
        // 排除自己
        numberNameWrapper.ne(Material::getId, updateDto.getId());
        if (StringUtils.isNotBlank(updateDto.getNumber()) && StringUtils.isNotBlank(updateDto.getName())) {
            numberNameWrapper.eq(Material::getNumber, updateDto.getNumber()).eq(Material::getName, updateDto.getName());
            exists = this.exists(numberNameWrapper);
            if (exists) {
                throw new BizException("已存在相同物料编码和姓名");
            }
        }

        // 更新物料
        Material material = new Material();
        BeanUtil.copyProperties(updateDto, material, HutoolUtils.excludeNullBlankCopyOptions());
        material.setModifyTime(LocalDateTime.now());
        this.updateById(material);
        // 删除缓存
        redisTemplate.delete(RedisConst.MATERIAL_HASH_KEY_PREFIX + material.getId());

        return true;
    }


}
