package com.mango.common.util;

import cn.hutool.core.bean.copier.CopyOptions;
import org.apache.commons.lang3.StringUtils;

public class HutoolUtils {

    public static CopyOptions excludeNullBlankCopyOptions() {
        return CopyOptions.create()
                .setIgnoreNullValue(true)   // 排除null值
                .setFieldValueEditor((name, value) -> {
                    Object finalVale = value;
                    // 字符串类型的属性，排除空字符床
                    if ((value instanceof CharSequence) && StringUtils.isBlank((CharSequence) value)) {
                        finalVale = null;
                    }

                    return finalVale;
                });
    }

}
