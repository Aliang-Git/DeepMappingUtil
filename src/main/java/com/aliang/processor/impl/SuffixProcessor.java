package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 加后缀
 */
public class SuffixProcessor implements ValueProcessor {
    private final String suffix;

    public SuffixProcessor(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        return value.toString() + suffix;
    }
} 