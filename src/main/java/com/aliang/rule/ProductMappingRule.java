package com.aliang.rule;

import com.aliang.registry.mapping.*;
import com.alibaba.fastjson.*;
import lombok.*;

import java.util.*;

/**
 * 产品映射规则类
 * <p>
 * 该类用于管理特定产品编码下的所有字段映射规则。每个产品编码（如 A001）可以包含多个字段映射规则，
 * 这些规则定义了如何将源数据映射到目标数据。
 * <p>
 * 主要功能：
 * 1. 管理产品编码下的字段映射规则集合
 * 2. 提供添加字段映射规则的方法
 * 3. 执行字段映射操作
 * <p>
 * 使用示例：
 * <pre>
 * 创建产品映射规则
 * ProductMappingRule rule = new ProductMappingRule("A001");
 *
 *  添加字段映射规则
 * rule.addFieldMapping(new FieldMapping("$.user.name", "$.profile.fullName")
 *     .addProcessors(new TrimProcessor()));
 *
 *  执行映射
 * JSONObject source = JSON.parseObject("{\"user\":{\"name\":\"John\"}}");
 * JSONObject target = JSON.parseObject("{\"profile\":{\"fullName\":\"\"}}");
 * JSONObject result = rule.apply(source, target);
 * </pre>
 */
@Data
@AllArgsConstructor
public class ProductMappingRule {
    /**
     * 产品编码，用于标识该规则属于哪个产品
     */
    private final String productCode;

    /**
     * 字段映射规则列表，存储该产品下的所有字段映射规则
     */
    private final List<FieldMapping> fieldMappings;

    /**
     * 执行字段映射操作
     * <p>
     * 该方法会遍历所有字段映射规则，将源数据中的字段值按照规则映射到目标数据中。
     * 每个字段映射规则可以包含多个处理器和聚合策略，用于对字段值进行处理。
     *
     * @param source 源数据
     * @param target 目标数据
     * @return 处理后的目标数据
     * @throws NullPointerException 如果 source 或 target 为 null
     */
    public JSONObject apply(@NonNull JSONObject source, @NonNull JSONObject target) {
        Objects.requireNonNull(source, "Source JSON cannot be null");
        Objects.requireNonNull(target, "Target JSON cannot be null");

        for (FieldMapping mapping : fieldMappings) {
            mapping.apply(source, target);
        }
        return target;
    }
}
