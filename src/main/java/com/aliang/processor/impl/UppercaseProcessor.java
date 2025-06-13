package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 将字符串转为大写
 */
public class UppercaseProcessor implements ValueProcessor {
    @Override
    public Object process(Object value) {
        System.out.println("开始执行UppercaseProcessor，value为：" + value);

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::process);
        }

        return value instanceof String ? ((String) value).toUpperCase() : value;
    }
}