package com.mango.bkl.controller;

import cn.hutool.core.bean.BeanUtil;
import com.mango.bkl.service.MaterialService;
import com.mango.common.domain.ApiResult;
import com.mango.common.dto.bkl.MaterialCreateDto;
import com.mango.common.dto.bkl.MaterialDto;
import com.mango.common.dto.bkl.MaterialUpdateDto;
import com.mango.common.vo.bkl.MaterialVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 物料管理
 *
 * @author Hu_jie
 * @since 2026-04-27
 */
@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    /**
     * 单条：添加物料
     *
     * @param createDto
     * @return
     */
    @PostMapping("/v1/insertOne")
    public ApiResult<MaterialVo> insertOne(@RequestBody MaterialCreateDto createDto) {
        return ApiResult.success(materialService.insertOne(createDto));
    }

    /**
     * 单条：根据ID查询物料
     *
     * @param id
     * @return
     */
    @GetMapping("/v1/queryOneById")
    public ApiResult<MaterialVo> queryOneById(
            @RequestParam(required = false)   // 去掉springmvc的校验，使用下面的NotBlank校验
            @NotBlank(message = "id不能为空")
            @Digits(integer = 19, fraction = 0, message = "物料 ID 长度不对")
                    String id) {
        MaterialDto materialDto = materialService.queryOneById(id);
        MaterialVo materialVo = BeanUtil.copyProperties(materialDto, MaterialVo.class);
        return ApiResult.success(materialVo);
    }

    /**
     * 单条：根据ID删除物料
     * @param id
     * @return
     */
    @DeleteMapping("/v1/deleteOneById/{id}")
    public ApiResult<Boolean> deleteOneById(@PathVariable String id) {
        return ApiResult.success(materialService.deleteOneById(id));
    }

    /**
     * 单条：根据ID更新物料
     * 不更新库存，更新库存使用单独的更新库存接口
     *
     * @param updateDto
     * @return
     */
    @PostMapping("/v1/updateOneById")
    public ApiResult<Boolean> updateOneById(@RequestBody @Valid MaterialUpdateDto updateDto) {
        return ApiResult.success(materialService.updateOneById(updateDto));
    }


}
