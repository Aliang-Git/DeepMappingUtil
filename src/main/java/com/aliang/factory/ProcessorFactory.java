package com.aliang.factory;

import com.aliang.processor.ValueProcessor;
import com.aliang.processor.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字段处理器工厂
 */
public class ProcessorFactory {
    private static final Logger logger = LoggerFactory.getLogger(ProcessorFactory.class);

    public List<ValueProcessor> createProcessors(List<String> processorNames) {
        List<ValueProcessor> processors = new ArrayList<>();
        if (processorNames == null || processorNames.isEmpty()) {
            return processors;
        }

        for (String processorName : processorNames) {
            try {
                String[] parts = processorName.split(":", 2);
                String name = parts[0].toLowerCase();
                String params = parts.length > 1 ? parts[1] : null;
                
                ValueProcessor processor = createProcessor(name, params);
                if (processor != null) {
                    processors.add(processor);
                }
            } catch (Exception e) {
                logger.error("创建处理器失败: {}, 错误信息: {}", processorName, e.getMessage());
                // 继续处理下一个处理器，不阻断流程
            }
        }
        return processors;
    }

    public ValueProcessor createProcessor(String name, String params) {
        if (name == null || name.isEmpty()) {
            logger.warn("处理器名称为空");
            return null;
        }

        name = name.toLowerCase();
        try {
            switch (name) {
                case "uppercase":
                    return new UppercaseProcessor();
                case "lowercase":
                    return new LowercaseProcessor();
                case "multiplybyten":
                    return new MultiplyByTenProcessor(params);
                case "roundtwodecimal":
                    return new RoundTwoDecimalProcessor(params);
                case "dateformat":
                    return new DateFormatProcessor(params);
                case "discount":
                    if (params == null) {
                        logger.warn("折扣处理器需要折扣率参数");
                        return null;
                    }
                    try {
                        double discountRate = Double.parseDouble(params);
                        return new DiscountProcessor(discountRate);
                    } catch (NumberFormatException e) {
                        logger.warn("折扣率参数必须是有效的数字: {}", params);
                        return null;
                    }
                case "prefix":
                    return new PrefixProcessor(params);
                case "suffix":
                    return new SuffixProcessor(params);
                case "mapvalue":
                    return new MapValueProcessor(getStringStringMap(params));
                case "booleantoyesno":
                    return new BooleanToYesNoProcessor(params);
                case "capitalize":
                    return new CapitalizeProcessor();
                case "range":
                    return new RangeProcessor(params);
                case "substring":
                    return new SubstringProcessor(params);
                case "replace":
                    if (params == null) {
                        logger.warn("替换处理器需要目标字符串和替换字符串参数");
                        return null;
                    }
                    String[] replaceParams = params.split(",");
                    if (replaceParams.length != 2) {
                        logger.warn("替换处理器参数格式应为：target,replacement");
                        return null;
                    }
                    return new ReplaceProcessor(params);
                case "format":
                    return new FormatProcessor(params);
                default:
                    logger.warn("未知的处理器类型: {}", name);
                    return null;
            }
        } catch (Exception e) {
            logger.error("创建处理器失败: {}, 错误信息: {}", name, e.getMessage());
            return null;
        }
    }

    private Map<String, String> getStringStringMap(String params) {
        Map<String, String> map = new HashMap<>();
        if (params != null) {
            try {
                String[] pairs = params.split(";");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        map.put(keyValue[0], keyValue[1]);
                    } else {
                        logger.warn("无效的键值对格式: {}", pair);
                    }
                }
            } catch (Exception e) {
                logger.error("解析映射参数失败: {}, 错误信息: {}", params, e.getMessage());
            }
        }
        return map;
    }
}
