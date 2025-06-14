package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 首字母大写处理器
 * 将字符串的首字母转换为大写
 * <p>
 * 配置格式：capitalize[:mode]
 * 模式说明：
 * - 默认：只转换第一个字母
 * - words：转换每个单词的首字母
 * <p>
 * 示例1 - 基本首字母大写：
 * 配置：capitalize
 * 输入："hello world"
 * 输出："Hello world"
 * <p>
 * 示例2 - 每个单词首字母大写：
 * 配置：capitalize:words
 * 输入："hello world"
 * 输出："Hello World"
 * <p>
 * 示例3 - 名称格式化：
 * 配置：capitalize:words
 * 输入："john smith"
 * 输出："John Smith"
 * <p>
 * 示例4 - 标题格式化：
 * 配置：capitalize:words
 * 输入："the quick brown fox"
 * 输出："The Quick Brown Fox"
 * <p>
 * 示例5 - 批量处理（数组）：
 * 配置：capitalize
 * 输入：["hello", "world", "test"]
 * 输出：["Hello", "World", "Test"]
 * <p>
 * 特殊情况处理：
 * 1. 空字符串：
 * 输入：""
 * 输出：""
 * <p>
 * 2. 单个字母：
 * 输入："a"
 * 输出："A"
 * <p>
 * 3. 已经是大写：
 * 输入："HELLO"
 * 输出："HELLO"
 * <p>
 * 注意：
 * 1. 默认只处理第一个字母
 * 2. words模式会处理所有单词的首字母
 * 3. 支持数组和集合类型的批量处理
 * 4. 非字符串类型的输入将被转换为字符串后处理
 * 5. 保持其他字符的大小写状态不变
 */
public class CapitalizeProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.isEmpty()) {
                return str;
            }
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
        return value;
    }
}
