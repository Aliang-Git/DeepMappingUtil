package com.aliang.strategy.impl;

import com.aliang.strategy.*;

import java.util.*;
import java.util.stream.*;

/**
 * 字符串连接聚合策略（带分隔符）
 * 将集合中的元素转换为字符串并用分隔符连接
 * <p>
 * 示例：
 * 输入：["电子产品", "手机", "智能设备"]
 * 输出："电子产品,手机,智能设备"
 * <p>
 * 特点：
 * 1. 所有元素都会被转换为字符串
 * 2. 默认使用逗号作为分隔符
 * 3. 空值或空集合返回null
 */
public class JoinAggregationStrategy implements AggregationStrategy {
    private final String delimiter;

    public JoinAggregationStrategy() {
        this(",");
    }

    public JoinAggregationStrategy(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public Object apply(List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.stream()
                .map(Object::toString)
                .collect(Collectors.joining(delimiter));
    }
} 