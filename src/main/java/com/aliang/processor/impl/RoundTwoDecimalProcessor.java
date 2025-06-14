package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.*;
import java.util.*;

/**
 * 四舍五入处理器
 * 将数值四舍五入到指定的小数位数
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
