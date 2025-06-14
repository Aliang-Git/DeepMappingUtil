package com.aliang.processor.impl;

import com.aliang.processor.*;

import java.text.*;
import java.util.*;

/**
 * 日期格式转换处理器
 * 支持通过 dateFormat:pattern 格式配置输出格式
 * 例如：dateFormat:yyyy年MM月dd日 HH:mm:ss
 * 
 * 输入格式支持：
 * - yyyy-MM-dd HH:mm:ss
 * - yyyy-MM-dd HH:mm
 * - yyyy-MM-dd HH
 * - yyyy-MM-dd
 * - yyyy年MM月dd日 HH:mm:ss
 * - yyyy年MM月dd日 HH:mm
 * - yyyy年MM月dd日 HH
 * - yyyy年MM月dd日
 */
public class DateFormatProcessor implements ValueProcessor {
    private final String outputPattern;
    private static final String DEFAULT_OUTPUT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final List<String> INPUT_PATTERNS = Arrays.asList(
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd HH:mm",
        "yyyy-MM-dd HH",
        "yyyy-MM-dd",
        "yyyy年MM月dd日 HH:mm:ss",
        "yyyy年MM月dd日 HH:mm",
        "yyyy年MM月dd日 HH",
        "yyyy年MM月dd日"
    );

    public DateFormatProcessor(String config) {
        if (config != null && !config.isEmpty()) {
            this.outputPattern = config;
        } else {
            this.outputPattern = DEFAULT_OUTPUT_PATTERN;
        }
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof List) {
            List<?> list = (List<?>) value;
            List<String> result = new ArrayList<>();
            for (Object item : list) {
                result.add(formatDate(item));
            }
            return result;
        }

        return formatDate(value);
    }

    private String formatDate(Object value) {
        if (value == null) {
            return null;
        }

        try {
            if (value instanceof String) {
                String dateStr = (String) value;
                // 尝试多种格式解析输入日期字符串
                Date date = parseDate(dateStr);
                // 使用指定的格式输出
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                return outputFormat.format(date);
            }
            return value.toString();
        } catch (Exception e) {
            return value.toString();
        }
    }

    private Date parseDate(String dateStr) throws ParseException {
        ParseException lastException = null;
        for (String inputPattern : INPUT_PATTERNS) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                inputFormat.setLenient(false);  // 严格模式
                return inputFormat.parse(dateStr);
            } catch (ParseException e) {
                lastException = e;
            }
        }
        throw lastException;
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(outputPattern);
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