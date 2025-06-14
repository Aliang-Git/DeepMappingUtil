package com.aliang.service;

import com.aliang.registry.engine.*;
import com.aliang.registry.parse.*;
import com.alibaba.fastjson.*;
import org.slf4j.*;

public class ProductMappingService extends BaseMappingService {
    private static final Logger logger = LoggerFactory.getLogger(ProductMappingService.class);
    private final MappingEngine engine;

    public ProductMappingService(String mongoUri, String dbName, String collectionName) {
        super(mongoUri, dbName, collectionName);
        this.engine = new MappingEngine(mappingRegistry);
    }

    @Override
    public JSONObject processMapping(String productCode, JSONObject source, JSONObject targetTemplate) {
        try {
            // 获取映射配置
            JSONObject mappingConfig = getMappingConfigFromMongo(productCode);
            if (mappingConfig == null) {
                throw new IllegalArgumentException("未找到产品映射配置: " + productCode);
            }

            // 解析并注册配置
            MappingConfigParser.parseAndRegister(mappingConfig, mappingRegistry);

            // 执行映射
            return engine.executeMapping(productCode, source, targetTemplate);
        } catch (Exception e) {
            logger.error("映射处理失败 - 产品编码: {}, 错误: {}", productCode, e.getMessage());
            throw new RuntimeException("处理产品映射失败", e);
        }
    }
} 