package com.aliang.Engine;

import com.aliang.registry.MappingRegistry;
import com.aliang.rule.ProductMappingRule;
import com.alibaba.fastjson.JSONObject;

/**
 * 映射引擎，提供统一入口进行映射操作
 */
public class MappingEngine {
    private final MappingRegistry registry;

    public MappingEngine(MappingRegistry registry) {
        this.registry = registry;
    }

    public JSONObject map(JSONObject source, JSONObject target, String productCode) {
        ProductMappingRule rule = registry.getRule(productCode);
        if (rule == null) {
            throw new IllegalArgumentException("未找到对应的产品编码映射规则: " + productCode);
        }
        rule.apply(source, target);
        return target;
    }
}