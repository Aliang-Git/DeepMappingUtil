package com.aliang.Foctory;

import com.aliang.processor.*;
import com.aliang.processor.impl.*;

import java.util.*;

/**
 * 根据配置名称创建对应的字段处理器实例
 */
public class ProcessorFactory {
    public static List<ValueProcessor> createProcessors(List<String> processorNames) {
        List<ValueProcessor> processors = new ArrayList<>();
        for (String name : processorNames) {
            if ("uppercase".equals(name)) {
                processors.add(new UppercaseProcessor());
            } else if ("lowercase".equals(name)) {
                processors.add(new LowercaseProcessor());
            } else if ("multiplyByTen".equals(name)) {
                processors.add(new MultiplyByTenProcessor());
            } else if ("roundTwoDecimal".equals(name)) {
                processors.add(new RoundTwoDecimalProcessor());
            } else if ("statusToChinese".equals(name)) {
                processors.add(new StatusToChineseProcessor());
            } else if (name.startsWith("dateFormat:")) {
                String pattern = name.substring("dateFormat:".length());
                processors.add(new DateFormatProcessor(pattern));
            } else if (name.startsWith("discount:")) {
                String rateStr = name.substring("discount:".length());
                double rate;
                try {
                    rate = Double.parseDouble(rateStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("折扣率必须为数字: " + rateStr, e);
                }
                processors.add(new DiscountProcessor(rate));
            } else if (name.startsWith("prefix:")) {
                String prefix = name.substring("prefix:".length());
                processors.add(new PrefixProcessor(prefix));
            } else if (name.startsWith("mapValue:")) {
                String mappingStr = name.substring("mapValue:".length());

                // 解析 mappingStr 成 Map<String, String>
                Map<String, String> mapping = new HashMap<>();
                String[] entries = mappingStr.split(";");
                for (String entry : entries) {
                    String[] parts = entry.split("=", 2); // 分割键值对
                    if (parts.length == 2) {
                        mapping.put(parts[0].trim(), parts[1].trim());
                    } else {
                        throw new IllegalArgumentException("无效的 mapValue 配置项: " + entry);
                    }
                }

                processors.add(new MapValueProcessor(mapping));
            } else if ("booleanToYesNo".equals(name)) {
                processors.add(new BooleanToYesNoProcessor());
            } else if ("capitalize".equals(name)) {
                processors.add(new CapitalizeProcessor());
            } else {
                throw new IllegalArgumentException("未知的处理器类型：" + name);
            }
        }
        return processors;
    }
}
