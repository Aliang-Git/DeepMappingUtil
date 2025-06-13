package com.aliang.loader;

import com.aliang.parse.*;
import com.aliang.registry.*;
import com.alibaba.fastjson.*;
import com.mongodb.client.*;
import org.bson.*;

import java.util.*;

public class MongoMappingConfigLoader {

    private final MongoCollection<Document> collection;
    private final MappingConfigParser parser;
    private final MappingRegistry registry;
    private final long refreshIntervalMillis = 5 * 60 * 1000; // 5分钟
    private long lastRefreshTime = 0;

    public MongoMappingConfigLoader(MongoCollection<Document> collection,
                                    MappingConfigParser parser,
                                    MappingRegistry registry) {
        this.collection = collection;
        this.parser = parser;
        this.registry = registry;
    }

    public void loadIfNecessary() {
        if (System.currentTimeMillis() - lastRefreshTime > refreshIntervalMillis) {
            List<Document> rules = collection.find().into(new ArrayList<>());
            JSONObject config = new JSONObject();
            for (Document doc : rules) {
                String productCode = doc.getString("productCode");
                JSONArray mappings = JSONArray.parseArray(doc.get("mappings", List.class).toString());
                config.put(productCode, mappings);
            }
            parser.parseAndRegister(config, registry);
            lastRefreshTime = System.currentTimeMillis();
        }
    }
}
