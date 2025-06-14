package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.*;
import java.util.*;

/**
 * 小数位数处理器
 * 将数值四舍五入到指定小数位数
 * 
 * 配置格式：roundTwoDecimal[:小数位数]
 * 默认值：2位小数
 * 
 * 示例1 - 标准金额格式：
 * 配置：roundTwoDecimal
 * 输入：123.456
 * 输出：123.46
 * 
 * 示例2 - 自定义小数位：
 * 配置：roundTwoDecimal:3
 * 输入：123.4567
 * 输出：123.457
 * 
 * 示例3 - 价格处理：
 * 配置：roundTwoDecimal
 * 输入：99.999
 * 输出：100.00
 * 
 * 示例4 - 精确计算：
 * 配置：roundTwoDecimal:4
 * 输入：1.23456789
 * 输出：1.2346
 * 
 * 示例5 - 批量处理（数组）：
 * 配置：roundTwoDecimal
 * 输入：[123.456, 789.123, 456.789]
 * 输出：[123.46, 789.12, 456.79]
 * 
 * 特殊情况处理：
 * 1. 整数：
 * 输入：100
 * 输出：100.00
 * 
 * 2. 科学计数：
 * 输入：1.23E3
 * 输出：1230.00
 * 
 * 注意：
 * 1. 使用BigDecimal进行精确计算
 * 2. 采用ROUND_HALF_UP模式（四舍五入）
 * 3. 支持数组和集合类型的批量处理
 * 4. 非数值类型的输入将被忽略并返回原值
 * 5. 小数位数必须是非负整数
 */
public class RoundTwoDecimalProcessor implements ValueProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RoundTwoDecimalProcessor.class);
    private final int scale;
    private static final int DEFAULT_SCALE = 2;

    public RoundTwoDecimalProcessor(String config) {
        int tempScale = DEFAULT_SCALE;
        if (config != null && !config.isEmpty()) {
            try {
                tempScale = Integer.parseInt(config);
            } catch (NumberFormatException e) {
                logger.warn("Invalid scale config: {}, using default value {}", config, DEFAULT_SCALE);
            }
        }
        this.scale = tempScale;
        logger.debug("RoundTwoDecimalProcessor initialized with scale: {}", scale);
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof Number) {
            // 使用 BigDecimal 来保持精度
            BigDecimal bd = new BigDecimal(value.toString());
            return bd.setScale(scale, RoundingMode.HALF_UP);  // 直接返回 BigDecimal
        }
        return value;
    }
}
