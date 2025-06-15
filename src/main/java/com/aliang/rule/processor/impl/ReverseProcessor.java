package com.aliang.rule.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;

/**
 * 字符串反转处理器
 * 将输入字符串反转
 */
public class ReverseProcessor implements ValueProcessor {
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public ReverseProcessor() {
        logger.logProcessorInit("ReverseProcessor", null);
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        try {
            String str = value.toString();
            String result = new StringBuilder(str).reverse().toString();
            logger.logProcessSuccess("ReverseProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("ReverseProcessor", value, e.getMessage());
            return value;
        }
    }
} 