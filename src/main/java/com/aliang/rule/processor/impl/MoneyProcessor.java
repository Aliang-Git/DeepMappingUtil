package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.math.*;
import java.text.*;

/**
 * 金额处理器
 * 将输入数值格式化为金额格式
 */
public class MoneyProcessor extends AbstractProcessor {
    private final DecimalFormat format;
    private final int scale;

    public MoneyProcessor(String config) {
        super("MoneyProcessor");
        String[] parts = config != null ? config.split(":") : new String[0];
        this.scale = parts.length > 0 ? Integer.parseInt(parts[0]) : 2;
        StringBuilder pattern = new StringBuilder("#,##0.");
        for (int i = 0; i < scale; i++) {
            pattern.append("0");
        }
        this.format = new DecimalFormat(pattern.toString());
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("金额格式: 小数位数=%d", scale), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (value == null) {
            return null;
        }

        try {
            BigDecimal number;
            if (value instanceof Number) {
                number = new BigDecimal(value.toString());
            } else if (value instanceof String) {
                number = new BigDecimal((String) value);
            } else {
                return value;
            }

            number = number.setScale(scale, RoundingMode.HALF_UP);
            String result = format.format(number);

            ProcessorUtils.logProcessResult(processorName, value, result, null);
            return result;
        } catch (NumberFormatException e) {
            ProcessorUtils.logProcessResult(processorName, value, value, e.getMessage());
            return value;
        }
    }
} 