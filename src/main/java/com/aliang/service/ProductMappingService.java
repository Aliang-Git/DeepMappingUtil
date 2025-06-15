package com.aliang.service;

import com.aliang.registry.engine.*;
import com.aliang.registry.parse.*;
import com.alibaba.fastjson.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.util.*;

@Service
public class ProductMappingService extends BaseMappingService {
    private static final Logger logger = LoggerFactory.getLogger(ProductMappingService.class);
    private MappingEngine engine;

    public ProductMappingService() {
        super();
    }

    @PostConstruct
    public void initEngine() {
        this.engine = new MappingEngine(mappingRegistry);
    }

    @Override
    public Map<String, Object> processMapping(String productCode, Map<String, Object> source, Map<String, Object> targetTemplate) {
        try {
            /*  获取映射配置 */
            JSONObject mappingConfig = getMappingConfigFromMongo(productCode);
            if (mappingConfig == null) {
                throw new IllegalArgumentException("未找到产品映射配置: " + productCode);
            }

            /*  清理旧规则并注册新规则 */
            mappingRegistry.clearMappings(productCode);
            MappingConfigParser.parseAndRegister(mappingConfig, mappingRegistry);

            /*  执行映射 */
            JSONObject sourceJson = new JSONObject(source);
            // 创建全新的模板对象，避免引用问题
            JSONObject targetJson = JSON.parseObject(JSON.toJSONString(targetTemplate));
            JSONObject result = engine.executeMapping(productCode, sourceJson, targetJson);
            return result.getInnerMap();
        } catch (Exception e) {
            logger.error("映射处理失败 - 产品编码: {}, 错误: {}", productCode, e.getMessage());
            throw new RuntimeException("处理产品映射失败", e);
        }
    }
} 