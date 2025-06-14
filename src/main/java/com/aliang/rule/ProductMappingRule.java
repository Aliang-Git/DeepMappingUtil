package com.aliang.rule;

import com.aliang.mapping.FieldMapping;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 产品映射规则类
 * 
 * 该类用于管理特定产品编码下的所有字段映射规则。每个产品编码（如 A001）可以包含多个字段映射规则，
 * 这些规则定义了如何将源数据映射到目标数据。
 * 
 * 主要功能：
 * 1. 管理产品编码下的字段映射规则集合
 * 2. 提供添加字段映射规则的方法
 * 3. 执行字段映射操作
 * 
 * 使用示例：
 * <pre>
 * // 创建产品映射规则
 * ProductMappingRule rule = new ProductMappingRule("A001");
 * 
 * // 添加字段映射规则
 * rule.addFieldMapping(new FieldMapping("$.user.name", "$.profile.fullName")
 *     .addProcessors(new TrimProcessor()));
 * 
 * // 执行映射
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
     * 构造函数
     * 
     * @param productCode 产品编码
     */
    public ProductMappingRule(@NonNull String productCode) {
        this.productCode = productCode;
        this.fieldMappings = new ArrayList<>();
    }

    /**
     * 添加字段映射规则
     * 
     * @param mapping 字段映射规则
     * @return 当前实例，支持链式调用
     */
    public ProductMappingRule addFieldMapping(@NonNull FieldMapping mapping) {
        fieldMappings.add(mapping);
        return this;
    }

    /**
     * 执行字段映射操作
     * 
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

    /**
     * 获取字段映射规则列表
     * 
     * @return 不可修改的字段映射规则列表
     */
    public List<FieldMapping> getFieldMappings() {
        return Collections.unmodifiableList(fieldMappings);
    }

    // Getter
}
