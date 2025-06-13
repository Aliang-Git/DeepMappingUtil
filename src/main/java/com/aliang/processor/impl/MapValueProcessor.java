package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * map转换
 * <p>
 * "sourcePath": "$.order.status",
 * "targetPath": "$.order.statusLabel",
 * "processors": ["mapValue:1=已发货;2=已取消;3=处理中"]
 */
public class MapValueProcessor implements ValueProcessor {
    private final Map<String, String> mapping;

    public MapValueProcessor(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    @Override
    public Object process(Object value) {
        System.out.println("开始执行MapValueProcessor，value为：" + value);

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::process);
        }

        if (value == null) {
            return null;
        }

        String key = value.toString();
        // 查找映射，如果找不到则返回原值
        return mapping.getOrDefault(key, key);
    }
}
