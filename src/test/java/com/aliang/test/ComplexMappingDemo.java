package com.aliang.test;

import com.aliang.engine.*;
import com.aliang.factory.*;
import com.aliang.parse.*;
import com.aliang.registry.*;
import com.alibaba.fastjson.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 复杂嵌套数据映射测试用例
 * 测试场景：8层嵌套的复杂JSON数据映射
 * 测试目的：验证系统处理复杂嵌套数据的能力
 */
public class ComplexMappingDemo {
    private static final Logger logger = LoggerFactory.getLogger(ComplexMappingDemo.class);

    @Test
    public void testDeepNestedMapping() {
        // 映射配置
        String configJson = "{\n" +
                "  \"productCode\": \"DEEP001\",\n" +
                "  \"mappings\": [\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.name\",\n" +
                "      \"targetPath\": \"$.result.users[0].fullName\",\n" +
                "      \"processors\": [\"uppercase\", \"prefix:USER_\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.isVip\",\n" +
                "      \"targetPath\": \"$.result.users[0].vipStatus\",\n" +
                "      \"processors\": [\"booleanToYesNo\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.orders[*].amount\",\n" +
                "      \"targetPath\": \"$.result.statistics.totalAmount\",\n" +
                "      \"aggregationStrategies\": [\"sum\"],\n" +
                "      \"processors\": [\"roundTwoDecimal\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.orders[*].status\",\n" +
                "      \"targetPath\": \"$.result.statistics.statusSummary\",\n" +
                "      \"processors\": [\"mapValue:completed=已完成;processing=处理中;cancelled=已取消\"],\n" +
                "      \"aggregationStrategies\": [\"first\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.orders[*].items[*].price\",\n" +
                "      \"targetPath\": \"$.result.statistics.maxItemPrice\",\n" +
                "      \"aggregationStrategies\": [\"max\"],\n" +
                "      \"processors\": [\"roundTwoDecimal\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.orders[*].items[*].quantity\",\n" +
                "      \"targetPath\": \"$.result.statistics.totalQuantity\",\n" +
                "      \"aggregationStrategies\": [\"sum\"],\n" +
                "      \"processors\": [\"roundTwoDecimal\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.orders[*].createTime\",\n" +
                "      \"targetPath\": \"$.result.statistics.firstOrderTime\",\n" +
                "      \"processors\": [\"dateFormat:yyyy年MM月dd日 HH:mm:ss\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.orders[*].createTime\",\n" +
                "      \"targetPath\": \"$.result.statistics.lastOrderTime\",\n" +
                "      \"aggregationStrategies\": [\"last\"],\n" +
                "      \"processors\": [\"dateFormat:yyyy年MM月dd日 HH:mm:ss\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.orders[*].invalidField\",\n" +
                "      \"targetPath\": \"$.result.statistics.invalidData\",\n" +
                "      \"processors\": [\"roundTwoDecimal\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.orders[*].amount\",\n" +
                "      \"targetPath\": \"$.result.statistics.invalidCalculation\",\n" +
                "      \"processors\": [\"invalidProcessor\"],\n" +
                "      \"aggregationStrategies\": [\"invalidStrategy\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.data.level1[*].level2[*].level3[*].level4[*].level5[*].level6[*].level7[*].level8.orders[*].items[*].price\",\n" +
                "      \"targetPath\": \"$.result.statistics.invalidAggregation\",\n" +
                "      \"aggregationStrategies\": [\"sum\", \"invalidStrategy\"]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 测试数据
        String sourceJson = "{\n" +
                "  \"data\": {\n" +
                "    \"level1\": [\n" +
                "      {\n" +
                "        \"level2\": [\n" +
                "          {\n" +
                "            \"level3\": [\n" +
                "              {\n" +
                "                \"level4\": [\n" +
                "                  {\n" +
                "                    \"level5\": [\n" +
                "                      {\n" +
                "                        \"level6\": [\n" +
                "                          {\n" +
                "                            \"level7\": [\n" +
                "                              {\n" +
                "                                \"level8\": {\n" +
                "                                  \"name\": \"张三\",\n" +
                "                                  \"isVip\": true,\n" +
                "                                  \"orders\": [\n" +
                "                                    {\n" +
                "                                      \"amount\": 100.5,\n" +
                "                                      \"status\": \"completed\",\n" +
                "                                      \"items\": [\n" +
                "                                        {\n" +
                "                                          \"price\": 50.25,\n" +
                "                                          \"quantity\": 2\n" +
                "                                        }\n" +
                "                                      ],\n" +
                "                                      \"createTime\": \"2024-03-20 10:30:00\"\n" +
                "                                    },\n" +
                "                                    {\n" +
                "                                      \"amount\": 200.75,\n" +
                "                                      \"status\": \"processing\",\n" +
                "                                      \"items\": [\n" +
                "                                        {\n" +
                "                                          \"price\": 100.375,\n" +
                "                                          \"quantity\": 2\n" +
                "                                        }\n" +
                "                                      ],\n" +
                "                                      \"createTime\": \"2024-03-21 15:45:00\"\n" +
                "                                    }\n" +
                "                                  ]\n" +
                "                                }\n" +
                "                              }\n" +
                "                            ]\n" +
                "                          }\n" +
                "                        ]\n" +
                "                      }\n" +
                "                    ]\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        // 目标模板
        String targetTemplate = "{\n" +
                "  \"result\": {\n" +
                "    \"users\": [\n" +
                "      {\n" +
                "        \"fullName\": null,\n" +
                "        \"vipStatus\": null\n" +
                "      }\n" +
                "    ],\n" +
                "    \"statistics\": {\n" +
                "      \"totalAmount\": null,\n" +
                "      \"statusSummary\": null,\n" +
                "      \"maxItemPrice\": null,\n" +
                "      \"totalQuantity\": null,\n" +
                "      \"firstOrderTime\": null,\n" +
                "      \"lastOrderTime\": null,\n" +
                "      \"invalidData\": null,\n" +
                "      \"invalidCalculation\": null,\n" +
                "      \"invalidAggregation\": null\n" +
                "    }\n" +
                "  }\n" +
                "}";

        // 初始化引擎
        MappingRegistry registry = new MappingRegistry();
        MappingConfigParser parser = new MappingConfigParser(new ProcessorFactory(), registry);
        
        try {
            parser.parseAndRegister(JSONObject.parseObject(configJson), registry);
        } catch (Exception e) {
            logger.error("解析配置时发生错误: {}", e.getMessage());
        }
        
        MappingEngine engine = new MappingEngine(registry);

        // 执行映射
        JSONObject source = JSONObject.parseObject(sourceJson);
        JSONObject target = JSONObject.parseObject(targetTemplate);
        JSONObject result = engine.map(source, target, "DEEP001");
        
        // 获取无效字段信息
        Set<String> invalidFields = parser.getInvalidFields();
        
        // 打印映射结果
        System.out.println("\n=== 映射结果 ===");
        System.out.println(result.toJSONString());
        
        // 打印无效的处理器
        System.out.println("\n=== 无效的处理器 ===");
        Set<String> invalidProcessors = parser.getInvalidProcessors();
        if (invalidProcessors.isEmpty()) {
            System.out.println("没有无效的处理器");
        } else {
            for (String processor : invalidProcessors) {
                System.out.println("- " + processor);
            }
        }
        
        // 打印无效的聚合策略
        System.out.println("\n=== 无效的聚合策略 ===");
        Set<String> invalidStrategies = parser.getInvalidStrategies();
        if (invalidStrategies.isEmpty()) {
            System.out.println("没有无效的聚合策略");
        } else {
            for (String strategy : invalidStrategies) {
                System.out.println("- " + strategy);
            }
        }
        
        // 打印无效的字段
        System.out.println("\n=== 无效的字段 ===");
        if (invalidFields.isEmpty()) {
            System.out.println("没有无效的字段");
        } else {
            for (String field : invalidFields) {
                System.out.println("- " + field);
            }
        }
        
        // 打印错误日志
        System.out.println("\n=== 错误日志 ===");
        System.out.println("1. 无效字段映射：");
        for (String field : invalidFields) {
            System.out.println("   - 字段路径: " + field);
        }
        
        System.out.println("\n2. 处理器错误：");
        for (String processor : invalidProcessors) {
            System.out.println("   - 无效处理器: " + processor);
        }
        
        System.out.println("\n3. 聚合策略错误：");
        for (String strategy : invalidStrategies) {
            System.out.println("   - 无效策略: " + strategy);
        }
        
        System.out.println("\n4. 映射结果分析：");
        System.out.println("   - 无效字段 'invalidField' 的映射结果: " + 
            JSONPath.eval(result, "$.result.statistics.invalidData"));
        System.out.println("   - 使用无效处理器的字段映射结果: " + 
            JSONPath.eval(result, "$.result.statistics.invalidCalculation"));
        System.out.println("   - 使用无效聚合策略的字段映射结果: " + 
            JSONPath.eval(result, "$.result.statistics.invalidAggregation"));
    }
} 