package com.aliang.logger;

/**
 * 服务层日志接口
 * 专门用于记录服务层的日志
 */
public interface ServiceLogger {
    /**
     * 记录配置保存成功
     *
     * @param productCode 产品编码
     */
    void logConfigSaved(String productCode);

    /**
     * 记录配置保存失败
     *
     * @param productCode 产品编码
     * @param error       错误信息
     */
    void logConfigSaveFailure(String productCode, String error);

    /**
     * 记录映射处理失败
     *
     * @param productCode 产品编码
     * @param error       错误信息
     */
    void logMappingFailure(String productCode, String error);

    /**
     * 记录配置获取失败
     *
     * @param productCode 产品编码
     * @param error       错误信息
     */
    void logConfigGetFailure(String productCode, String error);
} 