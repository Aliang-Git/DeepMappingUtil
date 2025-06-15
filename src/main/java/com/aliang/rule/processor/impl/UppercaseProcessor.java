package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

/**
 * 大写转换处理器
 * 将输入字符串转换为大写
 */
public class UppercaseProcessor extends AbstractProcessor {
    public UppercaseProcessor() {
        super("UppercaseProcessor");
    }

    @Override
    protected Object processValue(Object value) {
        String result = value.toString().toUpperCase();
        ProcessorUtils.logProcessResult(processorName, value, result, null);
        return result;
    }
}