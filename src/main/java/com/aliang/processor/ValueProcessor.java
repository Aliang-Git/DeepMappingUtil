package com.aliang.processor;

/**
 * 值处理器接口
 * 定义了处理器的基本行为
 */
public interface ValueProcessor {
    /**
     * 处理值
     * 这是一个默认方法，它调用 doProcess 方法来实现实际的处理逻辑
     *
     * @param value 要处理的值
     * @return 处理后的值
     */
    default Object process(Object value) {
        return doProcess(value);
    }

    /**
     * 实际的处理逻辑
     * 子类必须实现这个方法来提供具体的处理逻辑
     *
     * @param value 要处理的值
     * @return 处理后的值
     */
    Object doProcess(Object value);
}
