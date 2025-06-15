package com.aliang.test;

import com.aliang.service.*;
import com.alibaba.fastjson.*;
import org.junit.*;
import org.slf4j.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * 批量映射测试
 * 从本地 MongoDB(test_db.mapping_configs) 读取 DEEP01-DEEP20 的映射配置
 * 对同一份深层嵌套源 JSON 执行映射并打印结果。
 * <p>
 * 运行前需确保数据库已存在相应的 20 条配置文档。
 */
public class MultiDeepMappingTest {
    private static final Logger logger = LoggerFactory.getLogger(MultiDeepMappingTest.class);
    private ProductMappingService mappingService;

    @Before
    public void setUp() {
        /*  连接本地 MongoDB。若端口/地址不同请自行修改。 */
        mappingService = new ProductMappingService(
                "mongodb://localhost:27017",
                "config",
                "mapping_rules");
    }

    /**
     * 构造一个满足所有 sourcePath 的深层嵌套源 JSON。
     */
    private JSONObject buildSourceJson() {
        String json = "{\n" +
                "  \"user\": {\n" +
                "    \"profile\": {\n" +
                "      \"contact\": {\n" +
                "        \"address\": {\n" +
                "          \"street\": \"Sunset Blvd\",\n" +
                "          \"zipcode\": \"90001\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"preferences\": {\n" +
                "        \"notifications\": {\n" +
                "          \"email\": true\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"order\": {\n" +
                "    \"details\": {\n" +
                "      \"items\": [\n" +
                "        {\n" +
                "          \"product\": {\n" +
                "            \"info\": {\n" +
                "              \"name\": \"Laptop\"\n" +
                "            },\n" +
                "            \"pricing\": {\n" +
                "              \"unitPrice\": 999.49,\n" +
                "              \"quantity\": 2\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"product\": {\n" +
                "            \"info\": {\n" +
                "              \"name\": \"Mouse\"\n" +
                "            },\n" +
                "            \"pricing\": {\n" +
                "              \"unitPrice\": 49.99,\n" +
                "              \"quantity\": 3\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"totals\": {\n" +
                "        \"amounts\": {\n" +
                "          \"total\": 2199.45\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
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
            JSONObject targetTemplate = JSON.parseObject("{}");
            try {
                JSONObject result = mappingService.processMapping(code, source, targetTemplate);
                assertNotNull("映射结果不能为空 - " + code, result);
                logger.info("{} -> 映射结果:\n{}", code, JSON.toJSONString(result, true));
            } catch (Exception e) {
                fail("处理 " + code + " 失败: " + e.getMessage());
            }
        }
        System.out.println("当前耗时: " + (System.currentTimeMillis() - start));
    }
} 