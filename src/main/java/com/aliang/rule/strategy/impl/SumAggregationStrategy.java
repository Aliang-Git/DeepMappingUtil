package com.aliang.rule.strategy.impl;

import com.aliang.rule.strategy.*;

import java.math.*;
import java.util.*;

/**
 * 求和聚合策略
 * 将集合中的数值类型元素进行求和
 * <p>
 * 示例：
 * 输入：[100, 200, 300]
 * 输出：600
 * <p>
 * 特点：
 * 1. 只处理Number类型的值
 * 2. 使用BigDecimal保证精度
 * 3. 空值或空集合返回null
 */
public class SumAggregationStrategy implements AggregationStrategy {
    @Override
    public Object apply(List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        BigDecimal sum = values.stream()
                .filter(value -> value instanceof Number)
                .map(value -> new BigDecimal(value.toString()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum;
    }
} 