package com.aliang.logger;

/**
 * 配置日志接口
 * 专门用于记录配置相关的日志
 */
public interface ConfigLogger {
    /**
     * 记录配置解析错误
     *
     * @param message 错误信息
     */
    void logParseError(String message);

    /**
     * 记录配置验证错误
     *
     * @param message 错误信息
     */
    void logValidationError(String message);
} 