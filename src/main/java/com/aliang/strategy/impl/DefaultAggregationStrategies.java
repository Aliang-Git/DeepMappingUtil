package com.aliang.strategy.impl;

import com.aliang.strategy.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

/**
 * 默认聚合策略实现
 * 提供一组常用的聚合操作
 * 
 * 示例场景：电商订单数据处理
 * 
 * 1. SUM - 求和
 * 场景：计算订单总金额
 * 输入：[{"price": 100}, {"price": 200}, {"price": 300}]
 * 配置：aggregationStrategies: ["sum"]
 * 输出：600
 * 
 * 2. AVERAGE - 平均值
 * 场景：计算商品平均评分
 * 输入：[4.5, 5.0, 4.0, 4.8]
 * 配置：aggregationStrategies: ["average"]
 * 输出：4.575
 * 
 * 3. MAX - 最大值
 * 场景：获取最高销售额
 * 输入：[1000, 1500, 800, 2000]
 * 配置：aggregationStrategies: ["max"]
 * 输出：2000
 * 
 * 4. MIN - 最小值
 * 场景：获取最低库存量
 * 输入：[100, 50, 75, 25]
 * 配置：aggregationStrategies: ["min"]
 * 输出：25
 * 
 * 5. FIRST - 第一个值
 * 场景：获取最早的订单日期
 * 输入：["2024-03-20", "2024-03-21", "2024-03-22"]
 * 配置：aggregationStrategies: ["first"]
 * 输出："2024-03-20"
 * 
 * 6. LAST - 最后一个值
 * 场景：获取最近的交易时间
 * 输入：["10:00", "11:30", "14:20", "16:00"]
 * 配置：aggregationStrategies: ["last"]
 * 输出："16:00"
 * 
 * 7. JOIN - 字符串连接（带分隔符）
 * 场景：商品标签组合
 * 输入：["电子产品", "手机", "智能设备"]
 * 配置：aggregationStrategies: ["join"]
 * 输出："电子产品,手机,智能设备"
 * 
 * 8. GROUP - 保持数组形式
 * 场景：保留商品规格列表
 * 输入：["64G", "128G", "256G"]
 * 配置：aggregationStrategies: ["group"]
 * 输出：["64G", "128G", "256G"]
 * 
 * 9. COUNT - 计数
 * 场景：统计订单项数量
 * 输入：[item1, item2, item3, item4]
 * 配置：aggregationStrategies: ["count"]
 * 输出：4
 * 
 * 10. CONCAT - 字符串直接连接
 * 场景：组合商品编码
 * 输入：["A", "B", "C"]
 * 配置：aggregationStrategies: ["concat"]
 * 输出："ABC"
 * 
 * 11. SUBTRACT - 减法
 * 场景：计算利润（收入减去成本）
 * 输入：[1000, 600, 100] (1000为收入，600和100为成本)
 * 配置：aggregationStrategies: ["subtract"]
 * 输出：300 (1000 - 600 - 100)
 * 
 * 高级用法 - 策略组合：
 * 
 * 1. 求和后格式化
 * 场景：计算总金额并格式化
 * 配置：
 * aggregationStrategies: ["sum"]
 * processors: ["format:￥%.2f"]
 * 输入：[100, 200, 300]
 * 输出："￥600.00"
 * 
 * 2. 平均值后转换等级
 * 场景：评分均值转等级
 * 配置：
 * aggregationStrategies: ["average"]
 * processors: ["range:0,5,1,10"]
 * 输入：[4.5, 5.0, 4.0]
 * 输出：8.5
 * 
 * 注意事项：
 * 1. 数值计算使用BigDecimal保证精度
 * 2. 空值和异常处理会返回null或空集合
 * 3. 支持嵌套数据结构的处理
 */
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
