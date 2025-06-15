package com.aliang.utils;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;

import java.math.*;
import java.util.*;
import java.util.function.*;

/**
 * 处理器工具类
 * 提供处理集合类型的通用方法
 */
public class ProcessorUtils {
    private static final ProcessorLogger logger = new DefaultProcessorLogger();

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

    /**
     * 记录处理器处理结果
     *
     * @param processorName 处理器名称
     * @param input         输入值
     * @param output        输出值
     * @param error         错误信息（如果有）
     */
    public static void logProcessResult(String processorName, Object input, Object output, String error) {
        if (error != null) {
            logger.logProcessFailure(processorName, input, error);
        } else {
            logger.logProcessSuccess(processorName, input, output);
        }
    }

    /**
     * 安全地转换字符串为数字
     *
     * @param value 要转换的值
     * @return 转换后的数字，如果转换失败则返回null
     */
    public static BigDecimal safeParseNumber(Object value) {
        if (value == null) {
            return null;
        }
        try {
            if (value instanceof Number) {
                return new BigDecimal(value.toString());
            }
            String strValue = value.toString().replaceAll("[^\\d.-]", "");
            return new BigDecimal(strValue);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 安全地转换字符串为整数
     *
     * @param value 要转换的值
     * @return 转换后的整数，如果转换失败则返回null
     */
    public static Integer safeParseInteger(Object value) {
        BigDecimal number = safeParseNumber(value);
        return number != null ? number.intValue() : null;
    }
}

