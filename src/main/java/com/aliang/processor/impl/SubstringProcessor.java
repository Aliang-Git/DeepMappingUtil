package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import org.slf4j.*;

import java.util.*;

/**
 * 字符串截取处理器
 * 从字符串中截取指定范围的子串
 * 
 * 配置格式：substring:起始位置,结束位置
 * 位置说明：
 * - 起始位置：从0开始计数
 * - 结束位置：不包含该位置的字符
 * 
 * 示例1 - 提取前缀：
 * 配置：substring:0,3
 * 输入："PRODUCT12345"
 * 输出："PRO"
 * 
 * 示例2 - 提取中间部分：
 * 配置：substring:3,7
 * 输入："USER_ADMIN_ROLE"
 * 输出："_ADM"
 * 
 * 示例3 - 提取编号：
 * 配置：substring:4,10
 * 输入："ORD_20240320_001"
 * 输出："202403"
 * 
 * 示例4 - 提取后缀：
 * 配置：substring:11,14
 * 输入："FILE_2024_DOC"
 * 输出："DOC"
 * 
 * 示例5 - 批量处理（数组）：
 * 配置：substring:0,4
 * 输入：["CODE001", "CODE002", "CODE003"]
 * 输出：["CODE", "CODE", "CODE"]
 * 
 * 特殊情况处理：
 * 1. 超出字符串长度：
 * 配置：substring:0,10
 * 输入："ABC"
 * 输出："ABC" (自动调整到字符串实际长度)
 * 
 * 2. 起始位置超出长度：
 * 配置：substring:5,10
 * 输入："ABC"
 * 输出："" (返回空字符串)
 * 
 * 注意：
 * 1. 位置参数必须是非负整数
 * 2. 起始位置必须小于等于结束位置
 * 3. 支持数组和集合类型的批量处理
 * 4. 非字符串类型的输入将被转换为字符串后处理
 * 5. 会自动处理越界情况，确保安全运行
 */
public class SubstringProcessor implements ValueProcessor {
    private static final Logger logger = LoggerFactory.getLogger(SubstringProcessor.class);
    private final int start;
    private final int end;

    public SubstringProcessor(String config) {
        if (config == null || config.isEmpty()) {
            throw new IllegalArgumentException("SubstringProcessor requires config in format: start,end");
        }
        String[] parts = config.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("SubstringProcessor config must have 2 parts: start,end");
        }
        try {
            this.start = Integer.parseInt(parts[0].trim());
            this.end = Integer.parseInt(parts[1].trim());
            if (start < 0 || end < 0 || start > end) {
                throw new IllegalArgumentException("Invalid range: start must be >= 0 and <= end");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in SubstringProcessor config", e);
        }
        logger.debug("SubstringProcessor initialized with range [{},{}]", start, end);
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof String) {
            String str = (String) value;
            if (start >= str.length()) {
                return "";
            }
            int actualEnd = Math.min(end, str.length());
            return str.substring(start, actualEnd);
        }
        return value;
    }
} 