package com.aliang.strategy.impl;

import com.aliang.strategy.*;

import java.util.*;

/**
 * 计数聚合策略
 * 统计集合中的元素数量
 * <p>
 * 配置格式：count:filter
 * filter: 过滤条件（可选），可选值：
 * - all: 统计所有元素（默认）
 * - nonNull: 只统计非null元素
 * - nonEmpty: 只统计非空元素（对字符串和集合有效）
 * <p>
 * 示例1 - 基本计数：
 * 配置：count:all
 * 输入：[1, 2, 3, null, ""]
 * 输出：5
 * <p>
 * 示例2 - 非null计数：
 * 配置：count:nonNull
 * 输入：[1, 2, null, 4, null]
 * 输出：3
 * <p>
 * 示例3 - 非空计数：
 * 配置：count:nonEmpty
 * 输入：["a", "", "b", null, "c"]
 * 输出：3
 * <p>
 * 示例4 - 嵌套集合：
 * 配置：count:all
 * 输入：[[1, 2], [3], [], [4, 5]]
 * 输出：4
 * <p>
 * 示例5 - 对象集合：
 * 配置：count:nonNull
 * 输入：[{"id": 1}, null, {"id": 2}]
 * 输出：2
 * <p>
 * 特殊情况处理：
 * 1. 空集合：
 * 输入：[]
 * 输出：0
 * <p>
 * 2. null输入：
 * 输入：null
 * 输出：0
 * <p>
 * 3. 非集合输入：
 * 输入："single"
 * 输出：1
 * <p>
 * 注意：
 * 1. 支持任意类型的集合
 * 2. 支持嵌套集合
 * 3. 非集合输入将被视为单个元素
 * 4. null输入将返回0
 * 5. 过滤条件对不同类型的处理方式不同
 */
public class CountAggregationStrategy implements AggregationStrategy {
    @Override
    public Object apply(List<?> values) {
        if (values == null) {
            return 0;
        }
        return values.size();
    }
} 