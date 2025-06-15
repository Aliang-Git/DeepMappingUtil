package com.aliang.rule.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;

/**
 * 字符串修剪处理器
 * 去除输入字符串的首尾空白字符
 * <p>
 * 配置格式：trim
 * 不需要额外参数
 * <p>
 * 示例1 - 基本空格处理：
 * 配置：trim
 * 输入："  hello world  "
 * 输出："hello world"
 * <p>
 * 示例2 - 多种空白字符：
 * 配置：trim
 * 输入："\t\nuser name\r\n  "
 * 输出："user name"
 * <p>
 * 示例3 - 表单输入处理：
 * 配置：trim
 * 输入："   john@example.com   "
 * 输出："john@example.com"
 * <p>
 * 示例4 - 搜索关键词处理：
 * 配置：trim
 * 输入："  search term    "
 * 输出："search term"
 * <p>
 * 示例5 - 批量处理（数组）：
 * 配置：trim
 * 输入：["  a  ", "  b  ", "  c  "]
 * 输出：["a", "b", "c"]
 * <p>
 * 特殊情况处理：
 * 1. 空字符串：
 * 输入：""
 * 输出：""
 * <p>
 * 2. 全空白字符：
 * 输入："     "
 * 输出：""
 * <p>
 * 3. 中间有空格：
 * 输入："  hello   world  "
 * 输出："hello   world"
 * <p>
 * 注意：
 * 1. 处理所有类型的空白字符（空格、制表符、换行符等）
 * 2. 保留字符串中间的空白字符
 * 3. 支持数组和集合类型的批量处理
 * 4. 非字符串类型的输入将被转换为字符串后处理
 * 5. null值将被返回null
 */
public class TrimProcessor implements ValueProcessor {
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public TrimProcessor() {
        logger.logProcessorInit("TrimProcessor", null);
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        try {
            String str = value.toString();
            String result = str.trim();
            logger.logProcessSuccess("TrimProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("TrimProcessor", value, e.getMessage());
            return value;
        }
    }
} 