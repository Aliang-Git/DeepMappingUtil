package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.math.*;

/**
 * 乘以10处理器
 * 将输入数值乘以10
 */
public class MultiplyByTenProcessor extends AbstractProcessor {

    public MultiplyByTenProcessor(String config) {
        super("MultiplyByTenProcessor");
        ProcessorUtils.logProcessResult(processorName, null, "将输入值乘以10", null);
    }

    @Override
    protected Object processValue(Object value) {
        if (value == null) {
            return null;
        }

        try {
            BigDecimal number;
            if (value instanceof Number) {
                number = new BigDecimal(value.toString());
            } else if (value instanceof String) {
                number = new BigDecimal((String) value);
            } else {
                return value;
            }

            BigDecimal result = number.multiply(BigDecimal.TEN);
            ProcessorUtils.logProcessResult(processorName, value, result, null);
            return result;
        } catch (NumberFormatException e) {
            ProcessorUtils.logProcessResult(processorName, value, value, e.getMessage());
            return value;
        }
    }
}
