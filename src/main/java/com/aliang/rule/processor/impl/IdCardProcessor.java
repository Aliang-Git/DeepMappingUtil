package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.util.regex.*;

/**
 * 身份证号码处理器
 * 对身份证号码进行格式化或脱敏处理
 */
public class IdCardProcessor extends AbstractProcessor {
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$");
    private final boolean mask;

    public IdCardProcessor(String config) {
        super("IdCardProcessor");
        this.mask = config != null && "mask".equalsIgnoreCase(config);
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("处理模式: %s", mask ? "脱敏" : "格式化"), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (!(value instanceof String)) {
            return value;
        }

        String idCard = (String) value;
        if (!ID_CARD_PATTERN.matcher(idCard).matches()) {
            ProcessorUtils.logProcessResult(processorName, value, value, "无效的身份证号码格式");
            return value;
        }

        String result;
        if (mask) {
            result = idCard.substring(0, 6) + "********" + idCard.substring(14);
        } else {
            result = idCard.substring(0, 6) + " " + idCard.substring(6, 14) + " " + idCard.substring(14);
        }

        ProcessorUtils.logProcessResult(processorName, value, result, null);
        return result;
    }
} 