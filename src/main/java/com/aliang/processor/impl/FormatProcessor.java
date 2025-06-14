package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import java.util.List;
import java.util.Map;

public class FormatProcessor implements ValueProcessor {
    private final String format;

    public FormatProcessor(String format) {
        this.format = format != null && !format.isEmpty() ? format : "%s";
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value == null) {
            return null;
        }
        return String.format(format, value);
    }
} 