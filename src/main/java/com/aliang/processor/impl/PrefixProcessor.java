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
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        return prefix + value.toString();
    }
}
