package com.aliang.model;

import java.util.*;

public class MappingRulePO {
    private String code;
    private List<FieldMappingPO> mappings;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<FieldMappingPO> getMappings() {
        return mappings;
    }

    public void setMappings(List<FieldMappingPO> mappings) {
        this.mappings = mappings;
    }
} 