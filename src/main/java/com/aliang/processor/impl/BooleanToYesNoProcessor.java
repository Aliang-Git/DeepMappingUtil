package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 是/否转换
 */
public class BooleanToYesNoProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? "是" : "否";
        }
        return value;
    }
}
