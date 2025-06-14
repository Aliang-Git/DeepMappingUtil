package com.aliang.test.example;

import com.aliang.service.ProductMappingService;
import com.alibaba.fastjson.*;

public class ProductMappingExample {
    public static void main(String[] args) {
        // MongoDB连接信息
        String mongoUri = "mongodb://localhost:27017";
        String databaseName = "config";
        String collectionName = "mappingRule";
        
        // 创建服务实例
        ProductMappingService mappingService = new ProductMappingService(mongoUri, databaseName, collectionName);
        
        try {
            // 源数据
            JSONObject sourceData = JSON.parseObject("{\n" +
                    "  \"productCode\": \"X001\",\n" +
                    "  \"user\": {\n" +
                    "    \"name\": \"Alice Johnson\",\n" +
                    "    \"gender\": \"female\",\n" +
                    "    \"isVip\": true,\n" +
                    "    \"registrationTime\": \"2023-08-15T14:30:00Z\",\n" +
                    "    \"addresses\": [\n" +
                    "      {\n" +
                    "        \"type\": \"home\",\n" +
                    "        \"city\": \"Shanghai\",\n" +
                    "        \"postalCode\": \"200000\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"orders\": [\n" +
                    "    {\n" +
                    "      \"id\": \"ORD1001\",\n" +
                    "      \"status\": \"completed\",\n" +
                    "      \"totalAmount\": 123.45,\n" +
                    "      \"paymentDate\": \"2025-06-10T12:34:56Z\",\n" +
                    "      \"items\": [\n" +
                    "        {\n" +
                    "          \"name\": \"Wireless Mouse\",\n" +
                    "          \"price\": 79.99,\n" +
                    "          \"skuCode\": \"mouse_001\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"name\": \"Mechanical Keyboard\",\n" +
                    "          \"price\": 149.99,\n" +
                    "          \"skuCode\": \"keyboard_002\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"ORD1002\",\n" +
                    "      \"status\": \"processing\",\n" +
                    "      \"totalAmount\": 299.994,\n" +
                    "      \"paymentDate\": null,\n" +
                    "      \"items\": []\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");

// 目标数据（带初始值）
            JSONObject targetTemplate = JSON.parseObject("{\n" +
                    "  \"buyer\": {\n" +
                    "    \"fullName\": \"默认买家\"\n" +
                    "  },\n" +
                    "  \"shipping\": {\n" +
                    "    \"defaultCity\": \"Beijing\",\n" +
                    "    \"addressList\": [\n" +
                    "      {\n" +
                    "        \"city\": \"Default City\",\n" +
                    "        \"zipCode\": \"100000\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"trade\": {\n" +
                    "    \"orderId\": \"\",\n" +
                    "    \"total\": 0,\n" +
                    "    \"items\": []\n" +
                    "  },\n" +
                    "  \"finance\": {\n" +
                    "    \"totalAmount\": 0.0,\n" +
                    "    \"orderStatus\": \"未处理\",\n" +
                    "    \"paymentDate\": \"\"\n" +
                    "  },\n" +
                    "  \"inventory\": {\n" +
                    "    \"codes\": []\n" +
                    "  },\n" +
                    "  \"profile\": {\n" +
                    "    \"gender\": \"未知\",\n" +
                    "    \"isVip\": \"否\",\n" +
                    "    \"registeredAt\": \"\"\n" +
                    "  },\n" +
                    "  \"recommendations\": {\n" +
                    "    \"suggestedItems\": []\n" +
                    "  }\n" +
                    "}");
            
            // 执行映射
            JSONObject result = mappingService.processMapping("X001", sourceData, targetTemplate);
            
            // 输出结果
            System.out.println("映射结果：");
            System.out.println(result.toJSONString());
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭服务
            mappingService.close();
        }
    }
} 