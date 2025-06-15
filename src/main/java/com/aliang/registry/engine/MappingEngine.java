package com.aliang.registry.engine;

import com.aliang.registry.*;
import com.aliang.registry.factory.*;
import com.aliang.rule.processor.*;
import com.aliang.rule.strategy.*;
import com.aliang.rule.strategy.impl.*;
import com.alibaba.fastjson.*;
import org.slf4j.*;

import java.util.*;

/**
 * 映射引擎类
 * <p>
 * 该类是数据映射的核心引擎，提供了统一的入口来执行数据映射操作。
 * 它通过映射注册中心获取产品映射规则，并执行相应的映射操作。
 * <p>
 * 主要功能：
 * 1. 提供统一的数据映射入口
 * 2. 根据产品编码获取对应的映射规则
 * 3. 执行数据映射操作
 * <p>
 * 使用示例：
 * <pre>
 *   创建映射注册中心
 * MappingRegistry registry = new MappingRegistry();
 *
 *   注册产品映射规则
 * ProductMappingRule rule = new ProductMappingRule("A001");
 * rule.addFieldMapping(new FieldMapping("$.user.name", "$.profile.fullName"));
 * registry.register(rule);
 *
 *  创建映射引擎
 * MappingEngine engine = new MappingEngine(registry);
 *
 *   执行映射
 * JSONObject source = JSON.parseObject("{\"user\":{\"name\":\"John\"}}");
 * JSONObject target = JSON.parseObject("{\"profile\":{\"fullName\":\"\"}}");
 * JSONObject result = engine.map(source, target, "A001");
 * </pre>
 */
public class MappingEngine {
    private static final Logger logger = LoggerFactory.getLogger(MappingEngine.class);
    /**
     * 映射注册中心，用于存储和获取产品映射规则
     */
    private final MappingRegistry registry;
    private final ProcessorFactory processorFactory;

    /**
     * 构造函数
     *
     * @param registry 映射注册中心
     */
    public MappingEngine(MappingRegistry registry) {
        this.registry = registry;
        this.processorFactory = new ProcessorFactory();
    }

    /**
     * 执行数据映射操作
     * <p>
     * 该方法会根据产品编码获取对应的映射规则，并执行数据映射操作。
     * 如果找不到对应的映射规则，会抛出异常。
     *
     * @param source 源数据
     * @return 处理后的目标数据
     * @throws IllegalArgumentException 如果找不到对应的产品编码映射规则
     */
    public JSONObject executeMapping(String code, JSONObject source, JSONObject targetTemplate) {
        try {
            /*  获取映射规则 */
            Map<String, JSONObject> mappings = registry.getMappings(code);
            if (mappings == null || mappings.isEmpty()) {
                throw new IllegalArgumentException("未找到映射规则: " + code);
            }

            /*  创建结果对象 */
            JSONObject result = new JSONObject(targetTemplate);

            /*  执行每个字段的映射 */
            for (Map.Entry<String, JSONObject> entry : mappings.entrySet()) {
                String targetPath = entry.getKey();
                JSONObject mapping = entry.getValue();

                /*  获取源路径和值 */
                String sourcePath = mapping.getString("sourcePath");
                Object value = evaluateSourcePath(source, sourcePath);

                JSONArray aggregations = mapping.getJSONArray("aggregationStrategies");
                JSONArray processors = mapping.getJSONArray("processors");

                boolean aggregateFirst = shouldAggregateFirst(aggregations);

                /* 兼容：先转换再聚合的场景 */
                if (aggregateFirst) {
                    value = applyAggregationStrategies(value, aggregations);
                }
                if (processors != null) {
                    value = applyProcessors(value, processors);
                }
                if (!aggregateFirst && aggregations != null) {
                    value = applyAggregationStrategies(value, aggregations);
                }

                /*  设置结果 */
                setTargetPathValue(result, targetPath, value);
            }

            return result;
        } catch (Exception e) {
            logger.error("执行映射失败 - code: {}, 错误: {}", code, e.getMessage());
            throw new RuntimeException("执行映射失败", e);
        }
    }

    private void setTargetPathValue(JSONObject result, String targetPath, Object value) {
        try {
            // 递归支持多层[*]嵌套
            if (targetPath.startsWith("$.")) {
                targetPath = targetPath.substring(2);
            }
            String[] parts = targetPath.split("\\.");
            setPathRecursive(result, parts, 0, value);
        } catch (Exception e) {
            logger.error("设置目标路径失败 - path: {}, 错误: {}", targetPath, e.getMessage());
            throw new RuntimeException("设置目标路径失败 ", e);
        }
    }

