package com.aliang.test;

import com.aliang.service.*;
import com.alibaba.fastjson.*;
import org.junit.*;
import org.slf4j.*;

import static org.junit.Assert.*;

public class DeepNestedMappingTest {
    private static final Logger logger = LoggerFactory.getLogger(DeepNestedMappingTest.class);
    private ProductMappingService mappingService;

    @Before
    public void setUp() {
        mappingService = new ProductMappingService(
                "mongodb://localhost:27017",
                "test_db",
                "mapping_configs"
        ) {
            @Override
            protected JSONObject getMappingConfigFromMongo(String productCode) {
                if ("DEEP01".equals(productCode)) {
                    return JSON.parseObject("{\n" +
                            "  \"code\": \"DEEP01\",\n" +
                            "  \"mappings\": [\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.user.profile.contact.address.street\",\n" +
                            "      \"targetPath\": \"$.streetAddress\",\n" +
                            "      \"processors\": [\"uppercase\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.user.profile.contact.address.zipcode\",\n" +
                            "      \"targetPath\": \"$.postalCode\",\n" +
                            "      \"processors\": [\"prefix:ZIP_\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.user.profile.preferences.notifications.email\",\n" +
                            "      \"targetPath\": \"$.emailNotif\",\n" +
                            "      \"processors\": [\"booleantoyesno\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.order.details.items[0].product.info.name\",\n" +
                            "      \"targetPath\": \"$.firstProductName\",\n" +
                            "      \"processors\": [\"uppercase\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.order.details.items[0].product.pricing.unitPrice\",\n" +
                            "      \"targetPath\": \"$.firstProductUnitPrice\",\n" +
                            "      \"processors\": [\"roundtwodecimal\", \"prefix:$\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.order.details.totals.amounts.total\",\n" +
                            "      \"targetPath\": \"$.orderTotal\",\n" +
                            "      \"processors\": [\"roundtwodecimal\", \"prefix:$\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.order.details.items[*].product.pricing.quantity\",\n" +
                            "      \"targetPath\": \"$.totalItems\",\n" +
                            "      \"aggregationStrategies\": [\"sum\"],\n" +
                            "      \"processors\": [\"tointeger\"]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}");
                }
                return null;
            }
        };
    }

    @Test
    public void testDeepNestedMapping() {
        logger.info("测试深层嵌套 JSON 的映射转换");

        String sourceJson = "{\n" +
                "  \"user\": {\n" +
                "    \"profile\": {\n" +
                "      \"contact\": {\n" +
                "        \"address\": {\n" +
                "          \"street\": \"Sunset Blvd\",\n" +
                "          \"zipcode\": \"90001\"\n" +
                "        },\n" +
                "        \"phone\": \"123456789\"\n" +
                "      },\n" +
                "      \"preferences\": {\n" +
                "        \"notifications\": {\n" +
                "          \"email\": true,\n" +
                "          \"sms\": false\n" +
                "        },\n" +
                "        \"theme\": \"dark\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"order\": {\n" +
                "    \"details\": {\n" +
                "      \"items\": [\n" +
                "        {\n" +
                "          \"product\": {\n" +
                "            \"info\": {\n" +
                "              \"id\": \"p001\",\n" +
                "              \"name\": \"Laptop\"\n" +
                "            },\n" +
                "            \"pricing\": {\n" +
                "              \"unitPrice\": 999.49,\n" +
                "              \"quantity\": 2\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"totals\": {\n" +
                "        \"amounts\": {\n" +
                "          \"total\": 1998.98\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        JSONObject source = JSON.parseObject(sourceJson);
        JSONObject targetTemplate = JSON.parseObject("{}");
        JSONObject result = mappingService.processMapping("DEEP01", source, targetTemplate);

        logger.info("映射结果: {}", JSON.toJSONString(result, true));

        assertNotNull(result);
        assertEquals("SUNSET BLVD", result.getString("streetAddress"));
        assertEquals("ZIP_90001", result.getString("postalCode"));
        assertEquals("是", result.getString("emailNotif"));
        assertEquals("LAPTOP", result.getString("firstProductName"));
        assertEquals("$999.49", result.getString("firstProductUnitPrice"));
        assertEquals("$1998.98", result.getString("orderTotal"));
        assertEquals(Integer.valueOf(2), result.getInteger("totalItems"));
    }
} 