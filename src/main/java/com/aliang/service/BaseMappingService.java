package com.aliang.service;

import com.aliang.factory.*;
import com.aliang.registry.*;
import com.alibaba.fastjson.*;
import com.mongodb.client.*;
import org.bson.*;
import org.slf4j.*;

public abstract class BaseMappingService {
    private static final Logger logger = LoggerFactory.getLogger(BaseMappingService.class);

    protected final MongoClient mongoClient;
    protected final String dbName;
    protected final String collectionName;
    protected final MappingRegistry mappingRegistry;
    protected final ProcessorFactory processorFactory;

    protected BaseMappingService(String mongoUri, String dbName, String collectionName) {
        this.mongoClient = MongoClients.create(mongoUri);
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.mappingRegistry = new MappingRegistry();
        this.processorFactory = new ProcessorFactory();
    }

    protected JSONObject getMappingConfigFromMongo(String code) {
        try {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            Document doc = collection.find(new Document("code", code)).first();
            if (doc == null) {
                return null;
            }

            return JSONObject.parseObject(doc.toJson());
        } catch (Exception e) {
            logger.error("获取映射配置失败 - code: {}, 错误: {}", code, e.getMessage());
            return null;
        }
    }

    public abstract JSONObject processMapping(String code, JSONObject source, JSONObject targetTemplate);
} 