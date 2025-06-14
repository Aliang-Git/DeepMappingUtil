package com.aliang.parse;

import com.aliang.factory.*;
import com.aliang.mapping.*;
import com.aliang.processor.*;
import com.aliang.registry.*;
import com.aliang.rule.*;
import com.aliang.strategy.*;
import com.alibaba.fastjson.*;
import org.slf4j.*;

import java.util.*;

/**
 * 解析 JSON 配置文件，生成并注册产品映射规则
 */
public class MappingConfigParser {
    private static final Logger logger = LoggerFactory.getLogger(MappingConfigParser.class);
    private final ProcessorFactory processorFactory;
    private final Set<String> invalidProcessors = new HashSet<>();
    private final Set<String> invalidStrategies = new HashSet<>();
    private final Set<String> invalidFields = new HashSet<>();
    private final MappingRegistry mappingRegistry;

    public MappingConfigParser(ProcessorFactory processorFactory, MappingRegistry mappingRegistry) {
        this.processorFactory = processorFactory;
        this.mappingRegistry = mappingRegistry;
    }

    public void parseAndRegister(JSONObject config, MappingRegistry registry) {
        JSONArray mappings = config.getJSONArray("mappings");
        String productCode = config.getString("productCode");
        ProductMappingRule rule = new ProductMappingRule(productCode);

        for (Object obj : mappings) {
            JSONObject fieldRule = (JSONObject) obj;
            String sourcePath = fieldRule.getString("sourcePath");
            String targetPath = fieldRule.getString("targetPath");
            FieldMapping mapping = new FieldMapping(sourcePath, targetPath, productCode);

            // 处理处理器列表
            JSONArray processorsArray = fieldRule.getJSONArray("processors");
            if (processorsArray != null && !processorsArray.isEmpty()) {
                List<String> processorNames = processorsArray.toJavaList(String.class);
                try {
                    List<ValueProcessor> processors = processorFactory.createProcessors(processorNames);
                    mapping.addProcessors(processors.toArray(new ValueProcessor[0]));
                } catch (Exception e) {
                    String message = String.format("处理器创建失败：处理器=%s，源路径=%s，目标路径=%s，错误=%s", 
                        processorNames, sourcePath, targetPath, e.getMessage());
                    logger.error(message);
                    invalidProcessors.addAll(processorNames);
                    // 处理器创建失败时，将字段标记为无效
                    invalidFields.add(sourcePath);
                    continue;
                }
            }

            // 处理聚合策略列表
            JSONArray aggregationStrategyArray = fieldRule.getJSONArray("aggregationStrategies");
            if (aggregationStrategyArray != null && !aggregationStrategyArray.isEmpty()) {
                List<String> aggregationStrategyNames = aggregationStrategyArray.toJavaList(String.class);
                List<AggregationStrategy> validStrategies = new ArrayList<>();
                
                for (String strategyName : aggregationStrategyNames) {
                    try {
                        // 如果策略名称包含参数（如 join:,），则直接使用
                        List<AggregationStrategy> strategies = AggregationStrategyFactory.createStrategy(Collections.singletonList(strategyName));
                        validStrategies.addAll(strategies);
                    } catch (Exception e) {
                        String message = String.format("聚合策略创建失败：策略=%s，源路径=%s，目标路径=%s，错误=%s", 
                            strategyName, sourcePath, targetPath, e.getMessage());
                        logger.error(message);
                        invalidStrategies.add(strategyName);
                        // 聚合策略创建失败时，将字段标记为无效
                        invalidFields.add(sourcePath);
                        continue;
                    }
                }
                
                if (!validStrategies.isEmpty()) {
                    mapping.addAggregationStrategies(validStrategies.toArray(new AggregationStrategy[0]));
                }
            }

            rule.addFieldMapping(mapping);
        }
        registry.register(rule);
        
        // 收集所有字段映射中的无效字段
        for (FieldMapping mapping : rule.getFieldMappings()) {
            invalidFields.addAll(mapping.getInvalidFields());
        }
    }

    public Set<String> getInvalidProcessors() {
        return Collections.unmodifiableSet(invalidProcessors);
    }

    public Set<String> getInvalidStrategies() {
        return Collections.unmodifiableSet(invalidStrategies);
    }

    public Set<String> getInvalidFields() {
        return Collections.unmodifiableSet(invalidFields);
    }
}
