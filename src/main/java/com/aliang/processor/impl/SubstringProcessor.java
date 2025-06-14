package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import org.slf4j.*;

import java.util.*;

public class SubstringProcessor implements ValueProcessor {
    private static final Logger logger = LoggerFactory.getLogger(SubstringProcessor.class);
    private final int start;
    private final int end;

    public SubstringProcessor(String config) {
        if (config == null || config.isEmpty()) {
            throw new IllegalArgumentException("SubstringProcessor requires config in format: start,end");
        }
        String[] parts = config.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("SubstringProcessor config must have 2 parts: start,end");
        }
        try {
            this.start = Integer.parseInt(parts[0].trim());
            this.end = Integer.parseInt(parts[1].trim());
            if (start < 0 || end < 0 || start > end) {
                throw new IllegalArgumentException("Invalid range: start must be >= 0 and <= end");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in SubstringProcessor config", e);
        }
        logger.debug("SubstringProcessor initialized with range [{},{}]", start, end);
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof String) {
            String str = (String) value;
            if (start >= str.length()) {
                return "";
            }
            int actualEnd = Math.min(end, str.length());
            return str.substring(start, actualEnd);
        }
        return value;
    }
} 