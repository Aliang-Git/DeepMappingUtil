package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

/**
 * 整数转换处理器
 * 将输入值转换为整数
 */
public class ToIntegerProcessor extends AbstractProcessor {

    public ToIntegerProcessor(String config) {
        super("ToIntegerProcessor");
        ProcessorUtils.logProcessResult(processorName, null, "将输入值转换为整数", null);
    }

    @Override
    protected Object processValue(Object value) {
        if (value == null) {
            return null;
        }

        try {
            if (value instanceof Number) {
                int result = ((Number) value).intValue();
                ProcessorUtils.logProcessResult(processorName, value, result, null);
                return result;
            } else if (value instanceof String) {
                int result = Integer.parseInt((String) value);
                ProcessorUtils.logProcessResult(processorName, value, result, null);
                return result;
            }
        } catch (NumberFormatException e) {
            ProcessorUtils.logProcessResult(processorName, value, value, e.getMessage());
        }
        return value;
    }
} 