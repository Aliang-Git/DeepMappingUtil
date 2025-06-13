package com.aliang.processor.impl;
import com.aliang.processor.*;
import com.aliang.utils.*;

import java.text.*;
import java.util.*;

/**
 * 日期格式转换
 */
public class DateFormatProcessor implements ValueProcessor {
    private final String pattern;

    public DateFormatProcessor(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Object process(Object value) {
        System.out.println("开始执行DateFormatProcessor，value为：" + value);

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::process);
        }

        if (value instanceof Date) {
            return formatDate((Date) value);
        }

        if (value instanceof String) {
            return tryParseAndFormat((String) value);
        }

        return value;
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    private String tryParseAndFormat(String strValue) {
        try {
            // 尝试解析 ISO 8601 格式时间字符串
            Date date = parseISO8601(strValue);
            return formatDate(date);
        } catch (Exception e) {
            // 不是时间字符串就忽略
            return strValue;
        }
    }

    private Date parseISO8601(String str) throws ParseException {
        // 支持 "2023-08-15T14:30:00Z" 这种格式
        if (str.length() == 20 && str.endsWith("Z")) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(str);
        } else if (str.contains("T")) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(str);
        } else {
            return new SimpleDateFormat("yyyy-MM-dd").parse(str);
        }
    }
}