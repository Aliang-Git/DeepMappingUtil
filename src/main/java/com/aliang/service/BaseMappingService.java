package com.aliang.service;

import com.aliang.registry.*;
import com.aliang.registry.factory.*;
import com.alibaba.fastjson.*;
import org.bson.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public abstract class BaseMappingService {
    private static final Logger logger = LoggerFactory.getLogger(BaseMappingService.class);

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Value("${spring.data.mongodb.database}")
    protected String dbName;

    @Value("${your.collection.name}")
    protected String collectionName;

    protected final MappingRegistry mappingRegistry;
    protected final ProcessorFactory processorFactory;

    protected BaseMappingService() {
        this.mappingRegistry = new MappingRegistry();
        this.processorFactory = new ProcessorFactory();
    }

    /**
     * 从MongoDB获取映射配置
     * 子类可以覆盖此方法以提供自定义的配置获取逻辑
     *
     * @param code 产品编码
     * @return 映射配置
     */
    protected JSONObject getMappingConfigFromMongo(String code) {
        try {
            Query query = new Query(Criteria.where("code").is(code));
            Document doc = mongoTemplate.findOne(query, Document.class, collectionName);
            if (doc == null) {
                return null;
            }
            return JSONObject.parseObject(doc.toJson());
        } catch (Exception e) {
            logger.error("获取映射配置失败 - code: {}, 错误: {}", code, e.getMessage());
            return null;
        }
    }

    /**
     * 处理映射
     * 子类必须实现此方法以提供具体的映射处理逻辑
     *
     * @param code           产品编码
     * @param source         源数据
     * @param targetTemplate 目标模板
     * @return 处理后的数据
     */
    public abstract Map<String, Object> processMapping(String code, Map<String, Object> source, Map<String, Object> targetTemplate);
} 