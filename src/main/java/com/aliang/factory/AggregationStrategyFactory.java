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
            AggregationStrategy strategy = DefaultAggregationStrategies.getStrategy(strategyName);
            if (strategy == null) {
                throw new RuntimeException("不支持的聚合处理: " + strategyName);
            }
            aggregationStrategies.add(strategy);
        }
        return aggregationStrategies;
    }
}
