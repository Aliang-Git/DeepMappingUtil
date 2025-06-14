package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 是/否转换
 */
public class BooleanToYesNoProcessor implements ValueProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BooleanToYesNoProcessor.class);
    private final Map<Boolean, String> booleanMap;

    public BooleanToYesNoProcessor(String config) {
        this.booleanMap = new HashMap<>();
        if (config != null && !config.isEmpty()) {
            String[] mappings = config.split(";");
            for (String mapping : mappings) {
                String[] parts = mapping.split("=");
                if (parts.length == 2) {
                    booleanMap.put(Boolean.parseBoolean(parts[0].trim()), parts[1].trim());
                }
            }
        }
        // 设置默认值
        if (booleanMap.isEmpty()) {
            booleanMap.put(true, "是");
            booleanMap.put(false, "否");
        }
        logger.debug("BooleanToYesNoProcessor initialized with mappings: {}", booleanMap);
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof Boolean) {
            return booleanMap.getOrDefault(value, value.toString());
        }
        return value;
    }
}
