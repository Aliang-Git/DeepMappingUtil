package com.aliang.logger.imple;

import com.aliang.logger.*;
import org.slf4j.*;

public class Slf4jMappingLogger implements MappingLogger {
    private final Logger log = LoggerFactory.getLogger("MappingEngine");

    @Override
    public void warn(String message, Object... args) {
        log.warn(message, args);
    }

    @Override
    public void error(String message, Throwable t) {
        log.error(message, t);
    }

    @Override
    public void debug(String message, Object... args) {
        log.debug(message, args);
    }

    @Override
    public void info(String message, Object... args) {
        log.info(message, args);
    }
}
