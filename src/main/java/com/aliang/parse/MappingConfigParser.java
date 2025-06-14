package com.aliang.parse;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.mapping.*;
import com.aliang.processor.*;
import com.aliang.registry.*;
import com.aliang.strategy.*;
import com.aliang.strategy.impl.*;
import com.alibaba.fastjson.*;
import org.slf4j.*;

import java.util.*;

/**
 * 解析 JSON 配置文件，生成并注册产品映射规则
 */
public class MappingConfigParser {
    private static final Logger logger = LoggerFactory.getLogger(MappingConfigParser.class);
    private final ProcessorFactory processorFactory;
    private final ConfigLogger configLogger = new DefaultConfigLogger();
    private final Set<String> invalidProcessors = new HashSet<>();
    private final Set<String> invalidStrategies = new HashSet<>();
    private final Set<String> invalidFields = new HashSet<>();
    private final MappingRegistry mappingRegistry;

    public MappingConfigParser(ProcessorFactory processorFactory, MappingRegistry mappingRegistry) {
        this.processorFactory = processorFactory;
        this.mappingRegistry = mappingRegistry;
    }

    public static void parseAndRegister(JSONObject config, MappingRegistry registry) {
        // 验证配置
        validateConfig(config);

        // 解析配置并注册到注册表
        String code = config.getString("code");
        JSONObject mappings = config.getJSONObject("mappings");

        for (String key : mappings.keySet()) {
            JSONObject mapping = mappings.getJSONObject(key);
            // 确保处理器顺序正确
            if (mapping.containsKey("processors")) {
                JSONArray processors = mapping.getJSONArray("processors");
                // 验证处理器配置
                for (int i = 0; i < processors.size(); i++) {
                    String processorSpec = processors.getString(i);
                    if (processorSpec == null || processorSpec.trim().isEmpty()) {
                        logger.error("处理器配置无效 - code: {}, key: {}, index: {}", code, key, i);
                        processors.remove(i);
                        i--;
                    }
                }
            }
            registry.registerMapping(code, key, mapping);
        }
    }

    private static void validateConfig(JSONObject config) {
        if (config == null) {
            throw new IllegalArgumentException("配置不能为空");
        }

        String code = config.getString("code");
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("编码不能为空");
        }

        JSONObject mappings = config.getJSONObject("mappings");
        if (mappings == null || mappings.isEmpty()) {
            throw new IllegalArgumentException("映射配置不能为空");
        }
    }

    private FieldMapping parseFieldMapping(JSONObject mapping, String productCode) {
        validateFieldMapping(mapping);

        String sourcePath = mapping.getString("sourcePath");
        String targetPath = mapping.getString("targetPath");
        String aggregationStrategy = mapping.getString("aggregationStrategy");
        List<String> processorNames = parseProcessorNames(mapping);
        List<ValueProcessor> processors = processorFactory.createProcessors(processorNames);

        FieldMapping fieldMapping = new FieldMapping(sourcePath, targetPath, productCode);
        if (!processors.isEmpty()) {
            fieldMapping.addProcessors(processors.toArray(new ValueProcessor[0]));
        }
        if (aggregationStrategy != null && !aggregationStrategy.isEmpty()) {
            try {
                AggregationStrategy strategy = createAggregationStrategy(aggregationStrategy);
                if (strategy != null) {
                    fieldMapping.addAggregationStrategies(strategy);
                }
            } catch (Exception e) {
                logger.error("创建聚合策略失败: " + e.getMessage());
            }
        }
        return fieldMapping;
    }

    private AggregationStrategy createAggregationStrategy(String strategyName) {
        switch (strategyName.toLowerCase()) {
            case "sum":
                return new SumAggregationStrategy();
            case "avg":
            case "average":
                return new AverageAggregationStrategy();
            case "min":
                return new MinAggregationStrategy();
            case "max":
                return new MaxAggregationStrategy();
            case "first":
                return new FirstAggregationStrategy();
            case "last":
                return new LastAggregationStrategy();
            case "count":
                return new CountAggregationStrategy();
            case "join":
                return new JoinAggregationStrategy();
            case "group":
                return new GroupAggregationStrategy();
            case "subtract":
                return new SubtractAggregationStrategy();
            case "concat":
                return new ConcatAggregationStrategy();
            default:
                logger.error("未知的聚合策略类型: " + strategyName);
                return null;
        }
    }

    private void validateFieldMapping(JSONObject mapping) {
        if (mapping == null) {
            String message = "映射规则不能为空";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        if (!mapping.containsKey("sourcePath") || mapping.getString("sourcePath").isEmpty()) {
            String message = "sourcePath不能为空";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        if (!mapping.containsKey("targetPath") || mapping.getString("targetPath").isEmpty()) {
            String message = "targetPath不能为空";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private List<String> parseProcessorNames(JSONObject mapping) {
        List<String> processorNames = new ArrayList<>();
        if (mapping.containsKey("processors")) {
            JSONArray processors = mapping.getJSONArray("processors");
            for (int i = 0; i < processors.size(); i++) {
                processorNames.add(processors.getString(i));
            }
        }
        return processorNames;
    }

    public Set<String> getInvalidProcessors() {
        return Collections.unmodifiableSet(invalidProcessors);
    }

    public Set<String> getInvalidStrategies() {
        return Collections.unmodifiableSet(invalidStrategies);
    }

    public Set<String> getInvalidFields() {
        return Collections.unmodifiableSet(invalidFields);
    }
}
