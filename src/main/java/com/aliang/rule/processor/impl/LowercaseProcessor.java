package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

/**
 * 小写转换处理器
 * 将输入字符串转换为小写
 */
public class LowercaseProcessor extends AbstractProcessor {
    public LowercaseProcessor() {
        super("LowercaseProcessor");
    }

    @Override
    protected Object processValue(Object value) {
        String result = value.toString().toLowerCase();
        ProcessorUtils.logProcessResult(processorName, value, result, null);
        return result;
    }
}
