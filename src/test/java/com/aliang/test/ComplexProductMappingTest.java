package com.aliang.test;

import com.aliang.service.*;
import com.alibaba.fastjson.*;
import org.junit.*;
import org.slf4j.*;

import static org.junit.Assert.*;

public class ComplexProductMappingTest {
    private static final Logger logger = LoggerFactory.getLogger(ComplexProductMappingTest.class);
    private ProductMappingService mappingService;

    @Before
    public void setUp() {
        /*  使用内存MongoDB进行测试 */
        mappingService = new ProductMappingService(
                "mongodb://localhost:27017",
                "test_db",
                "mapping_configs"
        ) {
            /*  重写getMappingConfigFromMongo方法，模拟从MongoDB获取配置 */
            @Override
            protected JSONObject getMappingConfigFromMongo(String productCode) {
                if ("P001".equals(productCode)) {
                    return JSON.parseObject("{\n" +
                            "  \"code\": \"P001\",\n" +
                            "  \"mappings\": [\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.basicInfo.name\",\n" +
                            "      \"targetPath\": \"$.productName\",\n" +
                            "      \"processors\": [\"uppercase\", \"prefix:PRODUCT_\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.basicInfo.code\",\n" +
                            "      \"targetPath\": \"$.productCode\",\n" +
                            "      \"processors\": [\"prefix:CODE_\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.basicInfo.category\",\n" +
                            "      \"targetPath\": \"$.categoryName.category\",\n" +
                            "      \"processors\": [\"uppercase\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.basicInfo.isActive\",\n" +
                            "      \"targetPath\": \"$.status\",\n" +
                            "      \"processors\": [\"booleantoyesno\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.basicInfo.createTime\",\n" +
                            "      \"targetPath\": \"$.createTime\",\n" +
                            "      \"processors\": [\"dateformat:yyyy年MM月dd日 HH:mm:ss\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.priceInfo.originalPrice\",\n" +
                            "      \"targetPath\": \"$.price\",\n" +
                            "      \"processors\": [\"multiplybyten:0.8\", \"roundtwodecimal:8\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.priceInfo.taxRate\",\n" +
                            "      \"targetPath\": \"$.taxRate\",\n" +
                            "      \"processors\": [\"range:0,1,0,100\", \"roundtwodecimal:0\", \"suffix:%\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.inventory.total\",\n" +
                            "      \"targetPath\": \"$.totalStock\",\n" +
                            "      \"processors\": [\"format:库存数量：%s\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.inventory.locations\",\n" +
                            "      \"targetPath\": \"$.stockLocations\",\n" +
                            "      \"aggregationStrategies\": [\"join:keepArrayFormat=true\"],\n" +
                            "      \"processors\": [\"prefix:仓库-\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.sales.totalAmount\",\n" +
                            "      \"targetPath\": \"$.totalSales\",\n" +
                            "      \"processors\": [\"roundtwodecimal\", \"prefix:¥\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.sales.averagePrice\",\n" +
                            "      \"targetPath\": \"$.avgPrice\",\n" +
                            "      \"processors\": [\"roundtwodecimal\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.ratings.average\",\n" +
                            "      \"targetPath\": \"$.rating\",\n" +
                            "      \"processors\": [\"roundtwodecimal\", \"suffix:星\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.ratings.distribution\",\n" +
                            "      \"targetPath\": \"$.ratingDistribution\",\n" +
                            "      \"aggregationStrategies\": [\"sum\"],\n" +
                            "      \"processors\": [\"tointeger\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.attributes.color\",\n" +
                            "      \"targetPath\": \"$.color\",\n" +
                            "      \"processors\": [\"uppercase\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.attributes.size\",\n" +
                            "      \"targetPath\": \"$.size\",\n" +
                            "      \"processors\": [\"mapvalue:大号=L;中号=M;小号=S\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.attributes.weight\",\n" +
                            "      \"targetPath\": \"$.weight\",\n" +
                            "      \"processors\": [\"roundtwodecimal\", \"suffix:kg\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.status\",\n" +
                            "      \"targetPath\": \"$.productStatus\",\n" +
                            "      \"processors\": [\"mapvalue:active=在售;inactive=下架\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.tags\",\n" +
                            "      \"targetPath\": \"$.productTags\",\n" +
                            "      \"aggregationStrategies\": [\"join\"],\n" +
                            "      \"processors\": [\"prefix:#\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.description\",\n" +
                            "      \"targetPath\": \"$.productDesc\",\n" +
                            "      \"processors\": [\"substring:0,50\", \"suffix:...\"]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"sourcePath\": \"$.specifications.length\",\n" +
                            "      \"targetPath\": \"$.dimensions\",\n" +
                            "      \"processors\": [\"format:长%.2fcm\"]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}");
                }
                return null;
            }
        };
    }

