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
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue() * 10;
        }
        return value;
    }
}
