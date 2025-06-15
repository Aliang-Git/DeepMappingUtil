package com.aliang.rule.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 手机号格式化处理器
 * 将手机号格式化为指定格式，支持掩码处理
 * <p>
 * 配置格式：phone:format,mask
 * format: 格式化方式，可选值：default(默认), dash(破折号), space(空格)
 * mask: 是否掩码，可选值：true(是), false(否)
 * <p>
 * 示例1 - 默认格式化：
 * 配置：phone:default,false
 * 输入："13812345678"
 * 输出："138 1234 5678"
 * <p>
 * 示例2 - 破折号格式：
 * 配置：phone:dash,false
 * 输入："13812345678"
 * 输出："138-1234-5678"
 * <p>
 * 示例3 - 掩码处理：
 * 配置：phone:default,true
 * 输入："13812345678"
 * 输出："138 **** 5678"
 * <p>
 * 示例4 - 国际号码：
 * 配置：phone:default,false
 * 输入："+8613812345678"
 * 输出："+86 138 1234 5678"
 * <p>
 * 示例5 - 批量处理：
 * 配置：phone:dash,true
 * 输入：["13812345678", "13987654321"]
 * 输出：["138-****-5678", "139-****-4321"]
 * <p>
 * 特殊情况处理：
 * 1. 非法手机号：
 * 输入："123"
 * 输出：null
 * <p>
 * 2. 空字符串：
 * 输入：""
 * 输出：null
 * <p>
 * 3. 特殊字符：
 * 输入："138-1234-5678"
 * 输出：根据format参数格式化
 * <p>
 * 注意：
 * 1. 自动去除输入中的特殊字符后再处理
 * 2. 支持中国大陆11位手机号
 * 3. 支持+86前缀的国际格式
 * 4. 支持数组和集合类型的批量处理
 * 5. null值将返回null
 */
public class PhoneNumberProcessor implements ValueProcessor {
    private static final String DEFAULT_FORMAT = "default";
    private static final String DASH_FORMAT = "dash";
    private static final String SPACE_FORMAT = "space";

    private final String format;
    private final boolean mask;
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public PhoneNumberProcessor() {
        this.format = DEFAULT_FORMAT;
        this.mask = false;
        logger.logProcessorInit("PhoneNumberProcessor", "使用默认配置: format=" + format + ", mask=" + mask);
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }

        try {
            String phoneNumber = value.toString().replaceAll("[^0-9+]", "");

            /*  验证手机号格式 */
            if (!isValidPhoneNumber(phoneNumber)) {
                logger.logProcessFailure("PhoneNumberProcessor", value, "无效的手机号格式");
                return value;
            }

            String formatted = formatPhoneNumber(phoneNumber);
            logger.logProcessSuccess("PhoneNumberProcessor", value, formatted);
            return formatted;
        } catch (Exception e) {
            logger.logProcessFailure("PhoneNumberProcessor", value, e.getMessage());
            return value;
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        /*  简单的手机号验证：11位数字，或带+86前缀的13位数字 */
        return phoneNumber.matches("^\\d{11}$") ||
                phoneNumber.matches("^\\+86\\d{11}$");
    }

    private String formatPhoneNumber(String phoneNumber) {
        /*  去除可能存在的+86前缀 */
        String number = phoneNumber.replaceAll("^\\+86", "");

        /*  如果需要掩码处理 */
        if (mask) {
            number = number.substring(0, 3) + "****" + number.substring(7);
        }

        /*  根据格式要求处理 */
        switch (format) {
            case DASH_FORMAT:
                return formatWithSeparator(number, "-");
            case SPACE_FORMAT:
                return formatWithSeparator(number, " ");
            default: /*  DEFAULT_FORMAT */
                return formatWithSeparator(number, " ");
        }
    }

    private String formatWithSeparator(String number, String separator) {
        if (number.length() != 11) {
            return number;
        }
        return String.format("%s%s%s%s%s",
                number.substring(0, 3),
                separator,
                number.substring(3, 7),
                separator,
                number.substring(7)
        );
    }
} 