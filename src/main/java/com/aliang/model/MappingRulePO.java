package com.aliang.model;

import lombok.*;

import java.util.*;

@Data
public class MappingRulePO {
    private String code;
    private List<FieldMappingPO> mappings;
} 