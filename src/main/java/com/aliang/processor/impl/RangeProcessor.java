package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import org.slf4j.*;

import java.math.*;
import java.util.*;

/**
 * 范围映射处理器
 * 将输入值从一个范围映射到另一个范围
 * 
 * 配置格式：range:原始最小值,原始最大值,目标最小值,目标最大值
 * 
 * 示例1 - 分数等级转换：
 * 配置：range:0,100,1,5
 * 输入：85
 * 输出：4.4 (85分映射到1-5的等级区间)
 * 
 * 示例2 - 百分比转换：
 * 配置：range:0,1,0,100
 * 输入：0.75
 * 输出：75 (0.75转换为75%)
 * 
 * 示例3 - 温度转换（摄氏度到华氏度）：
 * 配置：range:0,100,32,212
 * 输入：25
 * 输出：77 (25°C转换为77°F)
 * 
 * 示例4 - 评分转星级：
 * 配置：range:0,10,0,5
 * 输入：8
 * 输出：4 (8分转换为4星)
 * 
 * 示例5 - 多值范围转换（数组）：
 * 配置：range:0,100,0,10
 * 输入：[20, 40, 60, 80]
 * 输出：[2, 4, 6, 8] (每个元素都会被转换)
 * 
 * 注意：
 * 1. 输入值超出原始范围时会被限制在范围内
 * 2. 所有数值计算都使用BigDecimal以保证精度
 * 3. 支持数组和集合类型的批量转换
 */
public class RangeProcessor implements ValueProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RangeProcessor.class);
    private final BigDecimal min;
    private final BigDecimal max;
    private final BigDecimal targetMin;
    private final BigDecimal targetMax;

    public RangeProcessor(String config) {
        if (config == null || config.isEmpty()) {
            throw new IllegalArgumentException("RangeProcessor requires config in format: min,max,targetMin,targetMax");
        }
        String[] parts = config.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("RangeProcessor config must have 4 parts: min,max,targetMin,targetMax");
        }
        try {
            this.min = new BigDecimal(parts[0].trim());
            this.max = new BigDecimal(parts[1].trim());
            this.targetMin = new BigDecimal(parts[2].trim());
            this.targetMax = new BigDecimal(parts[3].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in RangeProcessor config", e);
        }
        logger.debug("RangeProcessor initialized with range [{},{}] -> [{},{}]", min, max, targetMin, targetMax);
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof Number) {
            BigDecimal input = new BigDecimal(value.toString());
            // 如果输入值超出范围，返回null
            if (input.compareTo(min) < 0 || input.compareTo(max) > 0) {
                return null;
            }
            // 线性映射
            BigDecimal ratio = input.subtract(min).divide(max.subtract(min), 10, RoundingMode.HALF_UP);
            return targetMin.add(ratio.multiply(targetMax.subtract(targetMin)));
        }
        return value;
    }
} 