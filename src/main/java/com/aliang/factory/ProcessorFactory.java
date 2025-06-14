package com.aliang.factory;

import com.aliang.processor.*;
import com.aliang.processor.impl.*;

import java.util.*;

/**
 * 字段处理器工厂
 */
public class ProcessorFactory {
    public List<ValueProcessor> createProcessors(List<String> processorNames) {
        List<ValueProcessor> processors = new ArrayList<>();
        for (String processorName : processorNames) {
            String[] parts = processorName.split(":");
            String name = parts[0].toLowerCase();
            String params = parts.length > 1 ? parts[1] : null;
            
            ValueProcessor processor = createProcessor(name, params);
            processors.add(processor);
        }
        return processors;
    }

    private ValueProcessor createProcessor(String name, String params) {
        switch (name) {
            case "uppercase":
                return new UppercaseProcessor();
            case "lowercase":
                return new LowercaseProcessor();
            case "multiplybyten":
                return new MultiplyByTenProcessor();
            case "roundtwodecimal":
                return new RoundTwoDecimalProcessor();
            case "dateformat":
                return new DateFormatProcessor(params);
            case "discount":
                if (params == null) {
                    throw new IllegalArgumentException("折扣处理器需要折扣率参数");
                }
                try {
                    double discountRate = Double.parseDouble(params);
                    return new DiscountProcessor(discountRate);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("折扣率参数必须是有效的数字");
                }
            case "prefix":
                return new PrefixProcessor(params);
            case "mapvalue":
                return new MapValueProcessor(getStringStringMap(params));
            case "booleantoyesno":
                return new BooleanToYesNoProcessor();
            case "capitalize":
                return new CapitalizeProcessor();
            default:
                throw new IllegalArgumentException("未知的处理器类型: " + name);
        }
    }

    private Map<String, String> getStringStringMap(String params) {
        // 解析映射参数
        Map<String, String> map = new java.util.HashMap<>();
        if (params != null) {
            String[] pairs = params.split(";");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    map.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return map;
    }
}
