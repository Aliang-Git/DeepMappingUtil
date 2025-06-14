package com.aliang.processor.impl;

import com.aliang.processor.ValueProcessor;
import com.aliang.utils.ProcessorUtils;
import java.util.List;
import java.util.Map;

/**
 * 状态码替换处理器
 */
public class StatusToChineseProcessor implements ValueProcessor {
    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value instanceof String) {
            String status = (String) value;
            switch (status.toLowerCase()) {
                case "pending":
                    return "待处理";
                case "processing":
                    return "处理中";
                case "completed":
                    return "已完成";
                case "cancelled":
                    return "已取消";
                default:
                    return status;
            }
        }
        return value;
    }
}
