package com.aliang.test;

import com.aliang.service.*;
import com.alibaba.fastjson.*;
import org.junit.*;
import org.junit.runner.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * 批量映射测试
 * 从本地 MongoDB(test_db.mapping_configs) 读取 DEEP01-DEEP20 的映射配置
 * 对同一份深层嵌套源 JSON 执行映射并打印结果。
 * <p>
 * 运行前需确保数据库已存在相应的 20 条配置文档。
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiDeepMappingTest {
    private static final Logger logger = LoggerFactory.getLogger(MultiDeepMappingTest.class);
    @Autowired
    private ProductMappingService mappingService;

    /**
     * 构造一个满足所有 sourcePath 的深层嵌套源 JSON。
     */
    private JSONObject buildSourceJson() {
        String json = "{\n" +
                "  \"source\": {\n" +
                "    \"user\": {\n" +
                "      \"profile\": {\n" +
                "        \"contact\": {\n" +
                "          \"address\": {\n" +
                "            \"street\": \"123 main st\",\n" +
                "            \"zipcode\": \"10001\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"preferences\": {\n" +
                "          \"notifications\": {\n" +
                "            \"email\": true\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"order\": {\n" +
                "      \"details\": {\n" +
                "        \"items\": [\n" +
                "          {\n" +
                "            \"product\": {\n" +
                "              \"info\": {\n" +
                "                \"name\": \"apple\"\n" +
                "              },\n" +
                "              \"pricing\": {\n" +
                "                \"unitPrice\": 9.99,\n" +
                "                \"quantity\": 2\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"product\": {\n" +
                "              \"info\": {\n" +
                "                \"name\": \"banana\"\n" +
                "              },\n" +
                "              \"pricing\": {\n" +
                "                \"unitPrice\": 5.55,\n" +
                "                \"quantity\": 3\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        ],\n" +
                "        \"totals\": {\n" +
                "          \"amounts\": {\n" +
                "            \"total\": 25.53\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"targetTemplate\": {\n" +
                "    \"streetAddress\": \"\",\n" +
                "    \"postalCode\": \"\",\n" +
                "    \"emailNotif\": \"\",\n" +
                "    \"firstProductName\": [\n" +
                "      {\n" +
                "        \"name\": [\n" +
                "          {\n" +
                "            \"first\": \"\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ],\n" +
                "    \"firstProductUnitPrice\": \"\",\n" +
                "    \"orderTotal\": \"\",\n" +
                "    \"totalItems\": 0\n" +
                "  }\n" +
                "}";
        return JSON.parseObject(json);
    }

    @Test
    public void testDeepMappings() {
        long start = System.currentTimeMillis();
        List<String> codes = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            codes.add(String.format("DEEP%02d", i));
        }
        JSONObject source = buildSourceJson();
        for (String code : codes) {
            JSONObject targetTemplate = source.getJSONObject("targetTemplate");
            try {
                Map<String, Object> resultMap = mappingService.processMapping(code, source, targetTemplate);
                assertNotNull("映射结果不能为空 - " + code, resultMap);
                logger.info("{} -> 映射结果:\n{}", code, JSON.toJSONString(resultMap, true));
            } catch (Exception e) {
                fail("处理 " + code + " 失败: " + e.getMessage());
            }
        }
        System.out.println("当前耗时: " + (System.currentTimeMillis() - start));
    }
} 