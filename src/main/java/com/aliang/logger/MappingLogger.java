package com.aliang.logger;

/**
 * 映射处理日志接口
 */
public interface MappingLogger {
    void warn(String message, Object... args);

    void error(String message, Object... args);

    void error(String message, Throwable t);

    void debug(String message, Object... args);

    void info(String message, Object... args);
}
