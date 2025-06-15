package com.aliang.rule.processor;

import com.aliang.utils.*;

/**
 * 处理器抽象基类
 * 提供通用的处理逻辑和日志记录功能
 */
public abstract class AbstractProcessor implements ValueProcessor {
    protected final String processorName;

    protected AbstractProcessor(String processorName) {
        this.processorName = processorName;
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        try {
            Object result = ProcessorUtils.processCollection(value, this::processValue);
            return result;
        } catch (Exception e) {
            ProcessorUtils.logProcessResult(processorName, value, null, e.getMessage());
            return value;
        }
    }

    /**
     * 处理单个值
     * 子类必须实现这个方法来提供具体的处理逻辑
     *
     * @param value 要处理的值
     * @return 处理后的值
     */
    protected abstract Object processValue(Object value);
} 