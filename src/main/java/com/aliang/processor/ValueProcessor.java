package com.aliang.processor;

import com.aliang.util.MappingLogger;

/**
 * 字段值处理器接口，定义字段处理标准
 * 对字段值进行处理（如格式化、转换）
 */
public interface ValueProcessor {
    /**
     * 处理值
     * @param value 输入值
     * @param productCode 产品代码
     * @param fieldPath 字段路径
     * @return 处理后的值
     */
    default Object process(Object value, String productCode, String fieldPath) {
        Object result = doProcess(value);
        MappingLogger.logProcessor(productCode, getClass().getSimpleName(), fieldPath, value, result);
        return result;
    }

    /**
     * 实际的处理逻辑
     * @param value 输入值
     * @return 处理后的值
     */
    Object doProcess(Object value);
}
