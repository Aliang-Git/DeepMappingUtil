package com.aliang.processor;

/**
 * 值处理器接口
 * 定义了处理器的基本行为
 */
public interface ValueProcessor {

    /**
     * 实际的处理逻辑
     * 子类必须实现这个方法来提供具体的处理逻辑
     *
     * @param value 要处理的值
     * @return 处理后的值
     */
    Object doProcess(Object value);
}
