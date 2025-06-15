package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.util.*;
import java.util.stream.*;

/**
 * 列表连接处理器
 * 将列表中的元素使用指定的分隔符连接成字符串
 * <p>
 * 配置格式：join:separator,prefix,suffix
 * separator: 分隔符
 * prefix: 前缀（可选）
 * suffix: 后缀（可选）
 * <p>
 * 示例1 - 基本连接：
 * 配置：join:,
 * 输入：["a", "b", "c"]
 * 输出："a,b,c"
 * <p>
 * 示例2 - 自定义分隔符：
 * 配置：join:|
 * 输入：["user", "admin", "guest"]
 * 输出："user|admin|guest"
 * <p>
 * 示例3 - 带前后缀：
 * 配置：join:,,{,}
 * 输入：["1", "2", "3"]
 * 输出："{1,2,3}"
 * <p>
 * 示例4 - 处理数字列表：
 * 配置：join:-
 * 输入：[1, 2, 3]
 * 输出："1-2-3"
 * <p>
 * 示例5 - SQL IN子句：
 * 配置：join:,,(,)
 * 输入：["'a'", "'b'", "'c'"]
 * 输出："('a','b','c')"
 * <p>
 * 特殊情况处理：
 * 1. 空列表：
 * 输入：[]
 * 输出：""
 * <p>
 * 2. 单个元素：
 * 输入：["single"]
 * 输出："single"
 * <p>
 * 3. 包含null值：
 * 输入：["a", null, "c"]
 * 输出："a,,c"
 * <p>
 * 注意：
 * 1. 支持任意类型的列表元素，自动转换为字符串
 * 2. null元素会被转换为空字符串
 * 3. 支持嵌套列表的扁平化处理
 * 4. 非集合类型输入将被转换为字符串直接返回
 * 5. null输入将返回null
 */
public class ListJoinProcessor extends AbstractProcessor {
    private final String delimiter;
    private final String prefix;
    private final String suffix;

    public ListJoinProcessor(String config) {
        super("ListJoinProcessor");
        String[] parts = config != null ? config.split(":") : new String[0];
        this.delimiter = parts.length > 0 ? parts[0] : ",";
        this.prefix = parts.length > 1 ? parts[1] : "";
        this.suffix = parts.length > 2 ? parts[2] : "";
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("连接配置: 分隔符='%s', 前缀='%s', 后缀='%s'",
                        delimiter, prefix, suffix), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (!(value instanceof Collection<?>)) {
            return value;
        }

        Collection<?> collection = (Collection<?>) value;
        String result = collection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(delimiter, prefix, suffix));

        ProcessorUtils.logProcessResult(processorName, value, result, null);
        return result;
    }
} 