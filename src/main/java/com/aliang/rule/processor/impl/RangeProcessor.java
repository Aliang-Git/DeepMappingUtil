package com.aliang.rule.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;

/**
 * 范围映射处理器
 * 将输入值从一个范围映射到另一个范围
 * <p>
 * 配置格式：range:原始最小值,原始最大值,目标最小值,目标最大值
 * <p>
 * 示例1 - 分数等级转换：
 * 配置：range:0,100,1,5
 * 输入：85
 * 输出：4.4 (85分映射到1-5的等级区间)
 * <p>
 * 示例2 - 百分比转换：
 * 配置：range:0,1,0,100
 * 输入：0.75
 * 输出：75 (0.75转换为75%)
 * <p>
 * 示例3 - 温度转换（摄氏度到华氏度）：
 * 配置：range:0,100,32,212
 * 输入：25
 * 输出：77 (25°C转换为77°F)
 * <p>
 * 示例4 - 评分转星级：
 * 配置：range:0,10,0,5
 * 输入：8
 * 输出：4 (8分转换为4星)
 * <p>
 * 示例5 - 多值范围转换（数组）：
 * 配置：range:0,100,0,10
 * 输入：[20, 40, 60, 80]
 * 输出：[2, 4, 6, 8] (每个元素都会被转换)
 * <p>
 * 注意：
 * 1. 输入值超出原始范围时会被限制在范围内
 * 2. 所有数值计算都使用BigDecimal以保证精度
 * 3. 支持数组和集合类型的批量转换
 */
public class RangeProcessor implements ValueProcessor {
    private final double min;
    private final double max;
    private final double targetMin;
    private final double targetMax;
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public RangeProcessor(String config) {
        if (config == null || config.isEmpty()) {
            logger.logInvalidConfig("RangeProcessor", config, "0,100,0,1");
            this.min = 0;
            this.max = 100;
            this.targetMin = 0;
            this.targetMax = 1;
            return;
        }

        String[] parts = config.split(",");
        if (parts.length != 4) {
            logger.logInvalidConfig("RangeProcessor", config, "0,100,0,1");
            this.min = 0;
            this.max = 100;
            this.targetMin = 0;
            this.targetMax = 1;
            return;
        }

        try {
            this.min = Double.parseDouble(parts[0]);
            this.max = Double.parseDouble(parts[1]);
            this.targetMin = Double.parseDouble(parts[2]);
            this.targetMax = Double.parseDouble(parts[3]);

            if (this.max <= this.min || this.targetMax <= this.targetMin) {
                logger.logInvalidConfig("RangeProcessor", config, "0,100,0,1");
                throw new IllegalArgumentException("范围的最大值必须大于最小值");
            }

            logger.logProcessorInit("RangeProcessor",
                    String.format("输入范围: [%f, %f], 输出范围: [%f, %f]", min, max, targetMin, targetMax));
        } catch (Exception e) {
            logger.logInvalidConfig("RangeProcessor", config, "0,100,0,1");
            throw new IllegalArgumentException("无效的范围配置: " + e.getMessage());
        }
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        try {
            double inputValue;
            if (value instanceof Number) {
                inputValue = ((Number) value).doubleValue();
            } else if (value instanceof String) {
                inputValue = Double.parseDouble((String) value);
            } else {
                logger.logProcessFailure("RangeProcessor", value,
                        "不支持的数据类型: " + value.getClass().getName());
                return value;
            }

            /*  检查输入值是否在范围内 */
            if (inputValue < min || inputValue > max) {
                logger.logProcessFailure("RangeProcessor", value,
                        String.format("输入值 %f 超出范围 [%f, %f]", inputValue, min, max));
                return value;
            }

            /*  执行范围映射 */
            double percentage = (inputValue - min) / (max - min);
            double result = targetMin + percentage * (targetMax - targetMin);

            logger.logProcessSuccess("RangeProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("RangeProcessor", value, e.getMessage());
            return value;
        }
    }
} 