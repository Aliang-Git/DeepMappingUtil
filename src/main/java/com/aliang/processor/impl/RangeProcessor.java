package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import org.slf4j.*;

import java.math.*;
import java.util.*;

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