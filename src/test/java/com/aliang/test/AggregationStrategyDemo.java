package com.aliang.test;

import com.aliang.engine.*;
import com.aliang.factory.*;
import com.aliang.parse.*;
import com.aliang.registry.*;
import com.alibaba.fastjson.*;

public class AggregationStrategyDemo {
    public static void main(String[] args) {
        // 测试用例1：基本聚合策略测试
//        testBasicAggregation();
        
        // 测试用例2：复杂聚合策略测试
//        testComplexAggregation();
        
        // 测试用例3：多字段聚合测试
        testMultiFieldAggregation();
    }

    private static void testBasicAggregation() {
        System.out.println("\n=== 测试用例1：基本聚合策略测试 ===");
        
        // 创建配置
        String configJson = "{\n" +
                "  \"productCode\": \"A001\",\n" +
                "  \"mappings\": [\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.orders[*].amount\",\n" +
                "      \"targetPath\": \"$.trade.total\",\n" +
                "      \"aggregationStrategies\": [\"sum\"]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 初始化组件
        MappingRegistry registry = new MappingRegistry();
        MappingConfigParser parser = new MappingConfigParser(new ProcessorFactory(), registry);
        parser.parseAndRegister(JSON.parseObject(configJson), registry);
        MappingEngine engine = new MappingEngine(registry);

        // 源数据
        JSONObject source = JSON.parseObject("{\n" +
                "  \"orders\": [\n" +
                "    {\"amount\": 100},\n" +
                "    {\"amount\": 200},\n" +
                "    {\"amount\": 300}\n" +
                "  ]\n" +
                "}");

        // 目标数据
        JSONObject target = JSON.parseObject("{\n" +
                "  \"trade\": {\n" +
                "    \"total\": 0\n" +
                "  }\n" +
                "}");

        // 执行映射
        JSONObject result = engine.map(source, target, "A001");
        System.out.println("源数据：" + source.toJSONString());
        System.out.println("目标数据：" + target.toJSONString());
        System.out.println("映射结果：" + result.toJSONString());
    }

    private static void testComplexAggregation() {
        System.out.println("\n=== 测试用例2：复杂聚合策略测试 ===");
        
        // 创建配置
        String configJson = "{\n" +
                "  \"productCode\": \"B002\",\n" +
                "  \"mappings\": [\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.items[*].price\",\n" +
                "      \"targetPath\": \"$.summary.maxPrice\",\n" +
                "      \"aggregationStrategies\": [\"max\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.items[*].price\",\n" +
                "      \"targetPath\": \"$.summary.minPrice\",\n" +
                "      \"aggregationStrategies\": [\"min\"]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 初始化组件
        MappingRegistry registry = new MappingRegistry();
        MappingConfigParser parser = new MappingConfigParser(new ProcessorFactory(), registry);
        parser.parseAndRegister(JSON.parseObject(configJson), registry);
        MappingEngine engine = new MappingEngine(registry);

        // 源数据
        JSONObject source = JSON.parseObject("{\n" +
                "  \"items\": [\n" +
                "    {\"price\": 150.5},\n" +
                "    {\"price\": 75.25},\n" +
                "    {\"price\": 200.0}\n" +
                "  ]\n" +
                "}");

        // 目标数据
        JSONObject target = JSON.parseObject("{\n" +
                "  \"summary\": {\n" +
                "    \"maxPrice\": 0,\n" +
                "    \"minPrice\": 0\n" +
                "  }\n" +
                "}");

        // 执行映射
        JSONObject result = engine.map(source, target, "B002");
        System.out.println("源数据：" + source.toJSONString());
        System.out.println("目标数据：" + target.toJSONString());
        System.out.println("映射结果：" + result.toJSONString());
    }

    private static void testMultiFieldAggregation() {
        System.out.println("\n=== 测试用例3：多字段聚合测试 ===");
        
        // 创建配置
        String configJson = "{\n" +
                "  \"productCode\": \"C003\",\n" +
                "  \"mappings\": [\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.transactions[*].amount\",\n" +
                "      \"targetPath\": \"$.statistics.totalAmount\",\n" +
                "      \"aggregationStrategies\": [\"sum\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.transactions[*].timestamp\",\n" +
                "      \"targetPath\": \"$.statistics.firstTransaction\",\n" +
                "      \"aggregationStrategies\": [\"first\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.transactions[*].timestamp\",\n" +
                "      \"targetPath\": \"$.statistics.lastTransaction\",\n" +
                "      \"aggregationStrategies\": [\"last\"]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 初始化组件
        MappingRegistry registry = new MappingRegistry();
        MappingConfigParser parser = new MappingConfigParser(new ProcessorFactory(), registry);
        parser.parseAndRegister(JSON.parseObject(configJson), registry);
        MappingEngine engine = new MappingEngine(registry);

        // 源数据
        JSONObject source = JSON.parseObject("{\n" +
                "  \"transactions\": [\n" +
                "    {\"amount\": 100, \"timestamp\": \"2024-01-01\"},\n" +
                "    {\"amount\": 200, \"timestamp\": \"2024-01-02\"},\n" +
                "    {\"amount\": 300, \"timestamp\": \"2024-01-03\"}\n" +
                "  ]\n" +
                "}");

        // 目标数据
        JSONObject target = JSON.parseObject("{\n" +
                "  \"statistics\": {\n" +
                "    \"totalAmount\": 0,\n" +
                "    \"firstTransaction\": \"\",\n" +
                "    \"lastTransaction\": \"\"\n" +
                "  }\n" +
                "}");

        // 执行映射
        JSONObject result = engine.map(source, target, "C003");
        System.out.println("源数据：" + source.toJSONString());
        System.out.println("目标数据：" + target.toJSONString());
        System.out.println("映射结果：" + result.toJSONString());
    }
} 