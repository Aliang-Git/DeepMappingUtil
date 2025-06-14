package com.aliang.mapping;

import com.aliang.logger.*;
import com.aliang.logger.imple.*;
import com.aliang.processor.*;
import com.aliang.strategy.*;
import com.alibaba.fastjson.*;
import lombok.*;

import java.util.*;

/**
 * 字段映射类
 * 
 * 该类定义了从源路径到目标路径的字段映射规则，支持多个处理器和聚合策略。
 * 处理器用于对字段值进行处理（如格式化、转换等），聚合策略用于对数组类型的字段值进行聚合操作。
 * 
 * 主要功能：
 * 1. 定义源路径和目标路径的映射关系
 * 2. 支持多个处理器的链式处理
 * 3. 支持多个聚合策略的链式处理
 * 4. 记录处理过程中的无效字段
 * 
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
 *     DefaultAggregationStrategies.SUM  // 求和
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
    private final MappingLogger logger = new Slf4jMappingLogger();

    /**
     * 构造函数
     * 
     * @param sourcePath 源数据路径
     * @param targetPath 目标数据路径
     */
    public FieldMapping(String sourcePath, String targetPath) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.processors = new ArrayList<>();
        this.aggregationStrategies = new ArrayList<>();
        this.productCode = null;
    }

    /**
     * 构造函数
     * 
     * @param sourcePath 源数据路径
     * @param targetPath 目标数据路径
     * @param productCode 产品编码
     */
    public FieldMapping(String sourcePath, String targetPath, String productCode) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.productCode = productCode;
        this.processors = new ArrayList<>();
        this.aggregationStrategies = new ArrayList<>();
    }

    /**
     * 构造函数
     * 
     * @param sourcePath 源数据路径
     * @param targetPath 目标数据路径
     * @param processors 处理器列表
     * @param aggregationStrategies 聚合策略列表
     * @param productCode 产品编码
     */
    public FieldMapping(String sourcePath, String targetPath, List<ValueProcessor> processors, 
                       List<AggregationStrategy> aggregationStrategies, String productCode) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.processors = processors;
        this.aggregationStrategies = aggregationStrategies;
        this.productCode = productCode;
    }

    /**
     * 执行字段映射操作
     * 
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
    public JSONObject apply(JSONObject source, JSONObject target) {
        try {
            // 检查字段是否存在
            Object value = JSONPath.eval(source, sourcePath);
            if (value == null) {
                String message = String.format("字段映射跳过：源路径=%s 不存在，目标路径=%s", sourcePath, targetPath);
                logger.warn(message);
                invalidFields.add(sourcePath);
                return target;
            }

            // 检查数组是否为空
            if (value instanceof List && ((List<?>) value).isEmpty()) {
                String message = String.format("字段映射跳过：源路径=%s 为空数组，目标路径=%s", sourcePath, targetPath);
                logger.warn(message);
                invalidFields.add(sourcePath);
                return target;
            }

            // 检查字段值是否有效
            if (value instanceof List) {
                List<?> list = (List<?>) value;
                boolean hasValidValue = false;
                for (Object item : list) {
                    if (item != null) {
                        hasValidValue = true;
                        break;
                    }
                }
                if (!hasValidValue) {
                    String message = String.format("字段映射跳过：源路径=%s 包含无效值，目标路径=%s", sourcePath, targetPath);
                    logger.warn(message);
                    invalidFields.add(sourcePath);
                    return target;
                }
            }

            // 先执行聚合策略
            if (!aggregationStrategies.isEmpty() && value instanceof List) {
                for (AggregationStrategy strategy : aggregationStrategies) {
                    try {
                        value = strategy.apply((List<?>) value);
                        logger.debug("聚合策略 {} 执行完成，结果：{}", strategy.getClass().getSimpleName(), value);
                    } catch (Exception e) {
                        String message = String.format("聚合策略执行失败：策略=%s，源路径=%s，目标路径=%s，错误=%s", 
                            strategy.getClass().getSimpleName(), sourcePath, targetPath, e.getMessage());
                        logger.error(message);
                        invalidFields.add(sourcePath);
                        return target;
                    }
                }
            }

            // 再执行处理器链
            boolean processorSuccess = true;
            for (ValueProcessor processor : processors) {
                try {
                    value = processor.process(value, productCode, sourcePath);
                    logger.debug("处理器 {} 执行完成，结果：{}", processor.getClass().getSimpleName(), value);
                } catch (Exception e) {
                    String message = String.format("处理器执行失败：处理器=%s，源路径=%s，目标路径=%s，错误=%s", 
                        processor.getClass().getSimpleName(), sourcePath, targetPath, e.getMessage());
                    logger.error(message);
                    processorSuccess = false;
                    invalidFields.add(sourcePath);
                    return target;
                }
            }

            // 如果处理器执行失败，标记字段为无效
            if (!processorSuccess) {
                invalidFields.add(sourcePath);
                return target;
            }

            // 如果结果是单元素列表，提取出单个值
            if (value instanceof List && ((List<?>) value).size() == 1) {
                value = ((List<?>) value).get(0);
            }

            // 如果目标路径是数组中的元素，确保值是单个值而不是数组
            if (targetPath.contains("[") && value instanceof List) {
                if (((List<?>) value).size() == 1) {
                    value = ((List<?>) value).get(0);
                }
            }

            // 设置目标值
            JSONPath.set(target, targetPath, value);
            return target;
        } catch (Exception e) {
            String message = String.format("字段映射执行失败：源路径=%s，目标路径=%s，错误=%s", 
                sourcePath, targetPath, e.getMessage());
            logger.error(message);
            invalidFields.add(sourcePath);
            return target;
        }
    }

    /**
     * 添加处理器
     * 
     * @param processors 处理器数组
     * @return 当前实例，支持链式调用
     */
    public FieldMapping addProcessors(ValueProcessor... processors) {
        if (processors != null) {
            this.processors.addAll(Arrays.asList(processors));
        }
        return this;
    }

    /**
     * 添加聚合策略
     * 
     * @param strategies 聚合策略数组
     * @return 当前实例，支持链式调用
     */
    public FieldMapping addAggregationStrategies(AggregationStrategy... strategies) {
        if (strategies != null) {
            this.aggregationStrategies.addAll(Arrays.asList(strategies));
        }
        return this;
    }

    /**
     * 获取无效字段集合
     * 
     * @return 不可修改的无效字段集合
     */
    public Set<String> getInvalidFields() {
        return Collections.unmodifiableSet(invalidFields);
    }
}
