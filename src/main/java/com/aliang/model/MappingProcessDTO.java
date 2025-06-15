package com.aliang.model;

import java.util.*;

public class MappingProcessDTO {
    private Map<String, Object> source;
    private Map<String, Object> targetTemplate;

    public Map<String, Object> getSource() {
        return source;
    }

    public void setSource(Map<String, Object> source) {
        this.source = source;
    }

    public Map<String, Object> getTargetTemplate() {
        return targetTemplate;
    }

    public void setTargetTemplate(Map<String, Object> targetTemplate) {
        this.targetTemplate = targetTemplate;
    }
} 