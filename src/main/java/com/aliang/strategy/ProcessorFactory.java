package com.aliang.strategy;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.processor.*;
import com.aliang.processor.impl.*;

import java.util.*;

/**
 * 字段处理器工厂
 */
public class ProcessorFactory {
    private final ProcessorLogger logger = new DefaultProcessorLogger();

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
                logger.logProcessorCreationFailure(processorName, e.getMessage());
                // 继续处理下一个处理器，不阻断流程
            }
        }
        return processors;
    }

    public ValueProcessor createProcessor(String name, String params) {
        if (name == null || name.isEmpty()) {
            logger.logProcessorParamError("unknown", "处理器名称为空");
            return null;
        }

        name = name.toLowerCase();
        try {
            switch (name) {
                case "uppercase":
                    logger.logProcessorInit("UppercaseProcessor", params);
                    return new UppercaseProcessor();
                case "lowercase":
                    logger.logProcessorInit("LowercaseProcessor", params);
                    return new LowercaseProcessor();
                case "multiplybyten":
                    logger.logProcessorInit("MultiplyByTenProcessor", params);
                    return new MultiplyByTenProcessor(params);
                case "roundtwodecimal":
                    logger.logProcessorInit("RoundTwoDecimalProcessor", params);
                    return new RoundTwoDecimalProcessor(params);
                case "dateformat":
                    logger.logProcessorInit("DateFormatProcessor", params);
                    return new DateFormatProcessor(params);
                case "discount":
                    if (params == null) {
                        logger.logProcessorParamError("DiscountProcessor", "折扣处理器需要折扣率参数");
                        return null;
                    }
                    try {
                        double discountRate = Double.parseDouble(params);
                        logger.logProcessorInit("DiscountProcessor", params);
                        return new DiscountProcessor(discountRate);
                    } catch (NumberFormatException e) {
                        logger.logProcessorParamError("DiscountProcessor", "折扣率参数必须是有效的数字: " + params);
                        return null;
                    }
                case "prefix":
                    logger.logProcessorInit("PrefixProcessor", params);
                    return new PrefixProcessor(params);
                case "suffix":
                    logger.logProcessorInit("SuffixProcessor", params);
                    return new SuffixProcessor(params);
                case "mapvalue":
                    logger.logProcessorInit("MapValueProcessor", params);
                    return new MapValueProcessor(getStringStringMap(params));
                case "booleantoyesno":
                    logger.logProcessorInit("BooleanToYesNoProcessor", params);
                    return new BooleanToYesNoProcessor(params);
                case "capitalize":
                    logger.logProcessorInit("CapitalizeProcessor", params);
                    return new CapitalizeProcessor();
                case "range":
                    logger.logProcessorInit("RangeProcessor", params);
                    return new RangeProcessor(params);
                case "substring":
                    logger.logProcessorInit("SubstringProcessor", params);
                    return new SubstringProcessor(params);
                case "replace":
                    if (params == null) {
                        logger.logProcessorParamError("ReplaceProcessor", "替换处理器需要目标字符串和替换字符串参数");
                        return null;
                    }
                    String[] replaceParams = params.split(",");
                    if (replaceParams.length != 2) {
                        logger.logProcessorParamError("ReplaceProcessor", "替换处理器参数格式应为：target,replacement");
                        return null;
                    }
                    logger.logProcessorInit("ReplaceProcessor", params);
                    return new ReplaceProcessor(params);
                case "format":
                    logger.logProcessorInit("FormatProcessor", params);
                    return new FormatProcessor(params);
                case "tointeger":
                    logger.logProcessorInit("ToIntegerProcessor", params);
                    return new ToIntegerProcessor();
                default:
                    logger.logProcessorParamError(name, "未知的处理器类型");
                    return null;
            }
        } catch (Exception e) {
            logger.logProcessorCreationFailure(name, e.getMessage());
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
                        logger.logProcessorParamError("MapValueProcessor", "无效的键值对格式: " + pair);
                    }
                }
            } catch (Exception e) {
                logger.logProcessorCreationFailure("MapValueProcessor", "解析映射参数失败: " + e.getMessage());
            }
        }
        return map;
    }
}
