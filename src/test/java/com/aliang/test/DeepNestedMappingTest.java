package com.aliang.test;

import com.aliang.service.*;
import com.alibaba.fastjson.*;
import org.junit.*;
import org.junit.runner.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeepNestedMappingTest {
    private static final Logger logger = LoggerFactory.getLogger(DeepNestedMappingTest.class);

    @Autowired
    private ProductMappingService mappingService;

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
        Map<String, Object> resultMap = mappingService.processMapping("DEEP01", source, targetTemplate);
        JSONObject result = new JSONObject(resultMap);

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

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ProductMappingService mappingService() {
            return new ProductMappingService() {
                @Override
                protected JSONObject getMappingConfigFromMongo(String productCode) {
                    if ("DEEP01".equals(productCode)) {
                        return JSON.parseObject("{\n" +
                                "  \"code\": \"DEEP01\",\n" +
                                "  \"mappings\": [\n" +
                                "    {\"sourcePath\": \"$.user.profile.contact.address.street\", \"targetPath\": \"$.streetAddress\", \"processors\": [\"uppercase\"]},\n" +
                                "    {\"sourcePath\": \"$.user.profile.contact.address.zipcode\", \"targetPath\": \"$.postalCode\", \"processors\": [\"prefix:ZIP_\"]},\n" +
                                "    {\"sourcePath\": \"$.user.profile.preferences.notifications.email\", \"targetPath\": \"$.emailNotif\", \"processors\": [\"booleantoyesno\"]},\n" +
                                "    {\"sourcePath\": \"$.order.details.items[0].product.info.name\", \"targetPath\": \"$.firstProductName\", \"processors\": [\"uppercase\"]},\n" +
                                "    {\"sourcePath\": \"$.order.details.items[0].product.pricing.unitPrice\", \"targetPath\": \"$.firstProductUnitPrice\", \"processors\": [\"roundtwodecimal\", \"prefix:$\"]},\n" +
                                "    {\"sourcePath\": \"$.order.details.totals.amounts.total\", \"targetPath\": \"$.orderTotal\", \"processors\": [\"roundtwodecimal\", \"prefix:$\"]},\n" +
                                "    {\"sourcePath\": \"$.order.details.items[*].product.pricing.quantity\", \"targetPath\": \"$.totalItems\", \"aggregationStrategies\": [\"sum\"], \"processors\": [\"tointeger\"]}\n" +
                                "  ]\n" +
                                "}");
                    }
                    return null;
                }
            };
        }
    }
} 