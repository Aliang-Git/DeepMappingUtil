package com.aliang.service;

import com.aliang.factory.ProcessorFactory;
import com.aliang.processor.*;
import com.aliang.registry.*;
import com.aliang.strategy.*;
import com.aliang.strategy.impl.*;
import com.alibaba.fastjson.*;
import com.mongodb.client.*;
import org.bson.*;
import org.slf4j.*;

import java.util.*;

public abstract class BaseMappingService {
    private static final Logger logger = LoggerFactory.getLogger(BaseMappingService.class);

    protected final MongoClient mongoClient;
    protected final String dbName;
    protected final String collectionName;
    protected final MappingRegistry mappingRegistry;
    protected final ProcessorFactory processorFactory;

    protected BaseMappingService(String mongoUri, String dbName, String collectionName) {
        this.mongoClient = MongoClients.create(mongoUri);
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.mappingRegistry = new MappingRegistry();
        this.processorFactory = new ProcessorFactory();
    }

    protected JSONObject getMappingConfigFromMongo(String code) {
        try {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            Document doc = collection.find(new Document("code", code)).first();
            if (doc == null) {
                return null;
            }

            return JSONObject.parseObject(doc.toJson());
        } catch (Exception e) {
            logger.error("获取映射配置失败 - code: {}, 错误: {}", code, e.getMessage());
            return null;
        }
    }

    protected JSONObject executeMapping(JSONObject mappingConfig, JSONObject source, JSONObject targetTemplate) {
        try {
            // 执行映射逻辑
            JSONObject mappedResult = new JSONObject(targetTemplate);
            JSONObject mappings = mappingConfig.getJSONObject("mappings");

            for (String key : mappings.keySet()) {
                JSONObject mapping = mappings.getJSONObject(key);
                String sourcePath = mapping.getString("sourcePath");
                String targetPath = mapping.getString("targetPath");

                // 获取源值
                Object value = JSONPath.eval(source, sourcePath);

                JSONArray aggregations = mapping.getJSONArray("aggregationStrategies");
                JSONArray processors = mapping.getJSONArray("processors");

                boolean aggregateFirst = shouldAggregateFirst(aggregations);

                if (aggregateFirst && aggregations != null) {
                    value = applyAggregationStrategies(value, aggregations);
                }
                if (processors != null) {
                    value = applyProcessors(value, processors);
                }
                if (!aggregateFirst && aggregations != null) {
                    value = applyAggregationStrategies(value, aggregations);
                }

                // 设置目标值
                if (targetPath.startsWith("$.")) {
                    targetPath = targetPath.substring(2);
                }
                mappedResult.put(targetPath, value);
            }

            return mappedResult;
        } catch (Exception e) {
            logger.error("执行映射失败 - 错误: {}", e.getMessage());
            throw new RuntimeException("执行映射失败", e);
        }
    }

    protected Object applyProcessors(Object value, JSONArray processors) {
        if (value == null || processors == null || processors.isEmpty()) {
            return value;
        }

        Object result = value;
        for (int i = 0; i < processors.size(); i++) {
            String processorSpec = processors.getString(i);
            String[] parts = processorSpec.split(":", 2);
            String processorName = parts[0];
            String params = parts.length > 1 ? parts[1] : null;

            try {
                // 创建处理器
                ValueProcessor processor = processorFactory.createProcessor(processorName, params);
                if (processor != null) {
                    // 执行处理
                    result = processor.doProcess(result);
                    logger.debug("处理器执行成功 - processor: {}, input: {}, output: {}", processorName, value, result);
                }
            } catch (Exception e) {
                logger.error("处理器执行失败 - processor: {}, input: {}, error: {}", processorName, value, e.getMessage());
            }
        }
        return result;
    }

    protected Object applyAggregationStrategies(Object value, JSONArray strategies) {
        if (value == null || strategies == null || strategies.isEmpty()) {
            return value;
        }

        // 如果值不是集合，直接返回
        if (!(value instanceof List) && !(value instanceof JSONArray)) {
            return value;
        }

        // 将 JSONArray 转成 List 以便策略处理
        List<?> listValue;
        if (value instanceof List) {
            listValue = (List<?>) value;
        } else {
            listValue = ((JSONArray) value).toJavaList(Object.class);
        }

        Object result = listValue;

        for (int i = 0; i < strategies.size(); i++) {
            String spec = strategies.getString(i);
            if (spec == null || spec.trim().isEmpty()) {
                continue;
            }
            String[] parts = spec.split(":", 2);
            String name = parts[0].trim().toUpperCase();
            String params = parts.length > 1 ? parts[1] : null;

            AggregationStrategy strategy = createAggregationStrategy(name, params);
            if (strategy == null) {
                logger.warn("未知聚合策略: {}", name);
                continue;
            }

            try {
                result = strategy.apply((List<?>) result);
            } catch (Exception e) {
                logger.error("聚合策略执行失败 - strategy: {}, error: {}", name, e.getMessage());
            }
        }
        return result;
    }

    private AggregationStrategy createAggregationStrategy(String name, String params) {
        switch (name) {
            case "SUM":
                return new com.aliang.strategy.impl.SumAggregationStrategy();
            case "AVERAGE":
                return new com.aliang.strategy.impl.AverageAggregationStrategy();
            case "MAX":
                return new com.aliang.strategy.impl.MaxAggregationStrategy();
            case "MIN":
                return new com.aliang.strategy.impl.MinAggregationStrategy();
            case "COUNT":
                return new com.aliang.strategy.impl.CountAggregationStrategy();
            case "FIRST":
                return new com.aliang.strategy.impl.FirstAggregationStrategy();
            case "LAST":
                return new com.aliang.strategy.impl.LastAggregationStrategy();
            case "GROUP":
                return new com.aliang.strategy.impl.GroupAggregationStrategy();
            case "CONCAT":
                return new com.aliang.strategy.impl.ConcatAggregationStrategy();
            case "SUBTRACT":
                return new com.aliang.strategy.impl.SubtractAggregationStrategy();
            case "JOIN":
                // 默认分隔符为逗号
                String delimiter = ",";
                boolean keepArrayFormat = false;
                if (params != null) {
                    String[] paramPairs = params.split(";");
                    for (String pair : paramPairs) {
                        String[] kv = pair.split("=");
                        if (kv.length == 2) {
                            String key = kv[0].trim();
                            String value = kv[1].trim();
                            if ("delimiter".equalsIgnoreCase(key)) {
                                delimiter = value;
                            } else if ("keepArrayFormat".equalsIgnoreCase(key)) {
                                keepArrayFormat = Boolean.parseBoolean(value);
                            }
                        }
                    }
                }
                return new com.aliang.strategy.impl.JoinAggregationStrategy(delimiter, keepArrayFormat);
            default:
                // 尝试从默认策略工厂获取
                return DefaultAggregationStrategies.getStrategy(name);
        }
    }

    private boolean shouldAggregateFirst(JSONArray aggregations) {
        if (aggregations == null || aggregations.isEmpty()) {
            return true;
        }
        String firstSpec = aggregations.getString(0);
        if (firstSpec == null || firstSpec.trim().isEmpty()) {
            return true;
        }
        String name = firstSpec.split(":", 2)[0].trim().toUpperCase();
        switch (name) {
            case "SUM":
            case "AVERAGE":
            case "COUNT":
            case "MAX":
            case "MIN":
            case "SUBTRACT":
                return true; // 先聚合再处理
            default:
                return false; // 先处理再聚合
        }
    }

    public abstract JSONObject processMapping(String code, JSONObject source, JSONObject targetTemplate);
} 