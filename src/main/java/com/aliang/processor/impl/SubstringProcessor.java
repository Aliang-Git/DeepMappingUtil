package com.aliang.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.processor.*;
import com.aliang.utils.*;

/**
 * 字符串截取处理器
 * 从字符串中截取指定范围的子串
 * <p>
 * 配置格式：substring:起始位置,结束位置
 * 位置说明：
 * - 起始位置：从0开始计数
 * - 结束位置：不包含该位置的字符
 * <p>
 * 示例1 - 提取前缀：
 * 配置：substring:0,3
 * 输入："PRODUCT12345"
 * 输出："PRO"
 * <p>
 * 示例2 - 提取中间部分：
 * 配置：substring:3,7
 * 输入："USER_ADMIN_ROLE"
 * 输出："_ADM"
 * <p>
 * 示例3 - 提取编号：
 * 配置：substring:4,10
 * 输入："ORD_20240320_001"
 * 输出："202403"
 * <p>
 * 示例4 - 提取后缀：
 * 配置：substring:11,14
 * 输入："FILE_2024_DOC"
 * 输出："DOC"
 * <p>
 * 示例5 - 批量处理（数组）：
 * 配置：substring:0,4
 * 输入：["CODE001", "CODE002", "CODE003"]
 * 输出：["CODE", "CODE", "CODE"]
 * <p>
 * 特殊情况处理：
 * 1. 超出字符串长度：
 * 配置：substring:0,10
 * 输入："ABC"
 * 输出："ABC" (自动调整到字符串实际长度)
 * <p>
 * 2. 起始位置超出长度：
 * 配置：substring:5,10
 * 输入："ABC"
 * 输出："" (返回空字符串)
 * <p>
 * 注意：
 * 1. 位置参数必须是非负整数
 * 2. 起始位置必须小于等于结束位置
 * 3. 支持数组和集合类型的批量处理
 * 4. 非字符串类型的输入将被转换为字符串后处理
 * 5. 会自动处理越界情况，确保安全运行
 */
public class SubstringProcessor implements ValueProcessor {
    private final int start;
    private final int end;
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public SubstringProcessor(String config) {
        if (config == null || config.isEmpty()) {
            logger.logInvalidConfig("SubstringProcessor", config, "0,10");
            throw new IllegalArgumentException("截取处理器配置不能为空");
        }

        String[] parts = config.split(",");
        if (parts.length != 2) {
            logger.logInvalidConfig("SubstringProcessor", config, "0,10");
            throw new IllegalArgumentException("截取处理器配置格式错误，应为: start,end");
        }

        try {
            this.start = Integer.parseInt(parts[0]);
            this.end = Integer.parseInt(parts[1]);

            if (start < 0 || end < start) {
                logger.logInvalidConfig("SubstringProcessor", config, "0,10");
                throw new IllegalArgumentException("无效的截取范围: 起始位置不能小于0，结束位置不能小于起始位置");
            }

            logger.logProcessorInit("SubstringProcessor",
                    String.format("截取范围: [%d, %d]", start, end));
        } catch (NumberFormatException e) {
            logger.logInvalidConfig("SubstringProcessor", config, "0,10");
            throw new IllegalArgumentException("无效的截取范围配置: " + e.getMessage());
        }
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        // 如果是集合或 Map，递归处理其内部元素
        if (value instanceof java.util.Collection<?> || value instanceof java.util.Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this);
        }

        try {
            String strValue;
            if (value instanceof String) {
                strValue = (String) value;
            } else {
                strValue = String.valueOf(value);
            }

            if (strValue.length() < start) {
                logger.logProcessFailure("SubstringProcessor", value,
                        String.format("字符串长度(%d)小于起始位置(%d)", strValue.length(), start));
                return value;
            }

            int actualEnd = Math.min(end, strValue.length());
            String result = strValue.substring(start, actualEnd);

            logger.logProcessSuccess("SubstringProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("SubstringProcessor", value, e.getMessage());
            return value;
        }
    }
} 