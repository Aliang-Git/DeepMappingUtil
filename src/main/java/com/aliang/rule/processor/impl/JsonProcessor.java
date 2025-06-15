package com.aliang.rule.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;
import com.alibaba.fastjson.*;

/**
 * JSON处理器
 * 将输入值转换为JSON字符串
 */
public class JsonProcessor implements ValueProcessor {
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public JsonProcessor() {
        logger.logProcessorInit("JsonProcessor", null);
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        try {
            String result = JSON.toJSONString(value);
            logger.logProcessSuccess("JsonProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("JsonProcessor", value, e.getMessage());
            return value;
        }
    }
} 