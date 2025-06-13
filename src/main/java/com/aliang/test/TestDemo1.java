package com.aliang.test;

import com.aliang.Engine.*;
import com.aliang.Foctory.*;
import com.aliang.mapping.*;
import com.aliang.parse.*;
import com.aliang.processor.impl.*;
import com.aliang.registry.*;
import com.aliang.rule.*;
import com.alibaba.fastjson.*;

public class TestDemo1 {
    public static void main(String[] args) {
        TestDemo1 demo1 = new TestDemo1();
//        demo1.test01();
//        demo1.test02();
//        demo1.test03();
        demo1.test04();
    }


    public void test01() {
        /**
         * 测试数据存在于数据库中
         * {
         *   "A001": [
         *     {
         *       "sourcePath": "$.customer.name",
         *       "targetPath": "$.buyer.fullName",
         *       "processors": ["uppercase"]
         *     },
         *     {
         *       "sourcePath": "$.order[0].amount",
         *       "targetPath": "$.trade.total",
         *       "processors": ["multiplyByTen"]
         *     }
         *   ],
         *   "B002": [
         *     {
         *       "sourcePath": "$.user.firstName",
         *       "targetPath": "$.person.firstName"
         *     },
         *     {
         *       "sourcePath": "$.payment.date",
         *       "targetPath": "$.transaction.date",
         *       "processors": ["dateFormat:yyyy-MM-dd"]
         *     }
         *   ]
         * }
         */
        // 构建映射规则
        MappingRegistry registry = new MappingRegistry();

        ProductMappingRule a001Rule = new ProductMappingRule("A001")
                .addFieldMapping(new FieldMapping("$.customer.name", "$.buyer.fullName")
                        .addProcessors(new UppercaseProcessor()))
                .addFieldMapping(new FieldMapping("$.order.no", "$.trade.orderId"))
                .addFieldMapping(new FieldMapping("$.order.amount", "$.trade.total"));

        registry.register(a001Rule);

        // 初始化引擎
        MappingEngine engine = new MappingEngine(registry);

        // 源数据
        JSONObject json1 = JSON.parseObject("{\n" +
                "  \"productCode\": \"A001\",\n" +
                "  \"customer\": {\n" +
                "    \"name\": \"张三\"\n" +
                "  },\n" +
                "  \"order\": [{\n" +
                "    \"no\": \"20250613XYZ\",\n" +
                "    \"amount\": 999.99\n" +
                "  }," +
                "{\n" +
                "    \"no\": \"20250613XYZ\",\n" +
                "    \"amount\": 999.98\n" +
                "  }\n" +
                "]\n" +
                "}");
        System.out.println("转换前-原json1:" + json1);
        // 目标数据
        JSONObject json2 = JSON.parseObject("{\n" +
                "  \"buyer\": { \"fullName\": \"\" },\n" +
                "  \"trade\": {\n" +
                "    \"orderId\": \"\",\n" +
                "    \"total\": 0\n" +
                "  }\n" +
                "}");
        System.out.println("转换前-原json2:" + json2);
        // 执行映射
        JSONObject result = engine.map(json1, json2, "A001");

        System.out.println(result.toJSONString());
    }

