package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 数据计算
 * 例如：数字乘以10
 */
public class MultiplyByTenProcessor implements ValueProcessor {
    @Override
    public Object process(Object value) {
        System.out.println("开始执行MultiplyByTenProcessor，value为：" + value);

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::process);
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue() * 10;
        }
        return value;
    }
}
