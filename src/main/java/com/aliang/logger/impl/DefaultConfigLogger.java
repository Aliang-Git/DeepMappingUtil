package com.aliang.logger.impl;

import com.aliang.logger.*;
import org.slf4j.*;

/**
 * 默认配置日志实现
 */
public class DefaultConfigLogger implements ConfigLogger {
    private static final Logger log = LoggerFactory.getLogger("Config");

    @Override
    public void logParseError(String message) {
        log.error("配置解析错误 - {}", message);
    }

    @Override
    public void logValidationError(String message) {
        log.error("配置验证错误 - {}", message);
    }
} 