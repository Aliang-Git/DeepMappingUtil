package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 文本首字母大写
 */
public class CapitalizeProcessor implements ValueProcessor {
    @Override
    public Object process(Object value) {
        System.out.println("开始执行CapitalizeProcessor，value为：" + value);

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::process);
        }

        String str = value.toString();
        if (str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
