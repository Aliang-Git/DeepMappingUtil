package com.aliang.rule.strategy.impl;

import com.aliang.rule.strategy.*;

import java.util.*;
import java.util.stream.*;

/**
 * 字符串连接聚合策略（带分隔符）
 * 将集合中的元素转换为字符串并用分隔符连接
 * <p>
 * 示例：
 * 输入：["电子产品", "手机", "智能设备"]
 * 输出：
 * - keepArrayFormat=false时："电子产品,手机,智能设备"
 * - keepArrayFormat=true时："[电子产品, 手机, 智能设备]"
 * <p>
 * 特点：
 * 1. 所有元素都会被转换为字符串
 * 2. 默认使用逗号作为分隔符
 * 3. 空值或空集合返回null
 * 4. 可以配置是否保持数组格式
 */
public class JoinAggregationStrategy implements AggregationStrategy {
    private final String delimiter;
    private final boolean keepArrayFormat;


    public JoinAggregationStrategy(String delimiter, boolean keepArrayFormat) {
        this.delimiter = delimiter;
        this.keepArrayFormat = keepArrayFormat;
    }

    @Override
    public Object apply(List<?> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        String result = values.stream()
                .map(Object::toString)
                .collect(Collectors.joining(delimiter));

        return keepArrayFormat ? "[" + result + "]" : result;
    }
} 