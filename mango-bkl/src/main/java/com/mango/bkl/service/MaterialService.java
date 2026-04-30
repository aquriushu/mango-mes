package com.mango.bkl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.bkl.entity.Material;
import com.mango.common.dto.bkl.MaterialCreateDto;
import com.mango.common.dto.bkl.MaterialDto;
import com.mango.common.dto.bkl.MaterialUpdateDto;
import com.mango.common.vo.bkl.MaterialVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Hu_jie
 * @since 2026-04-27
 */
public interface MaterialService extends IService<Material> {

    MaterialVo insertOne(MaterialCreateDto createDto);

    MaterialDto queryOneById(String id);

    Boolean deleteOneById(String employeeId);

    Boolean updateOneById(MaterialUpdateDto updateDto);

}
