package com.aliang.test;

import org.slf4j.*;

public class TestLog {
    private static final Logger logger = LoggerFactory.getLogger(TestLog.class);

    public static void main(String[] args) {
        logger.debug("测试日志：{}", "Hello World");
        logger.warn("警告信息：{}", "Something went wrong");
    }
}
