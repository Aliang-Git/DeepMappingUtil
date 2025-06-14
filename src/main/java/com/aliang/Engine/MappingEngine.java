package com.aliang.engine;

import com.aliang.registry.MappingRegistry;
import com.aliang.rule.ProductMappingRule;
import com.alibaba.fastjson.JSONObject;

/**
 * 映射引擎类
 * 
 * 该类是数据映射的核心引擎，提供了统一的入口来执行数据映射操作。
 * 它通过映射注册中心获取产品映射规则，并执行相应的映射操作。
 * 
 * 主要功能：
 * 1. 提供统一的数据映射入口
 * 2. 根据产品编码获取对应的映射规则
 * 3. 执行数据映射操作
 * 
 * 使用示例：
 * <pre>
 * // 创建映射注册中心
 * MappingRegistry registry = new MappingRegistry();
 * 
 * // 注册产品映射规则
 * ProductMappingRule rule = new ProductMappingRule("A001");
 * rule.addFieldMapping(new FieldMapping("$.user.name", "$.profile.fullName"));
 * registry.register(rule);
 * 
 * // 创建映射引擎
 * MappingEngine engine = new MappingEngine(registry);
 * 
 * // 执行映射
 * JSONObject source = JSON.parseObject("{\"user\":{\"name\":\"John\"}}");
 * JSONObject target = JSON.parseObject("{\"profile\":{\"fullName\":\"\"}}");
 * JSONObject result = engine.map(source, target, "A001");
 * </pre>
 */
public class MappingEngine {
    /**
     * 映射注册中心，用于存储和获取产品映射规则
     */
    private final MappingRegistry registry;

    /**
     * 构造函数
     * 
     * @param registry 映射注册中心
     */
    public MappingEngine(MappingRegistry registry) {
        this.registry = registry;
    }

    /**
     * 执行数据映射操作
     * 
     * 该方法会根据产品编码获取对应的映射规则，并执行数据映射操作。
     * 如果找不到对应的映射规则，会抛出异常。
     * 
     * @param source 源数据
     * @param target 目标数据
     * @param productCode 产品编码
     * @return 处理后的目标数据
     * @throws IllegalArgumentException 如果找不到对应的产品编码映射规则
     */
    public JSONObject map(JSONObject source, JSONObject target, String productCode) {
        ProductMappingRule rule = registry.getRule(productCode);
        if (rule == null) {
            throw new IllegalArgumentException("未找到对应的产品编码映射规则: " + productCode);
        }
        return rule.apply(source, target);
    }
}