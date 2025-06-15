package com.aliang.rule.strategy.impl;

import com.aliang.rule.strategy.*;

import java.math.*;
import java.util.*;

/**
 * 取最小值聚合策略
 * 返回集合中的最小值
 * <p>
 * 配置格式：min:type
 * type: 值类型（可选），可选值：
 * - number: 数值类型（默认）
 * - string: 字符串类型
 * - date: 日期类型
 * <p>
 * 示例1 - 数字比较：
 * 配置：min:number
 * 输入：[5, 1, 3, 2]
 * 输出：1
 * <p>
 * 示例2 - 字符串比较：
 * 配置：min:string
 * 输入：["c", "a", "b"]
 * 输出："a"
 * <p>
 * 示例3 - 小数比较：
 * 配置：min:number
 * 输入：[2.3, 1.5, 1.8]
 * 输出：1.5
 * <p>
 * 示例4 - 日期比较：
 * 配置：min:date
 * 输入：["2023-12-31", "2023-01-01", "2023-06-15"]
 * 输出："2023-01-01"
 * <p>
 * 示例5 - 混合类型：
 * 配置：min:number
 * 输入：[3.5, "1", 2]
 * 输出：1
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
 * 输入：[3, null, 1]
 * 输出：1
 * <p>
 * 注意：
 * 1. 自动类型转换
 * 2. 忽略无法转换的值
 * 3. 支持BigDecimal精确计算
 * 4. null值会被忽略
 * 5. 空集合返回null
 */
public class MinAggregationStrategy implements AggregationStrategy {
    @Override
    public Object apply(List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        BigDecimal min = null;
        for (Object value : values) {
            if (value == null) {
                continue;
            }

            try {
                BigDecimal current = new BigDecimal(value.toString());
                if (min == null || current.compareTo(min) < 0) {
                    min = current;
                }
            } catch (NumberFormatException ignored) {
                /*  忽略无法转换的值 */
            }
        }

        return min;
    }
} 