package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import org.slf4j.*;

import java.util.*;

public class ReplaceProcessor implements ValueProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ReplaceProcessor.class);
    private final String target;
    private final String replacement;

    public ReplaceProcessor(String config) {
        if (config == null || config.isEmpty()) {
            throw new IllegalArgumentException("ReplaceProcessor requires config in format: target,replacement");
        }
        String[] parts = config.split(",", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("ReplaceProcessor config must have 2 parts: target,replacement");
        }
        this.target = parts[0].trim();
        this.replacement = parts[1].trim();
        logger.debug("ReplaceProcessor initialized with target '{}' and replacement '{}'", target, replacement);
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof String) {
            return ((String) value).replace(target, replacement);
        }
        return value;
    }
} 