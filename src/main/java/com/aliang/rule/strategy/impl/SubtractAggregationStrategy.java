package com.aliang.rule.strategy.impl;

import com.aliang.rule.strategy.*;

import java.math.*;
import java.util.*;

/**
 * 减法聚合策略
 * 第一个值减去其余所有值
 * <p>
 * 示例：
 * 输入：[1000, 600, 100] (1000为收入，600和100为成本)
 * 输出：300 (1000 - 600 - 100)
 * <p>
 * 特点：
 * 1. 只处理Number类型的值
 * 2. 使用BigDecimal保证精度
 * 3. 空值或空集合返回null
 * 4. 非数值类型会抛出IllegalArgumentException
 */
public class SubtractAggregationStrategy implements AggregationStrategy {
    @Override
    public Object apply(List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        if (!(values.get(0) instanceof Number)) {
            throw new IllegalArgumentException("SUBTRACT 策略只支持数字类型");
        }

        BigDecimal result = new BigDecimal(values.get(0).toString());
        for (int i = 1; i < values.size(); i++) {
            Object value = values.get(i);
            if (value instanceof Number) {
                result = result.subtract(new BigDecimal(value.toString()));
            } else {
                throw new IllegalArgumentException("SUBTRACT 策略只支持数字类型");
            }
        }
        return result;
    }
} 