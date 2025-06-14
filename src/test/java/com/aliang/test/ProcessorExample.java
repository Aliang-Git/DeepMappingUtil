package com.aliang.test;

import com.aliang.engine.MappingEngine;
import com.aliang.factory.ProcessorFactory;
import com.aliang.mapping.FieldMapping;
import com.aliang.parse.MappingConfigParser;
import com.aliang.registry.MappingRegistry;
import com.aliang.rule.ProductMappingRule;
import com.aliang.strategy.AggregationStrategy;
import com.aliang.strategy.impl.DefaultAggregationStrategies;
import com.aliang.processor.impl.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理器和聚合处理器使用示例
 */
public class ProcessorExample {
    public static void main(String[] args) {
        // 1. 基础处理器示例
        basicProcessorExample();
        
        // 2. 聚合处理器示例
        aggregationStrategyExample();
        
        // 3. 组合使用示例
        combinedExample();
    }

    /**
     * 基础处理器示例
     * 展示各种基础处理器的使用方法
     */
    private static void basicProcessorExample() {
        System.out.println("\n=== 基础处理器示例 ===");
        
        // 创建映射规则
        ProductMappingRule rule = new ProductMappingRule("BASIC001");
        
        // 1. 字符串处理示例
        rule.addFieldMapping(new FieldMapping("$.user.name", "$.profile.fullName")
                .addProcessors(new TrimProcessor()));  // 去除空格
        
        rule.addFieldMapping(new FieldMapping("$.user.email", "$.profile.email")
                .addProcessors(new LowercaseProcessor()));  // 转小写
        
        // 2. 数值处理示例
        rule.addFieldMapping(new FieldMapping("$.order.amount", "$.finance.total")
                .addProcessors(new MultiplyByTenProcessor()));  // 乘以10
        
        rule.addFieldMapping(new FieldMapping("$.order.amount", "$.finance.formattedAmount")
                .addProcessors(new FormatProcessor()));  // 格式化数字
        
        // 3. 值映射示例
        Map<String, String> statusMapping = new HashMap<>();
        statusMapping.put("1", "已发货");
        statusMapping.put("2", "已取消");
        statusMapping.put("3", "处理中");
        
        rule.addFieldMapping(new FieldMapping("$.order.status", "$.order.statusLabel")
                .addProcessors(new MapValueProcessor(statusMapping)));  // 状态码映射
        
        // 源数据
        JSONObject source = JSON.parseObject("{\n" +
                "  \"user\": {\n" +
                "    \"name\": \"  John Doe  \",\n" +
                "    \"email\": \"John.Doe@Example.com\"\n" +
                "  },\n" +
                "  \"order\": {\n" +
                "    \"amount\": 99.99,\n" +
                "    \"status\": \"1\"\n" +
                "  }\n" +
                "}");
        
        // 目标数据
        JSONObject target = JSON.parseObject("{\n" +
                "  \"profile\": {\n" +
                "    \"fullName\": \"\",\n" +
                "    \"email\": \"\"\n" +
                "  },\n" +
                "  \"finance\": {\n" +
                "    \"total\": 0,\n" +
                "    \"formattedAmount\": \"\"\n" +
                "  },\n" +
                "  \"order\": {\n" +
                "    \"statusLabel\": \"\"\n" +
                "  }\n" +
                "}");
        
        // 执行映射
        MappingRegistry registry = new MappingRegistry();
        registry.register(rule);
        MappingEngine engine = new MappingEngine(registry);
        JSONObject result = engine.map(source, target, "BASIC001");
        
        System.out.println("处理结果：");
        System.out.println(result.toJSONString());
    }

