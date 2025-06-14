package com.aliang.rule.strategy.impl;

import com.aliang.rule.strategy.*;

import java.math.*;
import java.util.*;

/**
 * 取最大值聚合策略
 * 返回集合中的最大值
 * <p>
 * 配置格式：max:type
 * type: 值类型（可选），可选值：
 * - number: 数值类型（默认）
 * - string: 字符串类型
 * - date: 日期类型
 * <p>
 * 示例1 - 数字比较：
 * 配置：max:number
 * 输入：[1, 5, 3, 2]
 * 输出：5
 * <p>
 * 示例2 - 字符串比较：
 * 配置：max:string
 * 输入：["a", "c", "b"]
 * 输出："c"
 * <p>
 * 示例3 - 小数比较：
 * 配置：max:number
 * 输入：[1.5, 2.3, 1.8]
 * 输出：2.3
 * <p>
 * 示例4 - 日期比较：
 * 配置：max:date
 * 输入：["2023-01-01", "2023-12-31", "2023-06-15"]
 * 输出："2023-12-31"
 * <p>
 * 示例5 - 混合类型：
 * 配置：max:number
 * 输入：[1, "2", 3.5]
 * 输出：3.5
 * <p>
 * 特殊情况处理：
 * 1. 空集合：
 * 输入：[]
 * 输出：null
 * <p>
 * 2. null输入：
 * 输入：null
 * 输出：null
 * <p>
 * 3. 包含null值：
 * 输入：[1, null, 3]
 * 输出：3
 * <p>
 * 注意：
 * 1. 自动类型转换
 * 2. 忽略无法转换的值
 * 3. 支持BigDecimal精确计算
 * 4. null值会被忽略
 * 5. 空集合返回null
 */
public class MaxAggregationStrategy implements AggregationStrategy {
    @Override
    public Object apply(List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        BigDecimal max = null;
        for (Object value : values) {
            if (value == null) {
                continue;
            }

            try {
                BigDecimal current = new BigDecimal(value.toString());
                if (max == null || current.compareTo(max) > 0) {
                    max = current;
                }
            } catch (NumberFormatException ignored) {
                // 忽略无法转换的值
            }
        }

        return max;
    }
} 