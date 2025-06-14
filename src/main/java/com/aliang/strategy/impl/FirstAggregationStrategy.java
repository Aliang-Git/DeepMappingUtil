package com.aliang.strategy.impl;

import com.aliang.strategy.*;

import java.util.*;

/**
 * 取第一个值聚合策略
 * 返回集合中的第一个元素
 * <p>
 * 配置格式：first:filter
 * filter: 过滤条件（可选），可选值：
 * - any: 返回第一个元素，包括null（默认）
 * - nonNull: 返回第一个非null元素
 * - nonEmpty: 返回第一个非空元素（对字符串和集合有效）
 * <p>
 * 示例1 - 基本使用：
 * 配置：first:any
 * 输入：[1, 2, 3]
 * 输出：1
 * <p>
 * 示例2 - 跳过null：
 * 配置：first:nonNull
 * 输入：[null, "a", "b"]
 * 输出："a"
 * <p>
 * 示例3 - 跳过空值：
 * 配置：first:nonEmpty
 * 输入：["", null, "text", ""]
 * 输出："text"
 * <p>
 * 示例4 - 对象集合：
 * 配置：first:any
 * 输入：[{"id": 1}, {"id": 2}]
 * 输出：{"id": 1}
 * <p>
 * 示例5 - 嵌套集合：
 * 配置：first:nonEmpty
 * 输入：[[], [1, 2], [3]]
 * 输出：[1, 2]
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
 * 配置：first:nonNull
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
public class FirstAggregationStrategy implements AggregationStrategy {
    @Override
    public Object apply(List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        Iterator<?> iterator = values.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }
} 