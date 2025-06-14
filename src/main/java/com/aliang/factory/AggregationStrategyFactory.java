package com.aliang.factory;

import com.aliang.strategy.*;
import com.aliang.strategy.impl.*;

import java.util.*;

/**
 * 字段聚合处理器工厂
 */
public class AggregationStrategyFactory {

    public static List<AggregationStrategy> createStrategy(List<String> strategyNameList) {
        List<AggregationStrategy> aggregationStrategies = new ArrayList<>();
        for (String strategyName : strategyNameList) {
            switch (strategyName.toLowerCase()) {
                case "sum":
                    aggregationStrategies.add(DefaultAggregationStrategies.SUM);
                    break;
                case "subtract":
                    aggregationStrategies.add(DefaultAggregationStrategies.SUBTRACT);
                    break;
                case "first":
                    aggregationStrategies.add(DefaultAggregationStrategies.FIRST);
                    break;
                case "last":
                    aggregationStrategies.add(DefaultAggregationStrategies.LAST);
                    break;
                case "max":
                    aggregationStrategies.add(DefaultAggregationStrategies.MAX);
                    break;
                case "min":
                    aggregationStrategies.add(DefaultAggregationStrategies.MIN);
                    break;
                case "join":
                    aggregationStrategies.add(DefaultAggregationStrategies.JOIN);
                    break;
                case "average":
                    aggregationStrategies.add(DefaultAggregationStrategies.AVERAGE);
                    break;
                case "group":
                    aggregationStrategies.add(DefaultAggregationStrategies.GROUP);
                    break;
                case "count":
                    aggregationStrategies.add(DefaultAggregationStrategies.COUNT);
                    break;
                case "concat":
                    aggregationStrategies.add(DefaultAggregationStrategies.CONCAT);
                    break;
                default:
                    throw new RuntimeException("不支持的聚合处理: " + strategyName);
            }
        }
        return aggregationStrategies;
    }
}
