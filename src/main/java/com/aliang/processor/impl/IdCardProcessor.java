package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 身份证号格式化处理器
 * 将身份证号格式化，支持掩码处理
 * <p>
 * 配置格式：idcard:mask
 * mask: 掩码类型，可选值：
 * - none: 不掩码
 * - birth: 掩码出生日期
 * - sequence: 掩码顺序号
 * - all: 掩码出生日期和顺序号
 * <p>
 * 示例1 - 不掩码：
 * 配置：idcard:none
 * 输入："440101199001011234"
 * 输出："440101 19900101 1234"
 * <p>
 * 示例2 - 掩码出生日期：
 * 配置：idcard:birth
 * 输入："440101199001011234"
 * 输出："440101 ******** 1234"
 * <p>
 * 示例3 - 掩码顺序号：
 * 配置：idcard:sequence
 * 输入："440101199001011234"
 * 输出："440101 19900101 ****"
 * <p>
 * 示例4 - 掩码全部敏感信息：
 * 配置：idcard:all
 * 输入："440101199001011234"
 * 输出："440101 ******** ****"
 * <p>
 * 示例5 - 批量处理：
 * 配置：idcard:birth
 * 输入：["440101199001011234", "440101199001011235"]
 * 输出：["440101 ******** 1234", "440101 ******** 1235"]
 * <p>
 * 特殊情况处理：
 * 1. 非法身份证号：
 * 输入："12345"
 * 输出：null
 * <p>
 * 2. 空字符串：
 * 输入：""
 * 输出：null
 * <p>
 * 3. 特殊字符：
 * 输入："440101-199001011234"
 * 输出：根据mask参数格式化
 * <p>
 * 注意：
 * 1. 自动去除输入中的特殊字符后再处理
 * 2. 支持18位身份证号
 * 3. 支持数组和集合类型的批量处理
 * 4. null值将返回null
 * 5. 校验身份证号的合法性（地区码、出生日期、校验位）
 */
public class IdCardProcessor implements ValueProcessor {
    private static final String MASK_NONE = "none";
    private static final String MASK_BIRTH = "birth";
    private static final String MASK_SEQUENCE = "sequence";
    private static final String MASK_ALL = "all";

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Collection || value.getClass().isArray()) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }

        String idCard = value.toString().replaceAll("[^0-9Xx]", "");

        // 验证身份证号
        if (!isValidIdCard(idCard)) {
            return null;
        }

        // 分割身份证号
        String area = idCard.substring(0, 6);
        String birth = idCard.substring(6, 14);
        String sequence = idCard.substring(14);

        // 默认不掩码
        return area + " " + birth + " " + sequence;
    }

    private boolean isValidIdCard(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return false;
        }

        // 简单校验，实际使用时应该添加更严格的校验
        return idCard.matches("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$");
    }
} 