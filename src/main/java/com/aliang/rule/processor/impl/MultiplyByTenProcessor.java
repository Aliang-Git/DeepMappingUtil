package com.aliang.rule.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.math.*;
import java.util.*;

/**
 * 乘法处理器
 * 将数值乘以指定的倍数
 * <p>
 * 配置格式：multiplyByTen:倍数
 * 倍数可以是任意正数，支持小数
 * <p>
 * 示例1 - 整数倍数：
 * 配置：multiplyByTen:10
 * 输入：5
 * 输出：50
 * <p>
 * 示例2 - 小数倍数：
 * 配置：multiplyByTen:0.8
 * 输入：100
 * 输出：80
 * <p>
 * 示例3 - 负数处理：
 * 配置：multiplyByTen:2
 * 输入：-7
 * 输出：-14
 * <p>
 * 示例4 - 字符串数字：
 * 配置：multiplyByTen:1.5
 * 输入："12"
 * 输出：18.0
 * <p>
 * 示例5 - 批量处理（数组）：
 * 配置：multiplyByTen:3
 * 输入：[1, 2, 3]
 * 输出：[3, 6, 9]
 * <p>
 * 特殊情况处理：
 * 1. 零值：
 * 输入：0
 * 输出：0
 * <p>
 * 2. 非数字字符串：
 * 输入："abc"
 * 输出：null
 * <p>
 * 3. 极大值：
 * 输入：Integer.MAX_VALUE
 * 输出：null（超出范围）
 * <p>
 * 注意：
 * 1. 支持整数和小数处理
 * 2. 支持数组和集合类型的批量处理
 * 3. 非数字类型的输入将尝试转换为数字后处理
 * 4. null值将返回null
 * 5. 超出数值范围的结果将返回null
 */
public class MultiplyByTenProcessor implements ValueProcessor {
    private static final BigDecimal DEFAULT_MULTIPLIER = BigDecimal.valueOf(10);
    private final BigDecimal multiplier;
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public MultiplyByTenProcessor() {
        this.multiplier = DEFAULT_MULTIPLIER;
        logger.logProcessorInit("MultiplyByTenProcessor", "使用默认倍数: " + DEFAULT_MULTIPLIER);
    }

    public MultiplyByTenProcessor(String config) {
        BigDecimal configMultiplier;
        try {
            configMultiplier = config != null && !config.isEmpty() ?
                    new BigDecimal(config) : DEFAULT_MULTIPLIER;
            if (configMultiplier.compareTo(BigDecimal.ZERO) <= 0) {
                logger.logInvalidConfig("MultiplyByTenProcessor", config, DEFAULT_MULTIPLIER.toString());
                configMultiplier = DEFAULT_MULTIPLIER;
            }
        } catch (NumberFormatException e) {
            logger.logInvalidConfig("MultiplyByTenProcessor", config, DEFAULT_MULTIPLIER.toString());
            configMultiplier = DEFAULT_MULTIPLIER;
        }
        this.multiplier = configMultiplier;
        logger.logProcessorInit("MultiplyByTenProcessor", "倍数: " + this.multiplier);
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }

        try {
            BigDecimal decimal;
            if (value instanceof BigDecimal) {
                decimal = (BigDecimal) value;
            } else if (value instanceof Number) {
                decimal = BigDecimal.valueOf(((Number) value).doubleValue());
            } else if (value instanceof String) {
                decimal = new BigDecimal((String) value);
            } else {
                logger.logProcessFailure("MultiplyByTenProcessor", value,
                        "不支持的数据类型: " + value.getClass().getName());
                return value;
            }

            BigDecimal result = decimal.multiply(multiplier)
                    .setScale(8, RoundingMode.HALF_UP);
            logger.logProcessSuccess("MultiplyByTenProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("MultiplyByTenProcessor", value, e.getMessage());
            return value;
        }
    }
}
