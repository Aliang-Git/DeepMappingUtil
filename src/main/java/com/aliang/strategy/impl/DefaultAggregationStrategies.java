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

            return sum;  // 直接返回 BigDecimal
        }
    },

    /**
     * 平均值
     */
    AVERAGE {
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
            return sum.divide(new BigDecimal(numbers.size()));
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
                    .map(value -> new BigDecimal(value.toString()))
                    .max(BigDecimal::compareTo)
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
                    .map(value -> new BigDecimal(value.toString()))
                    .min(BigDecimal::compareTo)
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
            return values.get(0);
        }
    },

    /**
     * 最后一个值
     */
    LAST {
        @Override
        public Object apply(List<?> values) {
            if (values == null || values.isEmpty()) {
                return null;
            }
            return values.get(values.size() - 1);
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
            String delimiter = ",";
            return values.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(delimiter));
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
    },

    /**
     * 计数
     */
    COUNT {
        @Override
        public Object apply(List<?> values) {
            if (values == null) {
                return 0;
            }
            return values.size();
        }
    },

    /**
     * 连接
     */
    CONCAT {
        @Override
        public Object apply(List<?> values) {
            if (values == null || values.isEmpty()) {
                return null;
            }
            return values.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining());
        }
    }
}
