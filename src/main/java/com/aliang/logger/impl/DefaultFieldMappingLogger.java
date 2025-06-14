package com.aliang.logger.impl;

import com.aliang.logger.*;
import org.slf4j.*;

/**
 * 默认字段映射日志实现
 */
public class DefaultFieldMappingLogger implements FieldMappingLogger {
    private static final Logger log = LoggerFactory.getLogger("FieldMapping");

    @Override
    public void logProcessorFailure(String sourcePath, String targetPath, String processorName, Object sourceValue, String error) {
        log.warn("字段处理失败 - 源字段: {}, 目标字段: {}, 处理器: {}, 源值: {}, 错误: {}",
                sourcePath, targetPath, processorName, sourceValue, error);
    }

    @Override
    public void logStrategyFailure(String sourcePath, String targetPath, String strategyName, Object sourceValue, String error) {
        log.warn("聚合处理失败 - 源字段: {}, 目标字段: {}, 策略: {}, 源值: {}, 错误: {}",
                sourcePath, targetPath, strategyName, sourceValue, error);
    }

    @Override
    public void logInvalidValue(String sourcePath, String targetPath, Object sourceValue, String reason) {
        log.warn("字段值无效 - 源字段: {}, 目标字段: {}, 源值: {}, 原因: {}",
                sourcePath, targetPath, sourceValue, reason);
    }

    @Override
    public void logMappingSuccess(String sourcePath, String targetPath, Object value) {
        log.debug("字段映射成功 - 源字段: {}, 目标字段: {}, 映射值: {}",
                sourcePath, targetPath, value);
    }

    @Override
    public void logMappingFailure(String sourcePath, String targetPath, Object value, String error) {
        log.warn("字段映射失败 - 源字段: {}, 目标字段: {}, 映射值: {}, 错误: {}",
                sourcePath, targetPath, value, error);
    }
} 