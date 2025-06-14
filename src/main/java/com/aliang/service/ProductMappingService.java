package com.aliang.service;

import com.aliang.engine.MappingEngine;
import com.aliang.factory.ProcessorFactory;
import com.aliang.parse.MappingConfigParser;
import com.aliang.registry.MappingRegistry;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductMappingService {
    private static final Logger logger = LoggerFactory.getLogger(ProductMappingService.class);
    
    private final MongoClient mongoClient;
    private final String databaseName;
    private final String collectionName;
    private final MappingRegistry registry;
    private final MappingEngine engine;
    
    public ProductMappingService(String mongoUri, String databaseName, String collectionName) {
        this.mongoClient = MongoClients.create(mongoUri);
        this.databaseName = databaseName;
        this.collectionName = collectionName;
        this.registry = new MappingRegistry();
        this.engine = new MappingEngine(registry);
    }
    
    /**
     * 保存映射配置到MongoDB
     * @param mappingConfigJson 映射配置JSON字符串
     */
    public void saveMappingConfig(String mappingConfigJson) {
        try {
            JSONObject config = JSONObject.parseObject(mappingConfigJson);
            String productCode = config.getString("productCode");
            
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            
            // 将JSON字符串转换为Document
            Document document = Document.parse(mappingConfigJson);
            
            // 使用产品编码作为查询条件
            Document query = new Document("productCode", productCode);
            
            // 如果已存在则更新，不存在则插入
            collection.replaceOne(query, document, new com.mongodb.client.model.ReplaceOptions().upsert(true));
            
            logger.info("映射配置保存成功: productCode={}", productCode);
        } catch (Exception e) {
            logger.error("保存映射配置失败: error={}", e.getMessage(), e);
            throw new RuntimeException("保存映射配置失败", e);
        }
    }
    
    /**
     * 处理产品映射
     * @param productCode 产品编码
     * @param sourceData 源数据
     * @param targetTemplate 目标数据模板
     * @return 映射后的结果
     */
    public JSONObject processMapping(String productCode, JSONObject sourceData, JSONObject targetTemplate) {
        try {
            // 1. 从MongoDB获取映射配置
            JSONObject mappingConfig = getMappingConfigFromMongo(productCode);
            if (mappingConfig == null) {
                throw new IllegalArgumentException("未找到产品编码对应的映射配置: " + productCode);
            }
            
            // 2. 解析并注册映射规则
            MappingConfigParser parser = new MappingConfigParser(new ProcessorFactory(), registry);
            parser.parseAndRegister(mappingConfig, registry);
            
            // 3. 执行映射
            return engine.map(sourceData, targetTemplate, productCode);
            
        } catch (Exception e) {
            logger.error("处理产品映射失败: productCode={}, error={}", productCode, e.getMessage(), e);
            throw new RuntimeException("处理产品映射失败", e);
        }
    }
    
    /**
     * 从MongoDB获取映射配置
     */
    protected JSONObject getMappingConfigFromMongo(String productCode) {
        try {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            
            // 查询条件：产品编码
            Document query = new Document("productCode", productCode);
            Document result = collection.find(query).first();
            
            if (result != null) {
                // 将Document转换为JSONObject
                return JSONObject.parseObject(result.toJson());
            }
            
            return null;
        } catch (Exception e) {
            logger.error("从MongoDB获取映射配置失败: productCode={}, error={}", productCode, e.getMessage(), e);
            throw new RuntimeException("获取映射配置失败", e);
        }
    }
    
    /**
     * 关闭MongoDB连接
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
} 