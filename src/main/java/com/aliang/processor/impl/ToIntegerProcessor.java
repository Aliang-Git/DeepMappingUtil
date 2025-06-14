package com.aliang.processor.impl;

import com.aliang.processor.*;

import java.util.*;

/**
 * 整数转换处理器
 * 将数值转换为整数类型
 * <p>
 * 示例1 - 单个数值：
 * 输入：15.0
 * 输出：15
 * <p>
 * 示例2 - 数组：
 * 输入：[5,4,3,2,1]
 * 输出：15 (数组元素之和)
 * <p>
 * 特点：
 * 1. 支持数值类型的转换
 * 2. 支持字符串类型的数值转换
 * 3. 支持数组类型（计算元素之和）
 * 4. null值返回null
 * 5. 非数值类型抛出异常
 */
public class ToIntegerProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof List) {
            List<?> list = (List<?>) value;
            int sum = 0;
            for (Object item : list) {
                if (item instanceof Number) {
                    sum += ((Number) item).intValue();
                } else {
                    try {
                        sum += Integer.parseInt(item.toString());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("数组中包含无法转换为整数的元素: " + item);
                    }
                }
            }
            return sum;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无法将值转换为整数: " + value);
        }
    }
} 