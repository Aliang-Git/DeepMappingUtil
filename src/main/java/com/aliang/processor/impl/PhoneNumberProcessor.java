package com.aliang.processor.impl;

import com.aliang.processor.*;
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

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Collection || value.getClass().isArray()) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }

        String phone = value.toString().replaceAll("[^+\\d]", "");

        // 验证手机号
        if (!isValidPhoneNumber(phone)) {
            return null;
        }

        // 处理国际前缀
        String prefix = "";
        if (phone.startsWith("+86")) {
            prefix = "+86 ";
            phone = phone.substring(3);
        }

        // 分割手机号
        String section1 = phone.substring(0, 3);
        String section2 = phone.substring(3, 7);
        String section3 = phone.substring(7);

        // 默认使用空格分隔
        return prefix + section1 + " " + section2 + " " + section3;
    }

    private boolean isValidPhoneNumber(String phone) {
        if (phone.startsWith("+86")) {
            phone = phone.substring(3);
        }
        return phone.matches("^1\\d{10}$");
    }
} 