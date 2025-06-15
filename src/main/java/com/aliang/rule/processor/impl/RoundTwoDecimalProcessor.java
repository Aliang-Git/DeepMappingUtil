package com.aliang.rule.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;

import java.math.*;

/**
 * 保留两位小数的处理器
 * 支持以下输入类型：
 * 1. 数字类型（Number）
 * 2. 字符串类型（包含数字和货币符号）
 */
public class RoundTwoDecimalProcessor implements ValueProcessor {
    private final int scale;
    private final ProcessorLogger logger;

    public RoundTwoDecimalProcessor() {
        this(2);
    }

    public RoundTwoDecimalProcessor(String config) {
        this.scale = parseScale(config);
        this.logger = new DefaultProcessorLogger();
        logger.logProcessorInit("RoundTwoDecimalProcessor", String.valueOf(scale));
    }

    private RoundTwoDecimalProcessor(int scale) {
        this.scale = scale;
        this.logger = new DefaultProcessorLogger();
        logger.logProcessorInit("RoundTwoDecimalProcessor", String.valueOf(scale));
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        try {
            BigDecimal number;
            if (value instanceof Number) {
                number = new BigDecimal(value.toString());
            } else if (value instanceof String) {
                // 移除货币符号和其他非数字字符（保留小数点和负号）
                String cleanValue = ((String) value).replaceAll("[^\\d.-]", "");
                number = new BigDecimal(cleanValue);
            } else {
                logger.logProcessFailure("RoundTwoDecimalProcessor", value, "不支持的数据类型");
                return value;
            }

            BigDecimal rounded = number.setScale(scale, RoundingMode.HALF_UP);
            logger.logProcessSuccess("RoundTwoDecimalProcessor", value, rounded);
            return rounded;
        } catch (Exception e) {
            logger.logProcessFailure("RoundTwoDecimalProcessor", value, e.getMessage());
            return value;
        }
    }

    private int parseScale(String config) {
        if (config == null || config.trim().isEmpty()) {
            return 2;
        }
        try {
            return Integer.parseInt(config.trim());
        } catch (NumberFormatException e) {
            logger.logProcessorParamError("RoundTwoDecimalProcessor", "无效的小数位数配置: " + config);
            return 2;
        }
    }
}
