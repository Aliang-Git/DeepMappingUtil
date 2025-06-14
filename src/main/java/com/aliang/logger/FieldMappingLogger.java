package com.aliang.logger;

/**
 * 字段映射日志接口
 * 专门用于记录字段映射过程中的问题
 */
public interface FieldMappingLogger {
    /**
     * 记录处理器执行失败的情况
     *
     * @param sourcePath    源字段路径
     * @param targetPath    目标字段路径
     * @param processorName 处理器名称
     * @param sourceValue   源字段值
     * @param error         错误信息
     */
    void logProcessorFailure(String sourcePath, String targetPath, String processorName, Object sourceValue, String error);

    /**
     * 记录聚合策略执行失败的情况
     *
     * @param sourcePath   源字段路径
     * @param targetPath   目标字段路径
     * @param strategyName 策略名称
     * @param sourceValue  源字段值
     * @param error        错误信息
     */
    void logStrategyFailure(String sourcePath, String targetPath, String strategyName, Object sourceValue, String error);

    /**
     * 记录字段值无效的情况
     *
     * @param sourcePath  源字段路径
     * @param targetPath  目标字段路径
     * @param sourceValue 源字段值
     * @param reason      原因
     */
    void logInvalidValue(String sourcePath, String targetPath, Object sourceValue, String reason);

    /**
     * 记录映射成功的情况
     *
     * @param sourcePath 源字段路径
     * @param targetPath 目标字段路径
     * @param value      映射后的值
     */
    void logMappingSuccess(String sourcePath, String targetPath, Object value);

    /**
     * 记录映射失败的情况
     *
     * @param sourcePath 源字段路径
     * @param targetPath 目标字段路径
     * @param value      映射的值
     * @param error      错误信息
     */
    void logMappingFailure(String sourcePath, String targetPath, Object value, String error);
} 