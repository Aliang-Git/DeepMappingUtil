package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

/**
 * 替换处理器
 * 将输入值中的指定字符串替换为新的字符串
 * 支持普通字符串替换和正则表达式替换
 */
public class ReplaceProcessor extends AbstractProcessor {
    private final String target;
    private final String replacement;
    private final boolean isRegex;

    public ReplaceProcessor(String config) {
        super("ReplaceProcessor");
        String[] parts = config != null ? config.split("->") : new String[]{"", ""};
        this.target = parts[0].trim();
        this.replacement = parts.length > 1 ? parts[1].trim() : "";
        this.isRegex = parts.length > 2 && "regex".equalsIgnoreCase(parts[2].trim());

        ProcessorUtils.logProcessResult(processorName, null,
                String.format("替换规则: '%s' -> '%s' (%s)",
                        target, replacement, isRegex ? "正则表达式" : "普通字符串"), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (value == null) {
            return null;
        }

        String strValue = value.toString();
        if (target.isEmpty()) {
            ProcessorUtils.logProcessResult(processorName, value, value, "替换目标为空");
            return value;
        }

        try {
            String result;
            if (isRegex) {
                result = strValue.replaceAll(target, replacement);
            } else {
                result = strValue.replace(target, replacement);
            }
            ProcessorUtils.logProcessResult(processorName, value, result, null);
            return result;
        } catch (Exception e) {
            ProcessorUtils.logProcessResult(processorName, value, value, e.getMessage());
            return value;
        }
    }
} 