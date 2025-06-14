package com.aliang.strategy;

import java.util.*;

@FunctionalInterface
public interface AggregationStrategy {
    Object apply(List<?> values);
}
