package com.aliang.registry;

import com.alibaba.fastjson.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * 注册中心（MappingRegistry）
 * 存储并管理所有产品的映射规则
 */
public class MappingRegistry {
    private static final Logger logger = LoggerFactory.getLogger(MappingRegistry.class);

    private final Map<String, Map<String, JSONObject>> mappings = new ConcurrentHashMap<>();

    public void registerMapping(String code, String key, JSONObject mapping) {
        if (code == null || code.trim().isEmpty()) {
            logger.error("注册映射失败：code不能为空");
            return;
        }
        if (key == null || key.trim().isEmpty()) {
            logger.error("注册映射失败：key不能为空");
            return;
        }
        if (mapping == null) {
            logger.error("注册映射失败：mapping不能为空");
            return;
        }

        // 验证映射配置
        if (!mapping.containsKey("sourcePath") || !mapping.containsKey("targetPath")) {
            logger.error("注册映射失败：映射配置必须包含sourcePath和targetPath - code: {}, key: {}", code, key);
            return;
        }

        mappings.computeIfAbsent(code, k -> new ConcurrentHashMap<>())
                .put(key, mapping);
        logger.debug("注册映射成功 - code: {}, key: {}", code, key);
    }

    public JSONObject getMapping(String code, String key) {
        if (code == null || code.trim().isEmpty()) {
            logger.error("获取映射失败：code不能为空");
            return null;
        }
        if (key == null || key.trim().isEmpty()) {
            logger.error("获取映射失败：key不能为空");
            return null;
        }

        Map<String, JSONObject> codeMappings = mappings.get(code);
        if (codeMappings == null) {
            logger.debug("未找到映射配置 - code: {}", code);
            return null;
        }
        JSONObject mapping = codeMappings.get(key);
        if (mapping == null) {
            logger.debug("未找到映射配置 - code: {}, key: {}", code, key);
            return null;
        }
        return mapping;
    }

    public Map<String, JSONObject> getMappings(String code) {
        if (code == null || code.trim().isEmpty()) {
            logger.error("获取映射失败：code不能为空");
            return new HashMap<>();
        }
        return mappings.getOrDefault(code, new HashMap<>());
    }

    public void clear() {
        mappings.clear();
        logger.debug("清空所有映射配置");
    }
}
