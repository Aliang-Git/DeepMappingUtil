package com.aliang.processor;

/**
 * 字段值处理器接口，定义字段处理标准
 * 对字段值进行处理（如格式化、转换）
 */
@FunctionalInterface
public interface ValueProcessor {
    Object process(Object value);
}
