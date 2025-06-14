package com.aliang.rule.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;

import java.util.*;

/**
 * 前缀处理器
 * 在字符串前添加指定的前缀
 * <p>
 * 配置格式：prefix:前缀字符串
 * <p>
 * 示例1 - 用户名前缀：
 * 配置：prefix:USER_
 * 输入："admin"
 * 输出："USER_admin"
 * <p>
 * 示例2 - 编号前缀：
 * 配置：prefix:ORD-
 * 输入："20240320001"
 * 输出："ORD-20240320001"
 * <p>
 * 示例3 - 文件名前缀：
 * 配置：prefix:backup_
 * 输入："data.json"
 * 输出："backup_data.json"
 * <p>
 * 示例4 - 多语言前缀：
 * 配置：prefix:zh_CN_
 * 输入："message"
 * 输出："zh_CN_message"
 * <p>
 * 示例5 - 批量处理（数组）：
 * 配置：prefix:SKU_
 * 输入：["001", "002", "003"]
 * 输出：["SKU_001", "SKU_002", "SKU_003"]
 * <p>
 * 特殊情况处理：
 * 1. 空字符串：
 * 配置：prefix:TEST_
 * 输入：""
 * 输出："TEST_"
 * <p>
 * 2. null值：
 * 配置：prefix:LOG_
 * 输入：null
 * 输出：null
 * <p>
 * 3. 数字值：
 * 配置：prefix:NO_
 * 输入：123
 * 输出："NO_123"
 * <p>
 * 注意：
 * 1. 前缀不能为null，但可以是空字符串
 * 2. 支持任意字符串作为前缀，包括特殊字符
 * 3. 支持数组和集合类型的批量处理
 * 4. 非字符串类型的输入将被转换为字符串后处理
 * 5. 如果输入为null，则直接返回null
 */
public class PrefixProcessor implements ValueProcessor {
    private final String prefix;
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public PrefixProcessor(String prefix) {
        this.prefix = prefix != null ? prefix : "";
        logger.logProcessorInit("PrefixProcessor", "前缀: " + this.prefix);
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof List<?>) {
            List<?> list = (List<?>) value;
            List<Object> result = new ArrayList<>();
            for (Object item : list) {
                result.add(doProcess(item));
            }
            return result;
        } else if (value instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) value;
            Map<Object, Object> result = new HashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(entry.getKey(), doProcess(entry.getValue()));
            }
            return result;
        }

        try {
            String result = prefix + value;
            logger.logProcessSuccess("PrefixProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("PrefixProcessor", value, e.getMessage());
            return value;
        }
    }
}
