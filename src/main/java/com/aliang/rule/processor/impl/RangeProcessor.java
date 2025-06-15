package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.math.*;

/**
 * 范围处理器
 * 将输入值限制在指定的范围内
 */
public class RangeProcessor extends AbstractProcessor {
    private final BigDecimal min;
    private final BigDecimal max;

    public RangeProcessor(String config) {
        super("RangeProcessor");
        String[] range = config != null ? config.split(",") : new String[0];
        this.min = range.length > 0 ? new BigDecimal(range[0]) : null;
        this.max = range.length > 1 ? new BigDecimal(range[1]) : null;
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("范围: %s - %s", min, max), null);
    }

    @Override
    protected Object processValue(Object value) {
        BigDecimal number = ProcessorUtils.safeParseNumber(value);
        if (number == null) {
            return value;
        }

        if (min != null && number.compareTo(min) < 0) {
            number = min;
        }
        if (max != null && number.compareTo(max) > 0) {
            number = max;
        }

        ProcessorUtils.logProcessResult(processorName, value, number, null);
        return number;
    }
} 