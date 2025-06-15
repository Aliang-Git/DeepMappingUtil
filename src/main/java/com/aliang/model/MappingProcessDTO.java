package com.aliang.model;

import lombok.*;

import java.util.*;

@Data
public class MappingProcessDTO {
    private Map<String, Object> source;
    private Map<String, Object> targetTemplate;

} 