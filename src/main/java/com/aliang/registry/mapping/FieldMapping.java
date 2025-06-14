package com.aliang.registry.mapping;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;
import com.aliang.rule.strategy.*;
import com.alibaba.fastjson.*;
import lombok.*;

import java.util.*;

/**
 * 字段映射类
 * <p>
 * 该类定义了从源路径到目标路径的字段映射规则，支持多个处理器和聚合策略。
 * 处理器用于对字段值进行处理（如格式化、转换等），聚合策略用于对数组类型的字段值进行聚合操作。
 * <p>
 * 主要功能：
 * 1. 定义源路径和目标路径的映射关系
 * 2. 支持多个处理器的链式处理
 * 3. 支持多个聚合策略的链式处理
 * 4. 记录处理过程中的无效字段
 * <p>
 * 使用示例：
 * <pre>
 * // 创建字段映射规则
 * FieldMapping mapping = new FieldMapping("$.user.name", "$.profile.fullName", "A001");
 *
 * // 添加处理器
 * mapping.addProcessors(
 *     new TrimProcessor(),  // 去除空格
 *     new LowercaseProcessor()  // 转小写
 * );
 *
 * // 添加聚合策略
 * mapping.addAggregationStrategies(
 *     DefaultAggregationStrategies.getStrategy("SUM")  // 求和
 * );
 *
 * // 执行映射
 * JSONObject source = JSON.parseObject("{\"user\":{\"name\":\"John\"}}");
 * JSONObject target = JSON.parseObject("{\"profile\":{\"fullName\":\"\"}}");
 * mapping.apply(source, target);
 * </pre>
 */
@Data
public class FieldMapping {
    /**
     * 源数据路径，使用 JSONPath 表达式
     */
    private final String sourcePath;

    /**
     * 目标数据路径，使用 JSONPath 表达式
     */
    private final String targetPath;

    /**
     * 字段值处理器列表，用于对字段值进行处理
     */
    private final List<ValueProcessor> processors;

    /**
     * 聚合策略列表，用于对数组类型的字段值进行聚合操作
     */
    private final List<AggregationStrategy> aggregationStrategies;

    /**
     * 产品编码，用于标识该映射规则属于哪个产品
     */
    private final String productCode;

    /**
     * 无效字段集合，记录处理过程中发现的无效字段
     */
    private final Set<String> invalidFields = new HashSet<>();

    /**
     * 日志记录器
     */
    private final FieldMappingLogger logger = new DefaultFieldMappingLogger();


    /**
     * 从源数据中获取字段值
     *
     * @param source     源数据
     * @param sourcePath 源字段路径
     * @return 字段值
     */
    private Object evaluateSourcePath(JSONObject source, String sourcePath) {
        try {
            Object value = JSONPath.eval(source, sourcePath);
            if (value == null) {
                logger.logInvalidValue(sourcePath, targetPath, null, "源字段不存在");
                invalidFields.add(sourcePath);
                return null;
            }
            return value;
        } catch (Exception e) {
            logger.logInvalidValue(sourcePath, targetPath, null, "获取源字段值失败: " + e.getMessage());
            invalidFields.add(sourcePath);
            return null;
        }
    }

    /**
     * 执行字段映射操作
     * <p>
     * 该方法会按照以下步骤执行映射：
     * 1. 从源数据中获取字段值
     * 2. 检查字段值是否有效
     * 3. 执行处理器链处理
     * 4. 执行聚合策略处理
     * 5. 将处理后的值设置到目标数据中
     *
     * @param source 源数据
     * @param target 目标数据
     * @return 处理后的目标数据
     */
    public void apply(JSONObject source, JSONObject target) {
        // 获取源值
        Object value = evaluateSourcePath(source, sourcePath);
        logger.logMappingSuccess(sourcePath, targetPath, "Initial value: " + value);
        if (value == null) {
            return;
        }

        // 应用聚合策略
        if (!aggregationStrategies.isEmpty() && value instanceof List) {
            for (AggregationStrategy strategy : aggregationStrategies) {
                try {
                    Object oldValue = value;
                    value = strategy.apply((List<?>) value);
                    logger.logMappingSuccess(sourcePath, targetPath, "After aggregation: " + value);
                    if (value == null) {
                        logger.logStrategyFailure(sourcePath, targetPath,
                                strategy.getClass().getSimpleName(), oldValue, "聚合结果为null");
                        continue;
                    }
                } catch (Exception e) {
                    logger.logStrategyFailure(sourcePath, targetPath,
                            strategy.getClass().getSimpleName(), value, e.getMessage());
                    continue;
                }
            }
        }

        // 应用处理器
        if (!processors.isEmpty()) {
            for (ValueProcessor processor : processors) {
                try {
                    Object oldValue = value;
                    value = processor.doProcess(value);
                    logger.logMappingSuccess(sourcePath, targetPath, value);
                } catch (Exception e) {
                    logger.logProcessorFailure(sourcePath, targetPath,
                            processor.getClass().getSimpleName(), value, e.getMessage());
                }
            }
        }

        // 设置目标值
        if (value != null) {
            try {
                JSONPath.set(target, targetPath, value);
                logger.logMappingSuccess(sourcePath, targetPath, value);
            } catch (Exception e) {
                logger.logMappingFailure(sourcePath, targetPath, value, e.getMessage());
            }
        }
    }
}
