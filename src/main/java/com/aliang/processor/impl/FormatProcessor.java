package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class FormatProcessor implements ValueProcessor {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof Number) {
            return DECIMAL_FORMAT.format(value);
        }
        return value;
    }
} 