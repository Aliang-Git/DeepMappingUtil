package com.aliang.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.processor.*;
import com.aliang.utils.*;

import java.math.*;
import java.util.*;

/**
 * 保留两位小数处理器
 */
public class RoundTwoDecimalProcessor implements ValueProcessor {
    private static final int DEFAULT_SCALE = 2;
    private final int scale;
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public RoundTwoDecimalProcessor() {
        this.scale = DEFAULT_SCALE;
        logger.logProcessorInit("RoundTwoDecimalProcessor", "使用默认精度: " + DEFAULT_SCALE);
    }

    public RoundTwoDecimalProcessor(String config) {
        int configScale;
        try {
            configScale = Integer.parseInt(config);
            if (configScale < 0) {
                logger.logInvalidConfig("RoundTwoDecimalProcessor", config, DEFAULT_SCALE);
                configScale = DEFAULT_SCALE;
            }
        } catch (NumberFormatException e) {
            logger.logInvalidConfig("RoundTwoDecimalProcessor", config, DEFAULT_SCALE);
            configScale = DEFAULT_SCALE;
        }
        this.scale = configScale;
        logger.logProcessorInit("RoundTwoDecimalProcessor", "精度: " + this.scale);
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
                logger.logProcessFailure("RoundTwoDecimalProcessor", value, "不支持的数据类型: " + value.getClass().getName());
                return value;
            }

            BigDecimal result = decimal.setScale(scale, RoundingMode.HALF_UP);
            logger.logProcessSuccess("RoundTwoDecimalProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("RoundTwoDecimalProcessor", value, e.getMessage());
            return value;
        }
    }
}
