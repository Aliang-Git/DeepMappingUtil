package com.aliang.logger.impl;

import com.aliang.logger.*;
import org.slf4j.*;

/**
 * 默认服务层日志实现
 */
public class DefaultServiceLogger implements ServiceLogger {
    private static final Logger log = LoggerFactory.getLogger("Service");

    @Override
    public void logConfigSaved(String productCode) {
        log.info("配置保存成功 - 产品编码: {}", productCode);
    }

    @Override
    public void logConfigSaveFailure(String productCode, String error) {
        log.error("配置保存失败 - 产品编码: {}, 错误: {}", productCode, error);
    }

    @Override
    public void logMappingFailure(String productCode, String error) {
        log.error("映射处理失败 - 产品编码: {}, 错误: {}", productCode, error);
    }

    @Override
    public void logConfigGetFailure(String productCode, String error) {
        log.error("配置获取失败 - 产品编码: {}, 错误: {}", productCode, error);
    }
} 