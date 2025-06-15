package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.math.*;

/**
 * 四舍五入处理器
 * 将数值四舍五入到指定小数位数
 */
public class RoundTwoDecimalProcessor extends AbstractProcessor {
    private final int scale;

    public RoundTwoDecimalProcessor(String config) {
        super("RoundTwoDecimalProcessor");
        this.scale = config != null ? Integer.parseInt(config.trim()) : 2;
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("小数位数: %d", scale), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (value == null) {
            return null;
        }

        try {
            BigDecimal number = new BigDecimal(value.toString());
            BigDecimal result = number.setScale(scale, RoundingMode.HALF_UP);
            ProcessorUtils.logProcessResult(processorName, value, result, null);
            return result;
        } catch (NumberFormatException e) {
            ProcessorUtils.logProcessResult(processorName, value, value, e.getMessage());
            return value;
        }
    }
}
