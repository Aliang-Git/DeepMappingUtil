package com.aliang.factory;

import com.aliang.strategy.*;
import com.aliang.strategy.impl.*;

import java.util.*;

/**
 * 字段聚合处理器工厂
 */
public class AggregationStrategyFactory {

    public static List<AggregationStrategy> createStrategy(List<String> strategyConfigs) {
        List<AggregationStrategy> aggregationStrategies = new ArrayList<>();
        for (String strategyConfig : strategyConfigs) {
            // 解析策略配置，格式：strategyName:param1=value1;param2=value2
            String[] parts = strategyConfig.split(":");
            String strategyName = parts[0].trim();
            Map<String, String> params = new HashMap<>();

            if (parts.length > 1) {
                String[] paramPairs = parts[1].split(";");
                for (String pair : paramPairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        params.put(keyValue[0].trim(), keyValue[1].trim());
                    }
                }
            }

            // 根据策略名称和参数创建策略实例
            AggregationStrategy strategy = createStrategyWithParams(strategyName, params);
            if (strategy == null) {
                throw new RuntimeException("不支持的聚合处理: " + strategyName);
            }
            aggregationStrategies.add(strategy);
        }
        return aggregationStrategies;
    }

    private static AggregationStrategy createStrategyWithParams(String strategyName, Map<String, String> params) {
        // 根据策略名称创建对应的策略实例
        switch (strategyName.toUpperCase()) {
            case "JOIN":
                String delimiter = params.getOrDefault("delimiter", ",");
                boolean keepArrayFormat = Boolean.parseBoolean(params.getOrDefault("keepArrayFormat", "false"));
                return new JoinAggregationStrategy(delimiter, keepArrayFormat);
            // 其他策略的处理...
            default:
                return DefaultAggregationStrategies.getStrategy(strategyName);
        }
    }
}