    @Test
    public void testAllProducts() {
        testProduct1();
        /*  后续添加其他产品的测试方法 */
    }

    private void testProduct1() {
        logger.info("测试产品1的映射转换");

        /*  源数据（模拟MongoDB数据） */
        String sourceJson = "{\n" +
                "  \"_id\": \"product1\",\n" +
                "  \"basicInfo\": {\n" +
                "    \"name\": \"测试商品1\",\n" +
                "    \"code\": \"P001\",\n" +
                "    \"category\": \"电子产品\",\n" +
                "    \"isActive\": true,\n" +
                "    \"createTime\": \"2024-03-21 10:00:00\",\n" +
                "    \"updateTime\": \"2024-03-21 15:30:00\"\n" +
                "  },\n" +
                "  \"priceInfo\": {\n" +
                "    \"originalPrice\": 1000.5678,\n" +
                "    \"discountRate\": 0.8,\n" +
                "    \"taxRate\": 0.13,\n" +
                "    \"currency\": \"CNY\"\n" +
                "  },\n" +
                "  \"inventory\": {\n" +
                "    \"total\": 1000,\n" +
                "    \"available\": 800,\n" +
                "    \"reserved\": 200,\n" +
                "    \"locations\": [\"A区\", \"B区\", \"C区\"]\n" +
                "  },\n" +
                "  \"sales\": {\n" +
                "    \"totalAmount\": 50000.1234,\n" +
                "    \"totalQuantity\": 50,\n" +
                "    \"averagePrice\": 1000.0025,\n" +
                "    \"lastSaleTime\": \"2024-03-20 16:45:00\"\n" +
                "  },\n" +
                "  \"ratings\": {\n" +
                "    \"average\": 4.5,\n" +
                "    \"count\": 100,\n" +
                "    \"distribution\": [5, 4, 3, 2, 1]\n" +
                "  },\n" +
                "  \"attributes\": {\n" +
                "    \"color\": \"红色\",\n" +
                "    \"size\": \"大号\",\n" +
                "    \"weight\": 2.5,\n" +
                "    \"material\": \"金属\"\n" +
                "  },\n" +
                "  \"status\": \"active\",\n" +
                "  \"tags\": [\"热销\", \"新品\", \"促销\"],\n" +
                "  \"description\": \"这是一个测试商品描述\",\n" +
                "  \"specifications\": {\n" +
                "    \"length\": 100.5,\n" +
                "    \"width\": 50.25,\n" +
                "    \"height\": 30.75\n" +
                "  }\n" +
                "}";

        /*  目标模板 */
        String targetTemplateJson = "{}";

        /*  执行映射 */
        JSONObject source = JSON.parseObject(sourceJson);
        JSONObject targetTemplate = JSON.parseObject(targetTemplateJson);
        JSONObject result = mappingService.processMapping("P001", source, targetTemplate);

        /*  验证结果 */
        logger.info("映射结果: {}", JSON.toJSONString(result, true));
        String resultJson = result.toJSONString();
        /*  添加断言 */
        assertNotNull(result);
        assertEquals("PRODUCT_测试商品1", result.getString("productName"));
        assertEquals("CODE_P001", result.getString("productCode"));
        assertEquals("电子产品", JSONPath.read(resultJson, "$.categoryName.category"));
        assertEquals("是", result.getString("status"));
        assertEquals("2024年03月21日 10:00:00", result.getString("createTime"));
        assertEquals("800.45424000", result.getString("price"));
        assertEquals("13%", result.getString("taxRate"));
        assertEquals("库存数量：1000", result.getString("totalStock"));
        assertEquals("[仓库-A区,仓库-B区,仓库-C区]", result.getString("stockLocations"));
        assertEquals("¥50000.12", result.getString("totalSales"));
        assertEquals("1000.00", result.getString("avgPrice"));
        assertEquals("4.50星", result.getString("rating"));
        assertEquals(Integer.valueOf(15), result.getInteger("ratingDistribution"));
        assertEquals("红色", result.getString("color"));
        assertEquals("L", result.getString("size"));
        assertEquals("2.50kg", result.getString("weight"));
        assertEquals("在售", result.getString("productStatus"));
        assertEquals("#热销,#新品,#促销", result.getString("productTags"));
        assertEquals("这是一个测试商品描述...", result.getString("productDesc"));
        assertEquals("长100.50cm", result.getString("dimensions"));
        System.out.println("最终结果：" + result);
    }
} 