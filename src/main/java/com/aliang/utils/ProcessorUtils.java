package com.aliang.utils;

import java.util.*;
import java.util.function.*;

public class ProcessorUtils {

    // 处理 List，支持嵌套结构
    public static List<Object> processList(List<?> list, Function<Object, Object> processor) {
        List<Object> result = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof List<?>) {
                result.add(processList((List<?>) item, processor));
            } else if (item instanceof Map<?, ?>) {
                result.add(processMap((Map<?, ?>) item, processor));
            } else {
                result.add(processor.apply(item));
            }
        }
        System.out.println("processList处理后："+result);
        return result;
    }

    // 改为泛型方法，支持任意类型的 Map
    public static Object processMap(Map<?, ?> map, Function<Object, Object> processor) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            // 确保 key 能转成 String
            String stringKey = key != null ? key.toString() : "null";

            if (value instanceof List<?>) {
                result.put(stringKey, processList((List<?>) value, processor));
            } else if (value instanceof Map<?, ?>) {
                result.put(stringKey, processMap((Map<?, ?>) value, processor));
            } else {
                result.put(stringKey, processor.apply(value));
            }
        }
        System.out.println("processMap处理后："+result);
        return result;
    }

    // 统一处理集合类型（List 或 Map）
    public static Object processCollection(Object value, Function<Object, Object> processor) {
        if (value instanceof List<?>) {
            return processList((List<?>) value, processor);
        } else if (value instanceof Map<?, ?>) {
            return processMap((Map<?, ?>) value, processor);
        }
        System.out.println("processCollection处理后："+value);
        return value;
    }
}

