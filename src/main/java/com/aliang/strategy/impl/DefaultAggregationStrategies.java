package com.aliang.strategy.impl;

import com.aliang.strategy.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

public enum DefaultAggregationStrategies implements AggregationStrategy {
    /**
     * 求和（仅 Number 类型）
     */
    SUM {
        @Override
        public Object apply(List<?> values) {
            if (values == null || values.isEmpty()) {
                return null;
            }
            
            BigDecimal sum = values.stream()
                    .filter(value -> value instanceof Number)
                    .map(value -> new BigDecimal(value.toString()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            return sum.setScale(2, RoundingMode.HALF_UP);  // 直接返回 BigDecimal，保留两位小数
        }
    },

    /**
     * 平均值
     */
    AVERAGE {
        @Override
        public Object apply(List<?> values) {
            if (values.isEmpty()) return null;
            double average = values.stream()
                    .filter(v -> v instanceof Number)
                    .mapToDouble(v -> ((Number)v).doubleValue())
                    .average()
                    .orElse(0);
            return average;
        }
    },

    /**
     * 最大值
     */
    MAX {
        @Override
        public Object apply(List<?> values) {
            if (values == null || values.isEmpty()) {
                return null;
            }
            
            return values.stream()
                    .filter(value -> value instanceof Number)
                    .map(value -> ((Number) value).doubleValue())
                    .max(Double::compareTo)
                    .orElse(null);
        }
    },

    /**
     * 最小值
     */
    MIN {
        @Override
        public Object apply(List<?> values) {
            if (values == null || values.isEmpty()) {
                return null;
            }
            
            return values.stream()
                    .filter(value -> value instanceof Number)
                    .map(value -> ((Number) value).doubleValue())
                    .min(Double::compareTo)
                    .orElse(null);
        }
    },

    /**
     * 第一个值
     */
    FIRST {
        @Override
        public Object apply(List<?> values) {
            if (values == null || values.isEmpty()) {
                return null;
            }
            Object first = values.get(0);
            // 如果第一个值也是列表，返回其第一个元素
            if (first instanceof List && !((List<?>) first).isEmpty()) {
                return ((List<?>) first).get(0);
            }
            return first;
        }
    },

    /**
     * 最后一个值
     */
    LAST {
        @Override
        public Object apply(List<?> values) {
            return values == null || values.isEmpty() ? null : values.get(values.size() - 1);
        }
    },

    /**
     * 所有值拼接成字符串（逗号分隔）
     */
    JOIN {
        @Override
        public Object apply(List<?> values) {
            if (values == null || values.isEmpty()) {
                return null;
            }
            return values.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        }
    },

    /**
     * 原样返回数组
     */
    GROUP {
        @Override
        public Object apply(List<?> values) {
            return values == null ? Collections.emptyList() : values;
        }
    },

    /**
     * 第一个值减去其余值
     */
    SUBTRACT {
        @Override
        public Object apply(List<?> values) {
            if (values == null || values.isEmpty()) {
                return null;
            }
            
            if (!(values.get(0) instanceof Number)) {
                throw new IllegalArgumentException("SUBTRACT 策略只支持数字类型");
            }
            
            double result = ((Number) values.get(0)).doubleValue();
            for (int i = 1; i < values.size(); i++) {
                Object value = values.get(i);
                if (value instanceof Number) {
                    result -= ((Number) value).doubleValue();
                } else {
                    throw new IllegalArgumentException("SUBTRACT 策略只支持数字类型");
                }
            }
            return result;
        }
    };
}
