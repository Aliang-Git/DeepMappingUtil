package com.aliang.rule.strategy.impl;

import com.aliang.rule.strategy.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

/**
 * 平均值聚合策略
 * 计算集合中数值类型元素的平均值
 * <p>
 * 示例：
 * 输入：[4.5, 5.0, 4.0, 4.8]
 * 输出：4.575
 * <p>
 * 特点：
 * 1. 只处理Number类型的值
 * 2. 使用BigDecimal保证精度
 * 3. 空值、空集合或无数值类型元素返回null
 */
public class AverageAggregationStrategy implements AggregationStrategy {
    @Override
    public Object apply(List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        List<BigDecimal> numbers = values.stream()
                .filter(value -> value instanceof Number)
                .map(value -> new BigDecimal(value.toString()))
                .collect(Collectors.toList());
        if (numbers.isEmpty()) {
            return null;
        }
        BigDecimal sum = numbers.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(numbers.size()), 10, RoundingMode.HALF_UP);
    }
} 