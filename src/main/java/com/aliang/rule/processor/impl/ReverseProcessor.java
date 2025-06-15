package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

/**
 * 字符串反转处理器
 * 将输入字符串反转
 */
public class ReverseProcessor extends AbstractProcessor {

    public ReverseProcessor(String config) {
        super("ReverseProcessor");
        ProcessorUtils.logProcessResult(processorName, null, "将字符串反转", null);
    }

    @Override
    protected Object processValue(Object value) {
        if (!(value instanceof String)) {
            return value;
        }

        String str = (String) value;
        String result = new StringBuilder(str).reverse().toString();

        ProcessorUtils.logProcessResult(processorName, value, result, null);
        return result;
    }
} 