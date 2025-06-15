package com.aliang.rule.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 小写转换处理器
 * 将字符串转换为小写形式
 * <p>
 * 配置格式：lowercase
 * 不需要额外参数
 * <p>
 * 示例1 - 基本转换：
 * 配置：lowercase
 * 输入："HELLO WORLD"
 * 输出："hello world"
 * <p>
 * 示例2 - URL转换：
 * 配置：lowercase
 * 输入："HTTPS: EXAMPLE.COM"
 * 输出："https: example.com"
 * <p>
 * 示例3 - 混合字符：
 * 配置：lowercase
 * 输入："UserName123"
 * 输出："username123"
 * <p>
 * 示例4 - 邮箱地址：
 * 配置：lowercase
 * 输入："USER@EXAMPLE.COM"
 * 输出："user@example.com"
 * <p>
 * 示例5 - 批量处理（数组）：
 * 配置：lowercase
 * 输入：["RED", "GREEN", "BLUE"]
 * 输出：["red", "green", "blue"]
 * <p>
 * 特殊情况处理：
 * 1. 空字符串：
 * 输入：""
 * 输出：""
 * <p>
 * 2. 已是小写：
 * 输入："hello"
 * 输出："hello"
 * <p>
 * 3. 非字母字符：
 * 输入："123-ABC-456"
 * 输出："123-abc-456"
 * <p>
 * 注意：
 * 1. 保持非字母字符不变
 * 2. 支持数组和集合类型的批量处理
 * 3. 非字符串类型的输入将被转换为字符串后处理
 * 4. null值将返回null
 * 5. 不会改变字符串的长度和空白字符
 */
public class LowercaseProcessor implements ValueProcessor {
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }

        try {
            String result = value.toString().toLowerCase();
            logger.logProcessSuccess("LowercaseProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("LowercaseProcessor", value, e.getMessage());
            return value;
        }
    }
}
