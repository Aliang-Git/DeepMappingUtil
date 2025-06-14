package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import java.util.List;
import java.util.Map;

/**
 * 大写转换处理器
 * 将字符串转换为大写形式
 * 
 * 配置格式：uppercase
 * 不需要额外参数
 * 
 * 示例1 - 基本转换：
 * 配置：uppercase
 * 输入："hello world"
 * 输出："HELLO WORLD"
 * 
 * 示例2 - 代码转换：
 * 配置：uppercase
 * 输入："get_user_info"
 * 输出："GET_USER_INFO"
 * 
 * 示例3 - 混合字符：
 * 配置：uppercase
 * 输入："User123_Admin"
 * 输出："USER123_ADMIN"
 * 
 * 示例4 - 特殊字符：
 * 配置：uppercase
 * 输入："test@example.com"
 * 输出："TEST@EXAMPLE.COM"
 * 
 * 示例5 - 批量处理（数组）：
 * 配置：uppercase
 * 输入：["red", "green", "blue"]
 * 输出：["RED", "GREEN", "BLUE"]
 * 
 * 特殊情况处理：
 * 1. 空字符串：
 * 输入：""
 * 输出：""
 * 
 * 2. 已是大写：
 * 输入："HELLO"
 * 输出："HELLO"
 * 
 * 3. 非字母字符：
 * 输入："123-456"
 * 输出："123-456"
 * 
 * 注意：
 * 1. 保持非字母字符不变
 * 2. 支持数组和集合类型的批量处理
 * 3. 非字符串类型的输入将被转换为字符串后处理
 * 4. null值将返回null
 * 5. 不会改变字符串的长度和空白字符
 */
public class UppercaseProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        return value instanceof String ? ((String) value).toUpperCase() : value;
    }
}