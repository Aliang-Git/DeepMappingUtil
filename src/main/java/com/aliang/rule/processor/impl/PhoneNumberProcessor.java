package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.util.regex.*;

/**
 * 手机号码处理器
 * 对手机号码进行格式化或脱敏处理
 */
public class PhoneNumberProcessor extends AbstractProcessor {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private final boolean mask;
    private final String format;

    public PhoneNumberProcessor(String config) {
        super("PhoneNumberProcessor");
        String[] parts = config != null ? config.split(":") : new String[0];
        this.mask = parts.length > 0 && "mask".equalsIgnoreCase(parts[0]);
        this.format = parts.length > 1 ? parts[1] : "****";
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("处理模式: %s, 格式: %s", mask ? "脱敏" : "格式化", format), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (!(value instanceof String)) {
            return value;
        }

        String phone = (String) value;
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            ProcessorUtils.logProcessResult(processorName, value, value, "无效的手机号码格式");
            return value;
        }

        String result;
        if (mask) {
            result = phone.substring(0, 3) + format + phone.substring(7);
        } else {
            result = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7);
        }

        ProcessorUtils.logProcessResult(processorName, value, result, null);
        return result;
    }
} 