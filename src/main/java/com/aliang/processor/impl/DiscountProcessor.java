package com.aliang.processor.impl;

import com.aliang.processor.*;

/**
 * 折扣计算处理器
 */
public class DiscountProcessor implements ValueProcessor {
    private final double discountRate;

    public DiscountProcessor(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null; // 空值直接返回
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue() * discountRate;
        }
        try {
            // 尝试将字符串转为 double
            double number = Double.parseDouble(value.toString());
            return number * discountRate;
        } catch (NumberFormatException e) {
            // 无法转换为数字时返回原值，而不是抛异常
            return value;
        }
    }
}
