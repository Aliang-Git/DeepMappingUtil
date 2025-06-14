package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.math.*;
import java.util.*;

/**
 * 四舍五入处理器
 * 将数值四舍五入到指定的小数位数
 */
public class RoundTwoDecimalProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof Number) {
            // 使用 BigDecimal 来保持精度
            BigDecimal bd = new BigDecimal(value.toString());
            return bd.setScale(2, RoundingMode.HALF_UP);  // 直接返回 BigDecimal
        }
        return value;
    }
}
