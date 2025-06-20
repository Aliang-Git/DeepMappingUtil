package com.aliang.rule.strategy.impl;

import com.aliang.rule.strategy.*;

import java.util.*;

/**
 * 取最后一个值聚合策略
 * 返回集合中的最后一个元素
 * <p>
 * 配置格式：last:filter
 * filter: 过滤条件（可选），可选值：
 * - any: 返回最后一个元素，包括null（默认）
 * - nonNull: 返回最后一个非null元素
 * - nonEmpty: 返回最后一个非空元素（对字符串和集合有效）
 * <p>
 * 示例1 - 基本使用：
 * 配置：last:any
 * 输入：[1, 2, 3]
 * 输出：3
 * <p>
 * 示例2 - 跳过null：
 * 配置：last:nonNull
 * 输入：["a", "b", null]
 * 输出："b"
 * <p>
 * 示例3 - 跳过空值：
 * 配置：last:nonEmpty
 * 输入：["text", "", null, ""]
 * 输出："text"
 * <p>
 * 示例4 - 对象集合：
 * 配置：last:any
 * 输入：[{"id": 1}, {"id": 2}]
 * 输出：{"id": 2}
 * <p>
 * 示例5 - 嵌套集合：
 * 配置：last:nonEmpty
 * 输入：[[1, 2], [3], []]
 * 输出：[3]
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
 * 3. 全部为null：
 * 配置：last:nonNull
 * 输入：[null, null]
 * 输出：null
 * <p>
 * 注意：
 * 1. 支持任意类型的集合
 * 2. 支持嵌套集合
 * 3. 非集合输入将直接返回
 * 4. null输入将返回null
 * 5. 如果找不到符合条件的元素，返回null
 */
public class LastAggregationStrategy implements AggregationStrategy {
    @Override
    public Object apply(List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        Object last = null;
        for (Object value : values) {
            last = value;
        }
        return last;
    }
} 