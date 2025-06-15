package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

/**
 * 首字母大写处理器
 * 将输入字符串的首字母转换为大写，其余字母转换为小写
 */
public class CapitalizeProcessor extends AbstractProcessor {
    public CapitalizeProcessor() {
        super("CapitalizeProcessor");
    }

    @Override
    protected Object processValue(Object value) {
        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty()) {
                return str;
            }
            String result = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
            ProcessorUtils.logProcessResult(processorName, value, result, null);
            return result;
        }
        return value;
    }
}
