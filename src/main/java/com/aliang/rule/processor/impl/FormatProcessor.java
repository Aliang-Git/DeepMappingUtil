package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

/**
 * 格式化处理器
 * 将输入值按照指定的格式进行格式化
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
 * <p>
 * 示例6 - 多参数格式化：
 * 配置：format:%s-%s
 * 输入："123 main st"
 * 输出："123 main st-123 main st" (重复输入值)
 */
public class FormatProcessor extends AbstractProcessor {
    private final String format;

    public FormatProcessor(String config) {
        super("FormatProcessor");
        this.format = config != null ? config : "%s";
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("格式化模式: '%s'", format), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (value == null) {
            return null;
        }

        try {
            String result;
            if (value instanceof Number) {
                // 对于数字类型，使用String.format
                result = String.format(format, value);
            } else if (value instanceof String) {
                // 对于字符串类型，计算需要的参数数量
                int paramCount = countFormatSpecifiers(format);
                if (paramCount > 1) {
                    // 如果需要多个参数，重复输入值
                    Object[] params = new Object[paramCount];
                    for (int i = 0; i < paramCount; i++) {
                        params[i] = value;
                    }
                    result = String.format(format, params);
                } else {
                    // 单个参数的情况
                    result = String.format(format, value);
                }
            } else {
                // 对于其他类型，先转换为字符串再格式化
                result = String.format(format, value.toString());
            }

            ProcessorUtils.logProcessResult(processorName, value, result, null);
            return result;
        } catch (Exception e) {
            ProcessorUtils.logProcessResult(processorName, value, value, e.getMessage());
            return value;
        }
    }

    /**
     * 计算格式化字符串中需要的参数数量
     */
    private int countFormatSpecifiers(String format) {
        int count = 0;
        int i = 0;
        while (i < format.length()) {
            if (format.charAt(i) == '%') {
                if (i + 1 < format.length() && format.charAt(i + 1) == '%') {
                    // 跳过转义的%
                    i += 2;
                } else {
                    // 找到一个格式化说明符
                    count++;
                    i++;
                }
            } else {
                i++;
            }
        }
        return count;
    }
} 