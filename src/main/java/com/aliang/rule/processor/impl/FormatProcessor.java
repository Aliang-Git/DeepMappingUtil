package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 格式化处理器
 * 使用String.format()对值进行格式化处理
 * <p>
 * 配置格式：format:格式化字符串
 * <p>
 * 示例1 - 金额格式化：
 * 配置：format:￥%.2f
 * 输入：99.9
 * 输出：￥99.90
 * <p>
 * 示例2 - 编号格式化：
 * 配置：format:ORDER-%06d
 * 输入：123
 * 输出：ORDER-000123
 * <p>
 * 示例3 - 文本格式化：
 * 配置：format:[%s]
 * 输入："test"
 * 输出：[test]
 * <p>
 * 示例4 - 百分比格式化：
 * 配置：format:%.1f%%
 * 输入：0.1234
 * 输出：12.3%
 * <p>
 * 示例5 - 多值格式化（数组）：
 * 配置：format:%s
 * 输入：["a", "b", "c"]
 * 输出：["a", "b", "c"] (每个元素都会被格式化)
 */
public class FormatProcessor implements ValueProcessor {
    private final String format;

    public FormatProcessor(String format) {
        this.format = format != null && !format.isEmpty() ? format : "%s";
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value == null) {
            return null;
        }

        Object param = value;
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal bd = (java.math.BigDecimal) value;
            if (format.contains("%d")) {
                param = bd.intValue();
            } else if (format.contains("%f")) {
                param = bd.doubleValue();
            }
        }
        try {
            return String.format(format, param);
        } catch (Exception e) {
            /*  回退为 value.toString() */
            return String.format(format, value);
        }
    }
} 