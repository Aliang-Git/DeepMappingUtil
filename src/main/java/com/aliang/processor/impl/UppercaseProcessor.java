package com.aliang.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.processor.*;

import java.util.*;

/**
 * 大写转换处理器
 * 将字符串转换为大写形式
 * <p>
 * 配置格式：uppercase
 * 不需要额外参数
 * <p>
 * 示例1 - 基本转换：
 * 配置：uppercase
 * 输入："hello world"
 * 输出："HELLO WORLD"
 * <p>
 * 示例2 - 代码转换：
 * 配置：uppercase
 * 输入："get_user_info"
 * 输出："GET_USER_INFO"
 * <p>
 * 示例3 - 混合字符：
 * 配置：uppercase
 * 输入："User123_Admin"
 * 输出："USER123_ADMIN"
 * <p>
 * 示例4 - 特殊字符：
 * 配置：uppercase
 * 输入："test@example.com"
 * 输出："TEST@EXAMPLE.COM"
 * <p>
 * 示例5 - 批量处理（数组）：
 * 配置：uppercase
 * 输入：["red", "green", "blue"]
 * 输出：["RED", "GREEN", "BLUE"]
 * <p>
 * 特殊情况处理：
 * 1. 空字符串：
 * 输入：""
 * 输出：""
 * <p>
 * 2. 已是大写：
 * 输入："HELLO"
 * 输出："HELLO"
 * <p>
 * 3. 非字母字符：
 * 输入："123-456"
 * 输出："123-456"
 * <p>
 * 注意：
 * 1. 保持非字母字符不变
 * 2. 支持数组和集合类型的批量处理
 * 3. 非字符串类型的输入将被转换为字符串后处理
 * 4. null值将返回null
 * 5. 不会改变字符串的长度和空白字符
 */
public class UppercaseProcessor implements ValueProcessor {
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public UppercaseProcessor() {
        logger.logProcessorInit("UppercaseProcessor", null);
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
            String result = value.toString().toUpperCase();
            logger.logProcessSuccess("UppercaseProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("UppercaseProcessor", value, e.getMessage());
            return value;
        }
    }
}