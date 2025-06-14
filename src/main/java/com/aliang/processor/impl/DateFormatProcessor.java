package com.aliang.processor.impl;

import com.aliang.processor.*;

import java.text.*;
import java.util.*;

/**
 * 日期格式转换
 */
public class DateFormatProcessor implements ValueProcessor {
    private final String pattern;

    public DateFormatProcessor(String pattern) {
        this.pattern = pattern != null ? pattern : "yyyy-MM-dd HH:mm:ss";
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
                // 尝试解析输入日期字符串
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = inputFormat.parse(dateStr);
                // 使用指定的格式输出
                SimpleDateFormat outputFormat = new SimpleDateFormat(pattern);
                return outputFormat.format(date);
            }
            return value.toString();
        } catch (Exception e) {
            return value.toString();
        }
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