package com.aliang.model;

import java.util.*;

class FieldMappingPO {
    private String sourcePath;
    private String targetPath;
    private List<String> processors;
    private List<String> aggregationStrategies;

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }
}

