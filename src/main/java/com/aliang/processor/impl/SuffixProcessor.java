package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 后缀处理器
 * 在字符串后添加指定的后缀
 * <p>
 * 配置格式：suffix:后缀字符串
 * <p>
 * 示例1 - 文件扩展名：
 * 配置：suffix:.txt
 * 输入："document"
 * 输出："document.txt"
 * <p>
 * 示例2 - 单位后缀：
 * 配置：suffix:元
 * 输入："100"
 * 输出："100元"
 * <p>
 * 示例3 - 时间后缀：
 * 配置：suffix: GMT+8
 * 输入："2024-03-20 14:30:00"
 * 输出："2024-03-20 14:30:00 GMT+8"
 * <p>
 * 示例4 - 版本号：
 * 配置：suffix:_v1.0
 * 输入："config"
 * 输出："config_v1.0"
 * <p>
 * 示例5 - 批量处理（数组）：
 * 配置：suffix:kg
 * 输入：["10", "20", "30"]
 * 输出：["10kg", "20kg", "30kg"]
 * <p>
 * 特殊情况处理：
 * 1. 空字符串：
 * 配置：suffix:_END
 * 输入：""
 * 输出："_END"
 * <p>
 * 2. null值：
 * 配置：suffix:_TAIL
 * 输入：null
 * 输出：null
 * <p>
 * 3. 数字值：
 * 配置：suffix:%
 * 输入：75
 * 输出："75%"
 * <p>
 * 注意：
 * 1. 后缀不能为null，但可以是空字符串
 * 2. 支持任意字符串作为后缀，包括特殊字符
 * 3. 支持数组和集合类型的批量处理
 * 4. 非字符串类型的输入将被转换为字符串后处理
 * 5. 如果输入为null，则直接返回null
 */
public class SuffixProcessor implements ValueProcessor {
    private final String suffix;

    public SuffixProcessor(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        return value.toString() + suffix;
    }
} 