    public void test02() {
        long startTime = System.currentTimeMillis();
        // 示例 JSON 映射配置
        String configJson = "{\n" +
                "  \"A001\": [\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.customer.name\",\n" +
                "      \"targetPath\": \"$.buyer.fullName\",\n" +
                "      \"processors\": [\"uppercase\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.order[*].amount\",\n" +
                "      \"targetPath\": \"$.trade.total\",\n" +
                "      \"processors\": [\"multiplyByTen\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.order[*].no\",\n" +
                "      \"targetPath\": \"$.trade.orderId\",\n" +
                "      \"processors\": [\"multiplyByTen\"]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"B002\": [\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.user.firstName\",\n" +
                "      \"targetPath\": \"$.person.firstName\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.payment.date\",\n" +
                "      \"targetPath\": \"$.transaction.date\",\n" +
                "      \"processors\": [\"dateFormat:yyyy-MM-dd\"]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"C003\": [\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.user.nickName\",\n" +
                "      \"targetPath\": \"$.customer.nickname\",\n" +
                "      \"processors\": [\"lowercase\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.order.status\",\n" +
                "      \"targetPath\": \"$.trade.status\",\n" +
                "      \"processors\": [\"statusToChinese\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.payment.amount\",\n" +
                "      \"targetPath\": \"$.finance.total\",\n" +
                "      \"processors\": [\"roundTwoDecimal\"]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 解析 JSON 配置
        JSONObject config = JSON.parseObject(configJson);

        // 初始化注册中心
        MappingRegistry registry = new MappingRegistry();

        // 初始化解析器并注册规则
        MappingConfigParser parser = new MappingConfigParser(new ProcessorFactory());
        parser.parseAndRegister(config, registry);

        // 使用引擎进行映射
        MappingEngine engine = new MappingEngine(registry);

        // 源数据
        JSONObject json1 = JSON.parseObject("{\n" +
                "  \"productCode\": \"A001\",\n" +
                "  \"customer\": [{\n" +
                "    \"name\": \"张三\"\n" +
                "  }],\n" +
                "  \"order\": [" +
                "{\n" +
                "    \"no\": \"202506758678YZ\",\n" +
                "    \"amount\": 999.99\n" +
                "}," +
                "{\n" +
                "    \"no\": \"20250756758YZ\",\n" +
                "    \"amount\": 99888.99\n" +
                "  }" +
                "]\n" +
                "}");

        // 目标数据
        JSONObject json2 = JSON.parseObject("{\n" +
                "  \"buyer\": { \"fullName\": \"\" },\n" +
                "  \"trade\": {\n" +
                "    \"orderId\": \"\",\n" +
                "    \"total\": 0\n" +
                "  }\n" +
                "}");

        // 执行映射
        JSONObject A001 = engine.map(json1, json2, "A001");
        JSONObject B002 = engine.map(json1, json2, "B002");
        JSONObject C003 = engine.map(json1, json2, "C003");

        System.out.println(A001.toJSONString());
        System.out.println(B002.toJSONString());
        System.out.println(C003.toJSONString());


        System.out.println("整体的转换耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }

    public void test03() {
        long startTime = System.currentTimeMillis();
        String configJson = "{\n" +
                "  \"X001\": [\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.user.name\",\n" +
                "      \"targetPath\": \"$.customer.fullName\",\n" +
                "      \"processors\": [\"uppercase\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.user.addresses[0].city\",\n" +
                "      \"targetPath\": \"$.shipping.defaultCity\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.orders[*].items[*].name\",\n" +
                "      \"targetPath\": \"$.cart.items[*].productName\",\n" +
                "      \"processors\": [\"lowercase\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.orders[*].totalAmount\",\n" +
                "      \"targetPath\": \"$.finance.totalAmount\",\n" +
                "      \"processors\": [\"multiplyByTen\", \"roundTwoDecimal\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.orders[*].status\",\n" +
                "      \"targetPath\": \"$.finance.orderStatus\",\n" +
                "      \"processors\": [\"statusToChinese\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.orders[*].paymentDate\",\n" +
                "      \"targetPath\": \"$.finance.paymentDate\",\n" +
                "      \"processors\": [\"dateFormat:yyyy-MM-dd HH:mm:ss\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.orders[*].items[*].price\",\n" +
                "      \"targetPath\": \"$.cart.items[*].priceAfterDiscount\",\n" +
                "      \"processors\": [\"discount:0.95\", \"roundTwoDecimal\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.orders[*].items[*].skuCode\",\n" +
                "      \"targetPath\": \"$.inventory.codes[*]\",\n" +
                "      \"processors\": [\"prefix:S_\", \"uppercase\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.user.gender\",\n" +
                "      \"targetPath\": \"$.profile.gender\",\n" +
                "      \"processors\": [\"mapValue:male=男,female=女,other=未知\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.user.isVip\",\n" +
                "      \"targetPath\": \"$.profile.isVip\",\n" +
                "      \"processors\": [\"booleanToYesNo\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.user.registrationTime\",\n" +
                "      \"targetPath\": \"$.profile.registeredAt\",\n" +
                "      \"processors\": [\"dateFormat:yyyy年MM月dd日\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"sourcePath\": \"$.orders[?(@.status == 'completed')].items[*].name\",\n" +
                "      \"targetPath\": \"$.recommendations.suggestedItems[*]\",\n" +
                "      \"processors\": [\"capitalize\"]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        // 解析 JSON 配置
        JSONObject config = JSON.parseObject(configJson);

        // 初始化注册中心
        MappingRegistry registry = new MappingRegistry();

        // 初始化解析器并注册规则
        MappingConfigParser parser = new MappingConfigParser(new ProcessorFactory());
        parser.parseAndRegister(config, registry);

        // 使用引擎进行映射
        MappingEngine engine = new MappingEngine(registry);

        // 源数据
        JSONObject json1 = JSON.parseObject("{\n" +
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
        JSONObject json2 = JSON.parseObject("{\n" +
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
        JSONObject result = engine.map(json1, json2, "X001");

        // 输出结果
        System.out.println(result.toJSONString());
        System.out.println("整体的转换耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }

    public void test04() {
        long startTime = System.currentTimeMillis();
        String configJson = "{\n" +
                "  \"X001\": [\n" +
                "    {\n" +
                    "\"sourcePath\": \"$.orders[*].status\",\n" +
                    "  \"targetPath\": \"$.order.statusLabel\",\n" +
                    "  \"processors\": [\"mapValue:1=已发货;2=已取消;3=处理中\"]" +
                "    }\n" +
                "  ]\n" +
                "}";
        // 解析 JSON 配置
        JSONObject config = JSON.parseObject(configJson);

        // 初始化注册中心
        MappingRegistry registry = new MappingRegistry();

        // 初始化解析器并注册规则
        MappingConfigParser parser = new MappingConfigParser(new ProcessorFactory());
        parser.parseAndRegister(config, registry);

        // 使用引擎进行映射
        MappingEngine engine = new MappingEngine(registry);

        // 源数据
        JSONObject json1 = JSON.parseObject("{\n" +
                "  \"productCode\": \"X001\",\n" +
                "  \"orders\": [\n" +
                "    {\n" +
                "      \"id\": \"ORD1001\",\n" +
                "      \"status\": \"1\",\n" +
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
                "      \"status\": \"2\",\n" +
                "      \"totalAmount\": 299.994,\n" +
                "      \"paymentDate\": null,\n" +
                "      \"items\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}");

// 目标数据（带初始值）
        JSONObject json2 = JSON.parseObject("{\n" +
                "  \"order\": {\n" +
                "    \"statusLabel\": \"默认买家\"\n" +
                "  }\n" +
                "}");

        // 执行映射
        JSONObject result = engine.map(json1, json2, "X001");

        // 输出结果
        System.out.println(result.toJSONString());
        System.out.println("整体的转换耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }
}
