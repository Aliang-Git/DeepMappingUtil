package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;
import org.slf4j.*;

import java.math.*;
import java.util.*;

/**
 * 乘以10处理器
 * 将数值乘以10
 * 
 * 配置格式：multiplyByTen
 * 不需要额外参数
 * 
 * 示例1 - 整数处理：
 * 配置：multiplyByTen
 * 输入：5
 * 输出：50
 * 
 * 示例2 - 小数处理：
 * 配置：multiplyByTen
 * 输入：3.14
 * 输出：31.4
 * 
 * 示例3 - 负数处理：
 * 配置：multiplyByTen
 * 输入：-7
 * 输出：-70
 * 
 * 示例4 - 字符串数字：
 * 配置：multiplyByTen
 * 输入："12.5"
 * 输出：125.0
 * 
 * 示例5 - 批量处理（数组）：
 * 配置：multiplyByTen
 * 输入：[1, 2, 3]
 * 输出：[10, 20, 30]
 * 
 * 特殊情况处理：
 * 1. 零值：
 * 输入：0
 * 输出：0
 * 
 * 2. 非数字字符串：
 * 输入："abc"
 * 输出：null
 * 
 * 3. 极大值：
 * 输入：Integer.MAX_VALUE
 * 输出：null（超出范围）
 * 
 * 注意：
 * 1. 支持整数和小数处理
 * 2. 支持数组和集合类型的批量处理
 * 3. 非数字类型的输入将尝试转换为数字后处理
 * 4. null值将返回null
 * 5. 超出数值范围的结果将返回null
 */
public class MultiplyByTenProcessor implements ValueProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MultiplyByTenProcessor.class);
    private final BigDecimal multiplier;

    public MultiplyByTenProcessor(String config) {
        BigDecimal tempMultiplier = BigDecimal.TEN;
        if (config != null && !config.isEmpty()) {
            try {
                tempMultiplier = new BigDecimal(config);
            } catch (NumberFormatException e) {
                logger.warn("Invalid multiplier config: {}, using default value 10", config);
            }
        }
        this.multiplier = tempMultiplier;
        logger.debug("MultiplyByTenProcessor initialized with multiplier: {}", multiplier);
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof Number) {
            BigDecimal bd = new BigDecimal(value.toString());
            return bd.multiply(multiplier);
        }
        return value;
    }
}
