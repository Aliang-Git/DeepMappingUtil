package com.aliang.test;

import com.aliang.service.*;
import com.alibaba.fastjson.*;
import org.junit.*;
import org.slf4j.*;

import static org.junit.Assert.*;

public class ComplexOrderMappingTest {
    private static final Logger logger = LoggerFactory.getLogger(ComplexOrderMappingTest.class);
    private OrderMappingService mappingService;

    @Before
    public void setUp() {
        // 使用内存MongoDB进行测试
        mappingService = new OrderMappingService(
                "mongodb://localhost:27017",
                "test_db",
                "mapping_configs"
        ) {
            // 重写getMappingConfigFromMongo方法，使用JSON模拟配置
            @Override
            protected JSONObject getMappingConfigFromMongo(String orderCode) {
                if ("ORD001".equals(orderCode)) {
                    return JSON.parseObject("{\n" +
                            "  \"code\": \"ORD001\",\n" +
                            "  \"mappings\": {\n" +
                            "    \"displayCode\": {\n" +
                            "      \"sourcePath\": \"$.orderInfo.code\",\n" +
                            "      \"targetPath\": \"displayCode\",\n" +
                            "      \"processors\": [\"prefix:ORDER_\", \"uppercase\"]\n" +
                            "    },\n" +
                            "    \"orderStatus\": {\n" +
                            "      \"sourcePath\": \"$.orderInfo.status\",\n" +
                            "      \"targetPath\": \"orderStatus\",\n" +
                            "      \"processors\": [\"mapvalue:pending=待处理;processing=处理中;completed=已完成;cancelled=已取消\"]\n" +
                            "    },\n" +
                            "    \"orderTime\": {\n" +
                            "      \"sourcePath\": \"$.orderInfo.createTime\",\n" +
                            "      \"targetPath\": \"orderTime\",\n" +
                            "      \"processors\": [\"dateformat:yyyy年MM月dd日 HH时mm分\"]\n" +
                            "    },\n" +
                            "    \"payAmount\": {\n" +
                            "      \"sourcePath\": \"$.paymentInfo.amount\",\n" +
                            "      \"targetPath\": \"payAmount\",\n" +
                            "      \"processors\": [\"roundtwodecimal:2\", \"prefix:¥\"]\n" +
                            "    },\n" +
                            "    \"discountRates\": {\n" +
                            "      \"sourcePath\": \"$.paymentInfo.discounts[*].rate\",\n" +
                            "      \"targetPath\": \"discountRates\",\n" +
                            "      \"aggregationStrategies\": [\"join:keepArrayFormat=true\"],\n" +
                            "      \"processors\": [\"multiplybyten:100\", \"roundtwodecimal:1\", \"suffix:%\"]\n" +
                            "    },\n" +
                            "    \"totalDiscount\": {\n" +
                            "      \"sourcePath\": \"$.paymentInfo.discounts[*].amount\",\n" +
                            "      \"targetPath\": \"totalDiscount\",\n" +
                            "      \"aggregationStrategies\": [\"sum\"],\n" +
                            "      \"processors\": [\"roundtwodecimal:2\", \"prefix:节省:¥\"]\n" +
                            "    },\n" +
                            "    \"totalItems\": {\n" +
                            "      \"sourcePath\": \"$.items[*].quantity\",\n" +
                            "      \"targetPath\": \"totalItems\",\n" +
                            "      \"aggregationStrategies\": [\"sum\"],\n" +
                            "      \"processors\": [\"format:共%d件商品\"]\n" +
                            "    },\n" +
                            "    \"itemList\": {\n" +
                            "      \"sourcePath\": \"$.items[*].name\",\n" +
                            "      \"targetPath\": \"itemList\",\n" +
                            "      \"aggregationStrategies\": [\"join:delimiter=, ;keepArrayFormat=true\"],\n" +
                            "      \"processors\": [\"substring:0,5\", \"suffix:...\"]\n" +
                            "    },\n" +
                            "    \"averageRating\": {\n" +
                            "      \"sourcePath\": \"$.items[*].ratings.score\",\n" +
                            "      \"targetPath\": \"averageRating\",\n" +
                            "      \"aggregationStrategies\": [\"average\"],\n" +
                            "      \"processors\": [\"roundtwodecimal:1\", \"suffix:星\"]\n" +
                            "    },\n" +
                            "    \"ratingTags\": {\n" +
                            "      \"sourcePath\": \"$.items[*].ratings.tags\",\n" +
                            "      \"targetPath\": \"ratingTags\",\n" +
                            "      \"aggregationStrategies\": [\"join\"],\n" +
                            "      \"processors\": [\"prefix:#\"]\n" +
                            "    },\n" +
                            "    \"shippingAddress\": {\n" +
                            "      \"sourcePath\": \"$.deliveryInfo.address\",\n" +
                            "      \"targetPath\": \"shippingAddress\",\n" +
                            "      \"processors\": [\"substring:0,50\", \"suffix:...\"]\n" +
                            "    },\n" +
                            "    \"deliveryDistance\": {\n" +
                            "      \"sourcePath\": \"$.deliveryInfo.distance\",\n" +
                            "      \"targetPath\": \"deliveryDistance\",\n" +
                            "      \"processors\": [\"roundtwodecimal:1\", \"format:%.1fkm\"]\n" +
                            "    },\n" +
                            "    \"deliveryTime\": {\n" +
                            "      \"sourcePath\": \"$.deliveryInfo.estimatedTime\",\n" +
                            "      \"targetPath\": \"deliveryTime\",\n" +
                            "      \"processors\": [\"multiplybyten:1.5\", \"roundtwodecimal:0\", \"format:预计%d分钟送达\"]\n" +
                            "    },\n" +
                            "    \"vipBadge\": {\n" +
                            "      \"sourcePath\": \"$.customerInfo.vipLevel\",\n" +
                            "      \"targetPath\": \"vipBadge\",\n" +
                            "      \"processors\": [\"mapvalue:0=普通会员;1=白银会员;2=黄金会员;3=钻石会员\", \"prefix:【\", \"suffix:】\"]\n" +
                            "    },\n" +
                            "    \"pointsInfo\": {\n" +
                            "      \"sourcePath\": \"$.customerInfo.points\",\n" +
                            "      \"targetPath\": \"pointsInfo\",\n" +
                            "      \"processors\": [\"format:可获得%d积分\"]\n" +
                            "    }\n" +
                            "  }\n" +
                            "}");
                }
                return null;
            }
        };
    }

