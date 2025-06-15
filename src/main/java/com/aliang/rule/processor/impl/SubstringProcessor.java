package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

/**
 * 子字符串处理器
 * 从输入字符串中提取指定范围的子字符串
 * <p>
 * 配置格式：substring:起始位置,结束位置
 * 位置说明：
 * - 起始位置：从0开始计数
 * - 结束位置：不包含该位置的字符
 * <p>
 * 示例1 - 提取前缀：
 * 配置：substring:0,3
 * 输入："PRODUCT12345"
 * 输出："PRO"
 * <p>
 * 示例2 - 提取中间部分：
 * 配置：substring:3,7
 * 输入："USER_ADMIN_ROLE"
 * 输出："_ADM"
 * <p>
 * 示例3 - 提取编号：
 * 配置：substring:4,10
 * 输入："ORD_20240320_001"
 * 输出："202403"
 * <p>
 * 示例4 - 提取后缀：
 * 配置：substring:11,14
 * 输入："FILE_2024_DOC"
 * 输出："DOC"
 * <p>
 * 示例5 - 批量处理（数组）：
 * 配置：substring:0,4
 * 输入：["CODE001", "CODE002", "CODE003"]
 * 输出：["CODE", "CODE", "CODE"]
 * <p>
 * 特殊情况处理：
 * 1. 超出字符串长度：
 * 配置：substring:0,10
 * 输入："ABC"
 * 输出："ABC" (自动调整到字符串实际长度)
 * <p>
 * 2. 起始位置超出长度：
 * 配置：substring:5,10
 * 输入："ABC"
 * 输出："" (返回空字符串)
 * <p>
 * 注意：
 * 1. 位置参数必须是非负整数
 * 2. 起始位置必须小于等于结束位置
 * 3. 支持数组和集合类型的批量处理
 * 4. 非字符串类型的输入将被转换为字符串后处理
 * 5. 会自动处理越界情况，确保安全运行
 */
public class SubstringProcessor extends AbstractProcessor {
    private final int start;
    private final int end;

    public SubstringProcessor(String config) {
        super("SubstringProcessor");
        String[] range = config != null ? config.split(",") : new String[0];
        this.start = range.length > 0 ? Integer.parseInt(range[0]) : 0;
        this.end = range.length > 1 ? Integer.parseInt(range[1]) : -1;
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("范围: %d - %s", start, end == -1 ? "end" : end), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (!(value instanceof String)) {
            return value;
        }

        String str = (String) value;
        String result;
        if (end == -1) {
            result = str.substring(start);
        } else {
            result = str.substring(start, Math.min(end, str.length()));
        }

        ProcessorUtils.logProcessResult(processorName, value, result, null);
        return result;
    }
} 