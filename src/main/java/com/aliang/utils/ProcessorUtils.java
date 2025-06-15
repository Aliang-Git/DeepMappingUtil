package com.aliang.utils;

import com.aliang.rule.processor.*;

import java.util.*;
import java.util.function.*;

/**
 * 处理器工具类
 * 提供处理集合类型的通用方法
 */
public class ProcessorUtils {

    /*  处理 List，支持嵌套结构 */
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
        return result;
    }

    /*  改为泛型方法，支持任意类型的 Map */
    public static Object processMap(Map<?, ?> map, Function<Object, Object> processor) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            /*  确保 key 能转成 String */
            String stringKey = key != null ? key.toString() : "null";

            if (value instanceof List<?>) {
                result.put(stringKey, processList((List<?>) value, processor));
            } else if (value instanceof Map<?, ?>) {
                result.put(stringKey, processMap((Map<?, ?>) value, processor));
            } else {
                result.put(stringKey, processor.apply(value));
            }
        }
        return result;
    }

    /**
     * 处理集合类型的值
     * 如果值是List或Map类型，递归处理其中的每个元素
     *
     * @param value     要处理的值
     * @param processor 处理器
     * @return 处理后的值
     */
    public static Object processCollection(Object value, ValueProcessor processor) {
        if (value == null) {
            return null;
        }

        if (value instanceof List<?>) {
            List<?> list = (List<?>) value;
            List<Object> result = new ArrayList<>();
            for (Object item : list) {
                result.add(processor.doProcess(item));
            }
            return result;
        } else if (value instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) value;
            Map<Object, Object> result = new HashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(entry.getKey(), processor.doProcess(entry.getValue()));
            }
            return result;
        }
        return processor.doProcess(value);
    }
}

