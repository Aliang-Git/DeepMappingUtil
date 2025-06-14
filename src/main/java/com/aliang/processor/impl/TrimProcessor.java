package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import java.util.List;
import java.util.Map;

public class TrimProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        return value instanceof String ? ((String) value).trim() : value;
    }
} 