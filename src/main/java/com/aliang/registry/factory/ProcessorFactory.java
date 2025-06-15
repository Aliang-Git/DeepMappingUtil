package com.aliang.registry.factory;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;
import com.aliang.rule.processor.impl.*;

import java.util.*;

/**
 * 字段处理器工厂
 */
public class ProcessorFactory {
    private final ProcessorLogger logger = new DefaultProcessorLogger();

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
                case "prefix":
                    if (params == null) {
                        logger.logProcessorParamError("PrefixProcessor", "前缀处理器需要前缀参数");
                        return null;
                    }
                    logger.logProcessorInit("PrefixProcessor", params);
                    return new PrefixProcessor(params);
                case "suffix":
                    if (params == null) {
                        logger.logProcessorParamError("SuffixProcessor", "后缀处理器需要后缀参数");
                        return null;
                    }
                    logger.logProcessorInit("SuffixProcessor", params);
                    return new SuffixProcessor(params);
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
                    logger.logProcessorInit("DiscountProcessor", params);
                    return new DiscountProcessor(params);
                case "mapvalue":
                    logger.logProcessorInit("MapValueProcessor", params);
                    return new MapValueProcessor(params);
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
                    logger.logProcessorInit("ReplaceProcessor", params);
                    return new ReplaceProcessor(params);
                case "format":
                    logger.logProcessorInit("FormatProcessor", params);
                    return new FormatProcessor(params);
                case "tointeger":
                    logger.logProcessorInit("ToIntegerProcessor", params);
                    return new ToIntegerProcessor(params);
                case "money":
                    logger.logProcessorInit("MoneyProcessor", params);
                    return new MoneyProcessor(params);
                case "phone":
                    logger.logProcessorInit("PhoneNumberProcessor", params);
                    return new PhoneNumberProcessor(params);
                case "json":
                    logger.logProcessorInit("JsonProcessor", params);
                    return new JsonProcessor();
                case "reverse":
                    logger.logProcessorInit("ReverseProcessor", params);
                    return new ReverseProcessor(params);
                case "trim":
                    logger.logProcessorInit("TrimProcessor", params);
                    return new TrimProcessor();
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
