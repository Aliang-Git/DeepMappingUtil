package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 是/否转换
 */
public class BooleanToYesNoProcessor implements ValueProcessor {
    @Override
    public Object process(Object value) {
        System.out.println("开始执行BooleanToYesNoProcessor，value为：" + value);

        // 递归处理集合
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::process);
        }

        if (value instanceof Boolean) {
            return ((Boolean) value) ? "是" : "否";
        } else if (value instanceof String) {
            String str = ((String) value).trim().toLowerCase();
            if ("true".equals(str) || "yes".equals(str) || "1".equals(str)) {
                return "是";
            } else {
                return "否";
            }
        }
        throw new IllegalArgumentException("无法转换为布尔值：" + value);
    }
}