    @Test
    public void testAllOrders() {
        testOrder1();
        // 后续添加其他订单的测试方法
    }

    private void testOrder1() {
        logger.info("测试订单1的映射转换");

        // 源数据
        String sourceJson = "{\n" +
                "  \"orderInfo\": {\n" +
                "    \"code\": \"ord001\",\n" +
                "    \"status\": \"processing\",\n" +
                "    \"createTime\": \"2024-03-21 14:30:00\",\n" +
                "    \"updateTime\": \"2024-03-21 14:35:00\"\n" +
                "  },\n" +
                "  \"paymentInfo\": {\n" +
                "    \"amount\": 299.99,\n" +
                "    \"currency\": \"CNY\",\n" +
                "    \"method\": \"Alipay\",\n" +
                "    \"discounts\": [\n" +
                "      {\n" +
                "        \"type\": \"VIP\",\n" +
                "        \"rate\": 0.95,\n" +
                "        \"amount\": 15.00\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"COUPON\",\n" +
                "        \"rate\": 0.90,\n" +
                "        \"amount\": 30.00\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"SEASONAL\",\n" +
                "        \"rate\": 0.88,\n" +
                "        \"amount\": 35.99\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"id\": \"ITEM001\",\n" +
                "      \"name\": \"超级豪华至尊汉堡\",\n" +
                "      \"quantity\": 2,\n" +
                "      \"price\": 88.00,\n" +
                "      \"ratings\": {\n" +
                "        \"score\": 4.8,\n" +
                "        \"tags\": [\"美味\", \"份量足\", \"速度快\"]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"ITEM002\",\n" +
                "      \"name\": \"双层芝士牛肉堡\",\n" +
                "      \"quantity\": 1,\n" +
                "      \"price\": 68.00,\n" +
                "      \"ratings\": {\n" +
                "        \"score\": 4.5,\n" +
                "        \"tags\": [\"口感好\", \"服务赞\"]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"ITEM003\",\n" +
                "      \"name\": \"超大份薯条\",\n" +
                "      \"quantity\": 3,\n" +
                "      \"price\": 18.00,\n" +
                "      \"ratings\": {\n" +
                "        \"score\": 4.2,\n" +
                "        \"tags\": [\"酥脆\", \"够咸\"]\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"deliveryInfo\": {\n" +
                "    \"address\": \"北京市朝阳区三里屯SOHO A座25层2501室\",\n" +
                "    \"distance\": 3.75,\n" +
                "    \"estimatedTime\": 30,\n" +
                "    \"courier\": {\n" +
                "      \"name\": \"张师傅\",\n" +
                "      \"phone\": \"13800138000\",\n" +
                "      \"rating\": 4.9\n" +
                "    }\n" +
                "  },\n" +
                "  \"customerInfo\": {\n" +
                "    \"id\": \"CUST001\",\n" +
                "    \"name\": \"李先生\",\n" +
                "    \"phone\": \"13900139000\",\n" +
                "    \"vipLevel\": 2,\n" +
                "    \"points\": 500\n" +
                "  }\n" +
                "}";

        // 目标模板
        String targetTemplateJson = "{}";

        // 执行映射
        JSONObject source = JSON.parseObject(sourceJson);
        JSONObject targetTemplate = JSON.parseObject(targetTemplateJson);
        JSONObject result = mappingService.processMapping("ORD001", source, targetTemplate);

        // 验证结果
        logger.info("映射结果: {}", JSON.toJSONString(result, true));

        // 添加断言
        assertNotNull(result);
        assertEquals("ORDER_ORD001", result.getString("displayCode"));
        assertEquals("处理中", result.getString("orderStatus"));
        assertEquals("2024年03月21日 14时30分", result.getString("orderTime"));
        assertEquals("¥299.99", result.getString("payAmount"));
        assertEquals("[95.0%,90.0%,88.0%]", result.getString("discountRates"));
        assertEquals("节省:¥80.99", result.getString("totalDiscount"));
        assertEquals("共6件商品", result.getString("totalItems"));
        assertEquals("[超级豪华至...,双层芝士牛...,超大份薯条...]", result.getString("itemList"));
        assertEquals("4.5星", result.getString("averageRating"));
        assertEquals("#美味,#份量足,#速度快,#口感好,#服务赞,#酥脆,#够咸", result.getString("ratingTags"));
        assertEquals("北京市朝阳区三里屯SOHO A座25层2501室...", result.getString("shippingAddress"));
        assertEquals("3.8km", result.getString("deliveryDistance"));
        assertEquals("预计45分钟送达", result.getString("deliveryTime"));
        assertEquals("【黄金会员】", result.getString("vipBadge"));
        assertEquals("可获得500积分", result.getString("pointsInfo"));
    }
} 