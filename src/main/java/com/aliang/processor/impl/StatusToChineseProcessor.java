package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 状态码替换处理器
 */
public class StatusToChineseProcessor implements ValueProcessor {
    private final Map<String, String> statusMap = new HashMap<>();

    public StatusToChineseProcessor() {
        statusMap.put("completed", "已支付");
        statusMap.put("processing", "未支付");
        statusMap.put("caneled", "已取消");
    }

    @Override
    public Object process(Object value) {
        System.out.println("开始执行StatusToChineseProcessor，value为：" + value);

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::process);
        }

        if (value instanceof String) {
            return statusMap.getOrDefault((String) value, "未知状态");
        }
        return value;
    }
}
