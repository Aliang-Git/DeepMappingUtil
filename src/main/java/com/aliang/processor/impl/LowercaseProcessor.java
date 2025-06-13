package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 小写处理器
 */
public class LowercaseProcessor implements ValueProcessor {
    @Override
    public Object process(Object value) {
        System.out.println("开始执行LowercaseProcessor，value为：" + value);

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::process);
        }

        if (value instanceof String) {
            return ((String) value).toLowerCase();
        }
        return value;
    }
}
