package com.aliang.model;

import lombok.*;

import java.util.*;

@Data
class FieldMappingPO {
    private String sourcePath;
    private String targetPath;
    private List<String> processors;
    private List<String> aggregationStrategies;
}

