package com.aliang.rule;

import com.aliang.mapping.*;
import com.alibaba.fastjson.*;
import lombok.*;

import java.util.*;

/**
 *
 * 某个产品编码（如 A001）下的所有字段映射规则集合
 */
@Data
@AllArgsConstructor
public class ProductMappingRule {
    private String productCode;
    private List<FieldMapping> fieldMappings = new ArrayList<>();

    public ProductMappingRule(String productCode) {
        this.productCode = productCode;
    }

    public ProductMappingRule addFieldMapping(FieldMapping mapping) {
        fieldMappings.add(mapping);
        return this;
    }

    public void apply(JSONObject source, JSONObject target) {
        for (FieldMapping mapping : fieldMappings) {
            mapping.apply(source, target); // ← 使用统一的 apply 方法
        }
    }

    // Getter
}
