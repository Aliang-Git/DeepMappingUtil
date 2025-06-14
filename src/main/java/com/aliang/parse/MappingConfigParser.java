package com.aliang.parse;

import com.aliang.Foctory.*;
import com.aliang.mapping.*;
import com.aliang.processor.*;
import com.aliang.registry.*;
import com.aliang.rule.*;
import com.alibaba.fastjson.*;

import java.util.*;

/**
 * 解析 JSON 配置文件，生成并注册产品映射规则
 */
public class MappingConfigParser {
    private final ProcessorFactory processorFactory;

    public MappingConfigParser(ProcessorFactory processorFactory) {
        this.processorFactory = processorFactory;
    }

    public void parseAndRegister(JSONObject config, MappingRegistry registry) {
        JSONArray mappings = config.getJSONArray("mappings");
        String productCode = config.getString("productCode");
        ProductMappingRule rule = new ProductMappingRule(productCode);
            for (Object obj : mappings) {
                JSONObject fieldRule = (JSONObject) obj;
                String sourcePath = fieldRule.getString("sourcePath");
                String targetPath = fieldRule.getString("targetPath");
                FieldMapping mapping = new FieldMapping(sourcePath, targetPath);
                // 处理处理器列表
                JSONArray processorsArray = fieldRule.getJSONArray("processors");
                if (processorsArray != null && !processorsArray.isEmpty()) {
                    List<String> processorNames = processorsArray.toJavaList(String.class);
                    List<ValueProcessor> processors = ProcessorFactory.createProcessors(processorNames);
                    mapping.addProcessors(processors.toArray(new ValueProcessor[0]));
                }
                rule.addFieldMapping(mapping);
            }
            registry.register(rule);
    }
}
