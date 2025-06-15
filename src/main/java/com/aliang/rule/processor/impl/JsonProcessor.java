package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;
import com.alibaba.fastjson.*;

/**
 * JSON处理器
 * 将输入值转换为JSON字符串
 */
public class JsonProcessor extends AbstractProcessor {
    public JsonProcessor() {
        super("JsonProcessor");
    }

    @Override
    protected Object processValue(Object value) {
        String result = JSON.toJSONString(value);
        ProcessorUtils.logProcessResult(processorName, value, result, null);
        return result;
    }
} 