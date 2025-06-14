package com.aliang.strategy.impl;

import com.aliang.strategy.*;

import java.util.*;

/**
 * 分组聚合策略
 * 原样返回集合，保持数组形式
 * <p>
 * 示例：
 * 输入：["64G", "128G", "256G"]
 * 输出：["64G", "128G", "256G"]
 * <p>
 * 特点：
 * 1. 保持原有集合结构
 * 2. null值返回空集合
 * 3. 不对元素进行任何转换
 */
public class GroupAggregationStrategy implements AggregationStrategy {
    @Override
    public Object apply(List<?> values) {
        return values == null ? Collections.emptyList() : values;
    }
} 