package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 加前缀
 */
public class PrefixProcessor implements ValueProcessor {
    private final String prefix;

    public PrefixProcessor(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Object process(Object value) {
        System.out.println("开始执行PrefixProcessor，value为：" + value);

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::process);
        }

        return prefix + value.toString();
    }
}
