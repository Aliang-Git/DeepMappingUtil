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
    public Object process(Object value) {
        System.out.println("开始执行RoundTwoDecimalProcessor，value为：" + value);

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::process);
        }

        if (value instanceof Number) {
            BigDecimal bd = new BigDecimal(((Number) value).doubleValue());
            return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return value;
    }
}
