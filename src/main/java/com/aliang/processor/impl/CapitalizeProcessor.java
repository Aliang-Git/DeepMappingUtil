package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 文本首字母大写
 */
public class CapitalizeProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty()) {
                return str;
            }
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
        return value;
    }
}
