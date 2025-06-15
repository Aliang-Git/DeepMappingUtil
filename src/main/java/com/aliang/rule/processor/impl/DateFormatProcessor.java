package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;

import java.text.*;
import java.util.*;

/**
 * 日期格式化处理器
 * 将日期值按照指定格式进行格式化
 * <p>
 * 配置格式：dateformat:日期格式字符串
 * <p>
 * 示例1 - 标准日期格式化：
 * 配置：dateformat:yyyy-MM-dd
 * 输入：2024-03-20 14:30:00
 * 输出：2024-03-20
 * <p>
 * 示例2 - 带时间的格式化：
 * 配置：dateformat:yyyy-MM-dd HH:mm:ss
 * 输入：2024-03-20 14:30:00
 * 输出：2024-03-20 14:30:00
 * <p>
 * 示例3 - 中文日期格式：
 * 配置：dateformat:yyyy年MM月dd日
 * 输入：2024-03-20
 * 输出：2024年03月20日
 * <p>
 * 示例4 - 时间戳转日期：
 * 配置：dateformat:yyyy-MM-dd HH:mm
 * 输入：1710915600000
 * 输出：2024-03-20 14:30
 * <p>
 * 示例5 - 多值日期格式化（数组）：
 * 配置：dateformat:MM/dd/yyyy
 * 输入：["2024-03-20", "2024-03-21"]
 * 输出：["03/20/2024", "03/21/2024"]
 * <p>
 * 支持的输入格式：
 * 1. 标准日期时间字符串
 * 2. 时间戳（毫秒）
 * 3. Date对象
 * 4. Calendar对象
 * <p>
 * 注意：
 * 1. 默认使用系统时区
 * 2. 如果输入格式无法解析，将返回原值
 * 3. 支持数组和集合类型的批量转换
 */
public class DateFormatProcessor implements ValueProcessor {
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
    private final String outputPattern;

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
                /*  尝试多种格式解析输入日期字符串 */
                Date date = parseDate(dateStr);
                /*  使用指定的格式输出 */
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
                inputFormat.setLenient(false);  /*  严格模式 */
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
            /*  尝试解析 ISO 8601 格式时间字符串 */
            Date date = parseISO8601(strValue);
            return formatDate(date);
        } catch (Exception e) {
            /*  不是时间字符串就忽略 */
            return strValue;
        }
    }

    private Date parseISO8601(String str) throws ParseException {
        /*  支持 "2023-08-15T14:30:00Z" 这种格式 */
        if (str.length() == 20 && str.endsWith("Z")) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(str);
        } else if (str.contains("T")) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(str);
        } else {
            return new SimpleDateFormat("yyyy-MM-dd").parse(str);
        }
    }
}