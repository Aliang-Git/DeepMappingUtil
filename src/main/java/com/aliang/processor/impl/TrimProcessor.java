package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import java.util.List;
import java.util.Map;

/**
 * 字符串修剪处理器
 * 去除字符串首尾的空白字符
 * 
 * 配置格式：trim
 * 不需要额外参数
 * 
 * 示例1 - 基本空格处理：
 * 配置：trim
 * 输入："  hello world  "
 * 输出："hello world"
 * 
 * 示例2 - 多种空白字符：
 * 配置：trim
 * 输入："\t\nuser name\r\n  "
 * 输出："user name"
 * 
 * 示例3 - 表单输入处理：
 * 配置：trim
 * 输入："   john@example.com   "
 * 输出："john@example.com"
 * 
 * 示例4 - 搜索关键词处理：
 * 配置：trim
 * 输入："  search term    "
 * 输出："search term"
 * 
 * 示例5 - 批量处理（数组）：
 * 配置：trim
 * 输入：["  a  ", "  b  ", "  c  "]
 * 输出：["a", "b", "c"]
 * 
 * 特殊情况处理：
 * 1. 空字符串：
 * 输入：""
 * 输出：""
 * 
 * 2. 全空白字符：
 * 输入："     "
 * 输出：""
 * 
 * 3. 中间有空格：
 * 输入："  hello   world  "
 * 输出："hello   world"
 * 
 * 注意：
 * 1. 处理所有类型的空白字符（空格、制表符、换行符等）
 * 2. 保留字符串中间的空白字符
 * 3. 支持数组和集合类型的批量处理
 * 4. 非字符串类型的输入将被转换为字符串后处理
 * 5. null值将被返回null
 */
public class TrimProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        return value instanceof String ? ((String) value).trim() : value;
    }
} 