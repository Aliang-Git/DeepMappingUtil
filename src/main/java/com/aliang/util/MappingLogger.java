package com.aliang.util;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 映射处理日志工具类
 */
public class MappingLogger {
    private static final Logger logger = LoggerFactory.getLogger(MappingLogger.class);
    private static final String LOG_FORMAT = "[%s] %s";
    private static final String PROCESSOR_LOG = "字段处理器: %s, 处理前: %s, 处理后: %s";
    private static final String STRATEGY_LOG = "聚合处理器: %s, 聚合前: %s, 聚合后: %s";
    private static final String PATH_LOG = "路径映射: 从 %s 到 %s";
    private static final String PRODUCT_LOG = "产品代码: %s";

    /**
     * 记录字段处理器处理过程
     */
    public static void logProcessor(String productCode, String processorName, String sourcePath, Object beforeValue, Object afterValue) {
        logger.info("[产品:{}] 字段处理器:{}，源字段:{}，处理前:{}，处理后:{}", productCode, processorName, sourcePath, beforeValue, afterValue);
    }

    /**
     * 记录聚合处理器处理过程
     */
    public static void logAggregation(String productCode, String strategyName, String sourcePath, Object beforeValue, Object afterValue) {
        logger.info("[产品:{}] 聚合处理器:{}，字段:{}，聚合前:{}，聚合后:{}", productCode, strategyName, sourcePath, beforeValue, afterValue);
    }

    /**
     * 记录路径映射过程
     */
    public static void logPath(String sourcePath, String targetPath) {
        System.out.println(String.format(LOG_FORMAT, "PATH", 
            String.format(PATH_LOG, sourcePath, targetPath)));
    }

    /**
     * 记录产品信息
     */
    public static void logProduct(String productCode) {
        System.out.println(String.format(LOG_FORMAT, "PRODUCT", 
            String.format(PRODUCT_LOG, productCode)));
    }

    public static void logMapping(String productCode, String sourcePath, String targetPath, Object value) {
        logger.info("[产品:{}] 字段:{}，从路径:{}，转到路径:{}，最终值:{}", productCode, sourcePath, sourcePath, targetPath, value);
    }
} 