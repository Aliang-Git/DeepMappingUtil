package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 前缀处理器
 * 在字符串前添加指定的前缀
 * 
 * 配置格式：prefix:前缀字符串
 * 
 * 示例1 - 用户名前缀：
 * 配置：prefix:USER_
 * 输入："admin"
 * 输出："USER_admin"
 * 
 * 示例2 - 编号前缀：
 * 配置：prefix:ORD-
 * 输入："20240320001"
 * 输出："ORD-20240320001"
 * 
 * 示例3 - 文件名前缀：
 * 配置：prefix:backup_
 * 输入："data.json"
 * 输出："backup_data.json"
 * 
 * 示例4 - 多语言前缀：
 * 配置：prefix:zh_CN_
 * 输入："message"
 * 输出："zh_CN_message"
 * 
 * 示例5 - 批量处理（数组）：
 * 配置：prefix:SKU_
 * 输入：["001", "002", "003"]
 * 输出：["SKU_001", "SKU_002", "SKU_003"]
 * 
 * 特殊情况处理：
 * 1. 空字符串：
 * 配置：prefix:TEST_
 * 输入：""
 * 输出："TEST_"
 * 
 * 2. null值：
 * 配置：prefix:LOG_
 * 输入：null
 * 输出：null
 * 
 * 3. 数字值：
 * 配置：prefix:NO_
 * 输入：123
 * 输出："NO_123"
 * 
 * 注意：
 * 1. 前缀不能为null，但可以是空字符串
 * 2. 支持任意字符串作为前缀，包括特殊字符
 * 3. 支持数组和集合类型的批量处理
 * 4. 非字符串类型的输入将被转换为字符串后处理
 * 5. 如果输入为null，则直接返回null
 */
public class PrefixProcessor implements ValueProcessor {
    private final String prefix;

    public PrefixProcessor(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        return prefix + value.toString();
    }
}