    /**
     * 聚合处理器示例
     * 展示各种聚合处理器的使用方法
     */
    private static void aggregationStrategyExample() {
        System.out.println("\n=== 聚合处理器示例 ===");
        
        // 创建映射规则
        ProductMappingRule rule = new ProductMappingRule("AGG001");
        
        // 1. 数值聚合示例
        rule.addFieldMapping(new FieldMapping("$.items[*].price", "$.summary.totalPrice")
                .addAggregationStrategies(DefaultAggregationStrategies.SUM));  // 求和
        
        rule.addFieldMapping(new FieldMapping("$.items[*].price", "$.summary.averagePrice")
                .addAggregationStrategies(DefaultAggregationStrategies.AVERAGE));  // 平均值
        
        rule.addFieldMapping(new FieldMapping("$.items[*].price", "$.summary.maxPrice")
                .addAggregationStrategies(DefaultAggregationStrategies.MAX));  // 最大值
        
        rule.addFieldMapping(new FieldMapping("$.items[*].price", "$.summary.minPrice")
                .addAggregationStrategies(DefaultAggregationStrategies.MIN));  // 最小值
        
        // 2. 列表操作示例
        rule.addFieldMapping(new FieldMapping("$.items[*].name", "$.summary.firstItem")
                .addAggregationStrategies(DefaultAggregationStrategies.FIRST));  // 第一个元素
        
        rule.addFieldMapping(new FieldMapping("$.items[*].name", "$.summary.lastItem")
                .addAggregationStrategies(DefaultAggregationStrategies.LAST));  // 最后一个元素
        
        rule.addFieldMapping(new FieldMapping("$.items[*].name", "$.summary.allItems")
                .addAggregationStrategies(DefaultAggregationStrategies.JOIN));  // 拼接字符串
        
        // 源数据
        JSONObject source = JSON.parseObject("{\n" +
                "  \"items\": [\n" +
                "    {\"name\": \"Item 1\", \"price\": 100.0},\n" +
                "    {\"name\": \"Item 2\", \"price\": 200.0},\n" +
                "    {\"name\": \"Item 3\", \"price\": 300.0}\n" +
                "  ]\n" +
                "}");
        
        // 目标数据
        JSONObject target = JSON.parseObject("{\n" +
                "  \"summary\": {\n" +
                "    \"totalPrice\": 0,\n" +
                "    \"averagePrice\": 0,\n" +
                "    \"maxPrice\": 0,\n" +
                "    \"minPrice\": 0,\n" +
                "    \"firstItem\": \"\",\n" +
                "    \"lastItem\": \"\",\n" +
                "    \"allItems\": \"\"\n" +
                "  }\n" +
                "}");
        
        // 执行映射
        MappingRegistry registry = new MappingRegistry();
        registry.register(rule);
        MappingEngine engine = new MappingEngine(registry);
        JSONObject result = engine.map(source, target, "AGG001");
        
        System.out.println("处理结果：");
        System.out.println(result.toJSONString());
    }

    /**
     * 组合使用示例
     * 展示处理器和聚合处理器的组合使用方法
     */
    private static void combinedExample() {
        System.out.println("\n=== 组合使用示例 ===");
        
        // 创建映射规则
        ProductMappingRule rule = new ProductMappingRule("COMB001");
        
        // 1. 处理器链示例
        rule.addFieldMapping(new FieldMapping("$.user.name", "$.profile.displayName")
                .addProcessors(
                    new TrimProcessor(),  // 先去除空格
                    new LowercaseProcessor()  // 再转小写
                ));
        
        // 2. 处理器和聚合策略组合示例
        rule.addFieldMapping(new FieldMapping("$.orders[*].amount", "$.finance.totalAmount")
                .addProcessors(new FormatProcessor())  // 先格式化数字
                .addAggregationStrategies(DefaultAggregationStrategies.SUM));  // 再求和
        
        // 3. 复杂组合示例
        Map<String, String> statusMapping = new HashMap<>();
        statusMapping.put("1", "已发货");
        statusMapping.put("2", "已取消");
        statusMapping.put("3", "处理中");
        
        rule.addFieldMapping(new FieldMapping("$.orders[*].status", "$.order.statusSummary")
                .addProcessors(new MapValueProcessor(statusMapping))  // 先转换状态码
                .addAggregationStrategies(DefaultAggregationStrategies.JOIN));  // 再拼接状态描述
        
        // 源数据
        JSONObject source = JSON.parseObject("{\n" +
                "  \"user\": {\n" +
                "    \"name\": \"  John Doe  \"\n" +
                "  },\n" +
                "  \"orders\": [\n" +
                "    {\"amount\": 100.0, \"status\": \"1\"},\n" +
                "    {\"amount\": 200.0, \"status\": \"2\"},\n" +
                "    {\"amount\": 300.0, \"status\": \"3\"}\n" +
                "  ]\n" +
                "}");
        
        // 目标数据
        JSONObject target = JSON.parseObject("{\n" +
                "  \"profile\": {\n" +
                "    \"displayName\": \"\"\n" +
                "  },\n" +
                "  \"finance\": {\n" +
                "    \"totalAmount\": 0\n" +
                "  },\n" +
                "  \"order\": {\n" +
                "    \"statusSummary\": \"\"\n" +
                "  }\n" +
                "}");
        
        // 执行映射
        MappingRegistry registry = new MappingRegistry();
        registry.register(rule);
        MappingEngine engine = new MappingEngine(registry);
        JSONObject result = engine.map(source, target, "COMB001");
        
        System.out.println("处理结果：");
        System.out.println(result.toJSONString());
    }
} 