    // 递归辅助方法
    @SuppressWarnings("unchecked")
    private void setPathRecursive(Object current, String[] parts, int idx, Object value) {
        if (idx >= parts.length) return;
        String part = parts[idx];
        boolean isArray = part.endsWith("[*]");
        String key = isArray ? part.substring(0, part.length() - 3) : part;
        if (idx == parts.length - 1) {
            // 最后一层，赋值
            if (isArray) {
                JSONArray arr = new JSONArray();
                arr.add(value);
                if (current instanceof JSONObject) {
                    ((JSONObject) current).put(key, arr);
                }
            } else {
                if (current instanceof JSONObject) {
                    ((JSONObject) current).put(key, value);
                }
            }
            return;
        }
        // 不是最后一层
        Object next;
        if (current instanceof JSONObject) {
            JSONObject obj = (JSONObject) current;
            if (!obj.containsKey(key)) {
                next = isArray ? new JSONArray() : new JSONObject();
                obj.put(key, next);
            } else {
                next = obj.get(key);
                // 类型兼容：如果是List但不是JSONArray，转为JSONArray
                if (isArray && next instanceof List && !(next instanceof JSONArray)) {
                    JSONArray arr = new JSONArray();
                    arr.addAll((List<?>) next);
                    obj.put(key, arr);
                    next = arr;
                }
            }
            if (isArray) {
                JSONArray arr = null;
                if (next instanceof JSONArray) {
                    arr = (JSONArray) next;
                }
                JSONObject childObj = new JSONObject();
                if (arr != null && arr.isEmpty()) {
                    childObj = new JSONObject();
                    arr.add(childObj);
                }
                setPathRecursive(childObj, parts, idx + 1, value);
            } else {
                setPathRecursive(next, parts, idx + 1, value);
            }
        } else if (current instanceof JSONArray) {
            JSONArray arr = (JSONArray) current;
            JSONObject childObj;
            if (arr.isEmpty()) {
                childObj = new JSONObject();
                arr.add(childObj);
            } else {
                Object first = arr.get(0);
                if (first instanceof JSONObject) {
                    childObj = (JSONObject) first;
                } else {
                    childObj = new JSONObject();
                    arr.set(0, childObj);
                }
            }
            setPathRecursive(childObj, parts, idx + 1, value);
        }
    }

    private Object evaluateSourcePath(JSONObject source, String sourcePath) {
        try {
            return JSONPath.eval(source, sourcePath);
        } catch (Exception e) {
            logger.error("解析源路径失败 - path: {}, 错误: {}", sourcePath, e.getMessage());
            throw new RuntimeException("解析源路径失败", e);
        }
    }

    private Object applyProcessors(Object value, JSONArray processors) {
        if (value == null || processors == null || processors.isEmpty()) {
            return value;
        }

        Object result = value;
        for (int i = 0; i < processors.size(); i++) {
            String processorSpec = processors.getString(i);
            String[] parts = processorSpec.split(":", 2);
            String processorName = parts[0];
            String params = parts.length > 1 ? parts[1] : null;

            ValueProcessor processor = processorFactory.createProcessor(processorName, params);
            if (processor != null) {
                try {
                    result = processor.doProcess(result);
                    logger.debug("处理器执行成功 - processor: {}, input: {}, output: {}", processorName, value, result);
                } catch (Exception e) {
                    logger.error("处理器执行失败 - processor: {}, input: {}, error: {}", processorName, value, e.getMessage());
                }
            }
        }
        return result;
    }

    private Object applyAggregationStrategies(Object value, JSONArray strategies) {
        if (value == null || strategies == null || strategies.isEmpty()) {
            return value;
        }

        Object result = value;
        for (int i = 0; i < strategies.size(); i++) {
            String strategySpec = strategies.getString(i);
            String[] parts = strategySpec.split(":", 2);
            String strategyName = parts[0];
            String params = parts.length > 1 ? parts[1] : null;

            AggregationStrategy strategy = createAggregationStrategy(strategyName, params);
            if (strategy != null && result instanceof List) {
                try {
                    result = strategy.apply((List<?>) result);
                    logger.debug("聚合策略执行成功 - strategy: {}, input: {}, output: {}", strategyName, value, result);
                } catch (Exception e) {
                    logger.error("聚合策略执行失败 - strategy: {}, input: {}, error: {}", strategyName, value, e.getMessage());
                }
            }
        }
        return result;
    }

    private AggregationStrategy createAggregationStrategy(String strategyName, String params) {
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
                Map<String, String> joinParams = parseStrategyParams(params);
                String delimiter = joinParams.getOrDefault("delimiter", ",");
                boolean keepArrayFormat = Boolean.parseBoolean(joinParams.getOrDefault("keepArrayFormat", "false"));
                return new JoinAggregationStrategy(delimiter, keepArrayFormat);
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

    private Map<String, String> parseStrategyParams(String params) {
        Map<String, String> result = new HashMap<>();
        if (params != null && !params.isEmpty()) {
            String[] pairs = params.split(";");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    result.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
        return result;
    }

    private boolean shouldAggregateFirst(JSONArray aggregations) {
        if (aggregations == null || aggregations.isEmpty()) {
            return false;
        }
        for (int i = 0; i < aggregations.size(); i++) {
            String spec = aggregations.getString(i);
            if (spec == null || spec.isEmpty()) {
                continue;
            }
            String strategyName = spec.split(":", 2)[0].trim().toLowerCase();
            if ("join".equals(strategyName) || "concat".equals(strategyName)) {
                return false;
            }
        }
        return true;
    }
}