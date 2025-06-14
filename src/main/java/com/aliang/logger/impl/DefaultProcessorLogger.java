package com.aliang.logger.impl;

import com.aliang.logger.*;
import org.slf4j.*;

/**
 * 默认处理器日志实现
 */
public class DefaultProcessorLogger implements ProcessorLogger {
    private static final Logger log = LoggerFactory.getLogger("Processor");

    @Override
    public void logProcessorInit(String processorName, String config) {
        log.debug("处理器初始化 - 处理器: {}, 配置: {}", processorName, config);
    }

    @Override
    public void logInvalidConfig(String processorName, String config, Object defaultValue) {
        log.warn("处理器配置无效 - 处理器: {}, 配置: {}, 使用默认值: {}",
                processorName, config, defaultValue);
    }

    @Override
    public void logProcessorCreationFailure(String processorName, String error) {
        log.error("处理器创建失败 - 处理器: {}, 错误: {}", processorName, error);
    }

    @Override
    public void logProcessorParamError(String processorName, String message) {
        log.warn("处理器参数错误 - 处理器: {}, 错误: {}", processorName, message);
    }

    @Override
    public void logProcessSuccess(String processorName, Object input, Object output) {
        log.debug("处理器处理成功 - 处理器: {}, 输入: {}, 输出: {}",
                processorName, input, output);
    }

    @Override
    public void logProcessFailure(String processorName, Object input, String error) {
        log.error("处理器处理失败 - 处理器: {}, 输入: {}, 错误: {}",
                processorName, input, error);
    }
} 