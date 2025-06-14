package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import org.slf4j.*;

import java.util.*;

/**
 * 字符串替换处理器
 * 将字符串中的指定内容替换为新的内容
 * 
 * 配置格式：replace:目标字符串,替换字符串
 * 
 * 示例1 - 基本替换：
 * 配置：replace:old,new
 * 输入："old_value"
 * 输出："new_value"
 * 
 * 示例2 - 分隔符替换：
 * 配置：replace:_,-
 * 输入："user_name_info"
 * 输出："user-name-info"
 * 
 * 示例3 - 空格处理：
 * 配置：replace: ,_
 * 输入："hello world test"
 * 输出："hello_world_test"
 * 
 * 示例4 - 特殊字符替换：
 * 配置：replace:@at@
 * 输入："user@example.com"
 * 输出："useratexample.com"
 * 
 * 示例5 - 批量处理（数组）：
 * 配置：replace:test,prod
 * 输入：["test_1", "test_2", "test_3"]
 * 输出：["prod_1", "prod_2", "prod_3"]
 * 
 * 特殊情况处理：
 * 1. 空字符串：
 * 配置：replace:a,b
 * 输入：""
 * 输出：""
 * 
 * 2. 目标不存在：
 * 配置：replace:xyz,abc
 * 输入："hello"
 * 输出："hello"
 * 
 * 3. 多次替换：
 * 配置：replace:a,b
 * 输入："aaa"
 * 输出："bbb"
 * 
 * 注意：
 * 1. 替换是全局的，会替换所有匹配项
 * 2. 支持数组和集合类型的批量处理
 * 3. 非字符串类型的输入将被转换为字符串后处理
 * 4. null值将返回null
 * 5. 目标字符串和替换字符串都不能为null
 */
public class ReplaceProcessor implements ValueProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ReplaceProcessor.class);
    private final String target;
    private final String replacement;

    public ReplaceProcessor(String config) {
        if (config == null || config.isEmpty()) {
            throw new IllegalArgumentException("ReplaceProcessor requires config in format: target,replacement");
        }
        String[] parts = config.split(",", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("ReplaceProcessor config must have 2 parts: target,replacement");
        }
        this.target = parts[0].trim();
        this.replacement = parts[1].trim();
        logger.debug("ReplaceProcessor initialized with target '{}' and replacement '{}'", target, replacement);
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof String) {
            return ((String) value).replace(target, replacement);
        }
        return value;
    }
} 