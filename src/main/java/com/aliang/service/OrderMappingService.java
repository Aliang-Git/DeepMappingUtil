package com.aliang.service;

import com.aliang.parse.*;
import com.alibaba.fastjson.*;
import lombok.extern.slf4j.*;
import org.slf4j.*;

@Slf4j
public class OrderMappingService extends BaseMappingService {
    private static final Logger logger = LoggerFactory.getLogger(OrderMappingService.class);

    public OrderMappingService(String mongoUri, String dbName, String collectionName) {
        super(mongoUri, dbName, collectionName);
    }

    @Override
    public JSONObject processMapping(String orderCode, JSONObject source, JSONObject targetTemplate) {
        try {
            // 获取映射配置
            JSONObject mappingConfig = getMappingConfigFromMongo(orderCode);
            if (mappingConfig == null) {
                throw new IllegalArgumentException("未找到订单映射配置: " + orderCode);
            }

            // 解析并注册配置
            MappingConfigParser.parseAndRegister(mappingConfig, mappingRegistry);

            // 执行映射
            return executeMapping(mappingConfig, source, targetTemplate);
        } catch (Exception e) {
            logger.error("映射处理失败 - 订单编码: {}, 错误: {}", orderCode, e.getMessage());
            throw new RuntimeException("处理订单映射失败", e);
        }
    }
} 