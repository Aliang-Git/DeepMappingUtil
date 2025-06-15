package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.text.*;
import java.util.*;

/**
 * 日期格式化处理器
 * 将日期字符串按照指定格式进行转换
 */
public class DateFormatProcessor extends AbstractProcessor {
    private final SimpleDateFormat inputFormat;
    private final SimpleDateFormat outputFormat;

    public DateFormatProcessor(String config) {
        super("DateFormatProcessor");
        String[] formats = config != null ? config.split("->") : new String[]{"yyyy-MM-dd", "yyyy-MM-dd"};
        this.inputFormat = new SimpleDateFormat(formats[0].trim());
        this.outputFormat = new SimpleDateFormat(formats.length > 1 ? formats[1].trim() : formats[0].trim());

        ProcessorUtils.logProcessResult(processorName, null,
                String.format("输入格式: '%s', 输出格式: '%s'",
                        inputFormat.toPattern(), outputFormat.toPattern()), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (value == null) {
            return null;
        }

        try {
            Date date;
            if (value instanceof Date) {
                date = (Date) value;
            } else {
                date = inputFormat.parse(value.toString());
            }

            String result = outputFormat.format(date);
            ProcessorUtils.logProcessResult(processorName, value, result, null);
            return result;
        } catch (ParseException e) {
            ProcessorUtils.logProcessResult(processorName, value, value, e.getMessage());
            return value;
        }
    }
}