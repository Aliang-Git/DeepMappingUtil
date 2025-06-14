package com.aliang.rule.strategy;

import java.util.*;

@FunctionalInterface
public interface AggregationStrategy {
    Object apply(List<?> values);
}
