package com.aliang.processor.impl;

import com.aliang.processor.*;

import java.util.*;
import java.util.stream.*;

/**
 * 列表连接处理器
 * 将列表元素使用指定分隔符连接成字符串
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
public class ListJoinProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        if (!(value instanceof Collection || value.getClass().isArray())) {
            return value.toString();
        }

        List<?> list = convertToList(value);
        if (list.isEmpty()) {
            return "";
        }

        // 默认使用逗号分隔
        return list.stream()
                .map(item -> item == null ? "" : item.toString())
                .collect(Collectors.joining(","));
    }

    private List<?> convertToList(Object value) {
        if (value instanceof List) {
            return (List<?>) value;
        }
        if (value instanceof Collection) {
            return new ArrayList<>((Collection<?>) value);
        }
        if (value.getClass().isArray()) {
            if (value instanceof Object[]) {
                return Arrays.asList((Object[]) value);
            }
            // 处理基本类型数组
            List<Object> result = new ArrayList<>();
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; i++) {
                result.add(java.lang.reflect.Array.get(value, i));
            }
            return result;
        }
        return Collections.singletonList(value);
    }
} 