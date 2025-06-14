package com.aliang.logger;

/**
 * 处理器日志接口
 * 专门用于记录处理器相关的日志
 */
public interface ProcessorLogger {
    /**
     * 记录处理器初始化信息
     *
     * @param processorName 处理器名称
     * @param config        配置信息
     */
    void logProcessorInit(String processorName, String config);

    /**
     * 记录处理器配置无效
     *
     * @param processorName 处理器名称
     * @param config        配置信息
     * @param defaultValue  默认值
     */
    void logInvalidConfig(String processorName, String config, Object defaultValue);

    /**
     * 记录处理器创建失败
     *
     * @param processorName 处理器名称
     * @param error         错误信息
     */
    void logProcessorCreationFailure(String processorName, String error);

    /**
     * 记录处理器参数错误
     *
     * @param processorName 处理器名称
     * @param message       错误信息
     */
    void logProcessorParamError(String processorName, String message);

    /**
     * 记录处理器处理成功
     *
     * @param processorName 处理器名称
     * @param input         输入值
     * @param output        输出值
     */
    void logProcessSuccess(String processorName, Object input, Object output);

    /**
     * 记录处理器处理失败
     *
     * @param processorName 处理器名称
     * @param input         输入值
     * @param error         错误信息
     */
    void logProcessFailure(String processorName, Object input, String error);
} 