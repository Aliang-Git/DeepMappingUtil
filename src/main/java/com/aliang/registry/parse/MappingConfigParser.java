package com.aliang.registry.parse;

import com.aliang.registry.*;
import com.alibaba.fastjson.*;
import org.slf4j.*;

/**
 * 解析 JSON 配置，生成并注册产品映射规则
 */
public class MappingConfigParser {
    private static final Logger logger = LoggerFactory.getLogger(MappingConfigParser.class);

    public static void parseAndRegister(JSONObject config, MappingRegistry registry) {
        /*  验证配置 */
        validateConfig(config);

        /*  解析配置并注册到注册表 */
        String code = config.getString("code");
        Object mappingsRaw = config.get("mappings");

        if (mappingsRaw instanceof JSONObject) {
            JSONObject mappings = (JSONObject) mappingsRaw;
            for (String key : mappings.keySet()) {
                JSONObject mapping = mappings.getJSONObject(key);
                sanitizeProcessors(code, key, mapping);
                registry.registerMapping(code, key, mapping);
            }
        } else if (mappingsRaw instanceof JSONArray) {
            JSONArray array = (JSONArray) mappingsRaw;
            for (int i = 0; i < array.size(); i++) {
                JSONObject mapping = array.getJSONObject(i);
                String targetPath = mapping.getString("targetPath");
                sanitizeProcessors(code, targetPath, mapping);
                registry.registerMapping(code, targetPath, mapping);
            }
        } else {
            throw new IllegalArgumentException("mappings 必须是对象或数组");
        }
    }

    private static void validateConfig(JSONObject config) {
        if (config == null) {
            throw new IllegalArgumentException("配置不能为空");
        }

        String code = config.getString("code");
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("编码不能为空");
        }

        Object mappingsRaw = config.get("mappings");
        if (mappingsRaw == null) {
            throw new IllegalArgumentException("映射配置不能为空");
        }
    }

    private static void sanitizeProcessors(String code, String key, JSONObject mapping) {
        if (mapping.containsKey("processors")) {
            JSONArray processors = mapping.getJSONArray("processors");
            for (int i = 0; i < processors.size(); i++) {
                Object elem = processors.get(i);
                if (!(elem instanceof String)) {
                    logger.error("处理器配置项不是字符串 - code: {}, key: {}, index: {}", code, key, i);
                    processors.remove(i);
                    i--;
                    continue;
                }
                String spec = (String) elem;
                if (spec.trim().isEmpty()) {
                    logger.error("处理器配置无效 - code: {}, key: {}, index: {}", code, key, i);
                    processors.remove(i);
                    i--;
                }
            }
        }
    }
}
