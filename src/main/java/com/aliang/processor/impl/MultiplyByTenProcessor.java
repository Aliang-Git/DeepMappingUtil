package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;
import org.slf4j.*;

import java.math.*;
import java.util.*;

/**
 * 数据计算
 * 例如：数字乘以10
 */
public class MultiplyByTenProcessor implements ValueProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MultiplyByTenProcessor.class);
    private final BigDecimal multiplier;

    public MultiplyByTenProcessor(String config) {
        BigDecimal tempMultiplier = BigDecimal.TEN;
        if (config != null && !config.isEmpty()) {
            try {
                tempMultiplier = new BigDecimal(config);
            } catch (NumberFormatException e) {
                logger.warn("Invalid multiplier config: {}, using default value 10", config);
            }
        }
        this.multiplier = tempMultiplier;
        logger.debug("MultiplyByTenProcessor initialized with multiplier: {}", multiplier);
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof Number) {
            BigDecimal bd = new BigDecimal(value.toString());
            return bd.multiply(multiplier);
        }
        return value;
    }
}
