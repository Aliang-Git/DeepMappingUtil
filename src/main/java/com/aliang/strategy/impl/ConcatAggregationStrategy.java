package com.aliang.strategy.impl;

import com.aliang.strategy.*;

import java.util.*;
import java.util.stream.*;

/**
 * 字符串连接聚合策略（无分隔符）
 * 将集合中的元素直接连接成字符串
 * <p>
 * 示例：
 * 输入：["A", "B", "C"]
 * 输出："ABC"
 * <p>
 * 特点：
 * 1. 所有元素都会被转换为字符串
 * 2. 直接连接，不使用分隔符
 * 3. 空值或空集合返回null
 */
public class ConcatAggregationStrategy implements AggregationStrategy {
    @Override
    public Object apply(List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }
} 