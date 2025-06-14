package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.math.*;
import java.util.*;

/**
 * 四舍五入处理器
 */
public class RoundTwoDecimalProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof Number) {
            BigDecimal bd = BigDecimal.valueOf(((Number) value).doubleValue());
            return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return value;
    }
}
