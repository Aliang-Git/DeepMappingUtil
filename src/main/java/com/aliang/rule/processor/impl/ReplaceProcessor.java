package com.aliang.rule.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;

/**
 * 字符串替换处理器
 * 将字符串中的指定内容替换为新的内容
 * <p>
 * 配置格式：replace:目标字符串,替换字符串
 * <p>
 * 示例1 - 基本替换：
 * 配置：replace:old,new
 * 输入："old_value"
 * 输出："new_value"
 * <p>
 * 示例2 - 分隔符替换：
 * 配置：replace:_,-
 * 输入："user_name_info"
 * 输出："user-name-info"
 * <p>
 * 示例3 - 空格处理：
 * 配置：replace: ,_
 * 输入："hello world test"
 * 输出："hello_world_test"
 * <p>
 * 示例4 - 特殊字符替换：
 * 配置：replace:@at@
 * 输入："user@example.com"
 * 输出："useratexample.com"
 * <p>
 * 示例5 - 批量处理（数组）：
 * 配置：replace:test,prod
 * 输入：["test_1", "test_2", "test_3"]
 * 输出：["prod_1", "prod_2", "prod_3"]
 * <p>
 * 特殊情况处理：
 * 1. 空字符串：
 * 配置：replace:a,b
 * 输入：""
 * 输出：""
 * <p>
 * 2. 目标不存在：
 * 配置：replace:xyz,abc
 * 输入："hello"
 * 输出："hello"
 * <p>
 * 3. 多次替换：
 * 配置：replace:a,b
 * 输入："aaa"
 * 输出："bbb"
 * <p>
 * 注意：
 * 1. 替换是全局的，会替换所有匹配项
 * 2. 支持数组和集合类型的批量处理
 * 3. 非字符串类型的输入将被转换为字符串后处理
 * 4. null值将返回null
 * 5. 目标字符串和替换字符串都不能为null
 */
public class ReplaceProcessor implements ValueProcessor {
    private final String target;
    private final String replacement;
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public ReplaceProcessor(String config) {
        if (config == null || config.isEmpty()) {
            logger.logInvalidConfig("ReplaceProcessor", config, "oldValue,newValue");
            throw new IllegalArgumentException("替换处理器配置不能为空");
        }

        String[] parts = config.split(",");
        if (parts.length != 2) {
            logger.logInvalidConfig("ReplaceProcessor", config, "oldValue,newValue");
            throw new IllegalArgumentException("替换处理器配置格式错误，应为: oldValue,newValue");
        }

        this.target = parts[0];
        this.replacement = parts[1];
        logger.logProcessorInit("ReplaceProcessor",
                String.format("目标文本: '%s', 替换为: '%s'", target, replacement));
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        try {
            String strValue;
            if (value instanceof String) {
                strValue = (String) value;
            } else {
                strValue = String.valueOf(value);
            }

            String result = strValue.replace(target, replacement);
            if (result.equals(strValue)) {
                logger.logProcessSuccess("ReplaceProcessor", value, result);
                return value; /*  如果没有发生替换，返回原值 */
            }

            logger.logProcessSuccess("ReplaceProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("ReplaceProcessor", value, e.getMessage());
            return value;
        }
    }
} 