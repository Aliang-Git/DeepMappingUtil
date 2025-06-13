package com.aliang.registry;

import com.aliang.rule.ProductMappingRule;

import java.util.HashMap;
import java.util.Map;


/**
 * 注册中心（MappingRegistry）
 * 存储并管理所有产品的映射规则
 */
public class MappingRegistry {
    private final Map<String, ProductMappingRule> registry = new HashMap<>();

    public void register(ProductMappingRule rule) {
        registry.put(rule.getProductCode(), rule);
    }

    public ProductMappingRule getRule(String productCode) {
        return registry.get(productCode);
    }
}
