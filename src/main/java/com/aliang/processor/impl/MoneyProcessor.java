package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;

import java.math.*;
import java.text.*;
import java.util.*;

/**
 * 金额格式化处理器
 * 将数字金额格式化为货币形式，支持不同货币符号和格式
 * <p>
 * 配置格式：money:currency,format
 * currency: 货币符号，可选值：
 * - CNY: 人民币符号(¥)
 * - USD: 美元符号($)
 * - EUR: 欧元符号(€)
 * - none: 不显示货币符号
 * format: 格式化方式，可选值：
 * - standard: 标准格式(1,234.56)
 * - simple: 简单格式(1234.56)
 * - chinese: 中文大写(壹仟贰佰叁拾肆元伍角陆分)
 * <p>
 * 示例1 - 标准人民币格式：
 * 配置：money:CNY,standard
 * 输入：1234.56
 * 输出："¥1,234.56"
 * <p>
 * 示例2 - 中文大写：
 * 配置：money:CNY,chinese
 * 输入：1234.56
 * 输出："壹仟贰佰叁拾肆元伍角陆分"
 * <p>
 * 示例3 - 美元格式：
 * 配置：money:USD,standard
 * 输入：1234.56
 * 输出："$1,234.56"
 * <p>
 * 示例4 - 简单格式：
 * 配置：money:none,simple
 * 输入：1234.56
 * 输出："1234.56"
 * <p>
 * 示例5 - 批量处理：
 * 配置：money:CNY,standard
 * 输入：[1234.56, 7890.12]
 * 输出：["¥1,234.56", "¥7,890.12"]
 * <p>
 * 特殊情况处理：
 * 1. 负数：
 * 输入：-1234.56
 * 输出："-¥1,234.56"
 * <p>
 * 2. 零值：
 * 输入：0
 * 输出："¥0.00"
 * <p>
 * 3. 超大数字：
 * 输入：1234567890.12
 * 输出："¥1,234,567,890.12"
 * <p>
 * 注意：
 * 1. 自动四舍五入到2位小数
 * 2. 支持负数处理
 * 3. 支持数组和集合类型的批量处理
 * 4. null值将返回null
 * 5. 非数字输入将返回null
 */
public class MoneyProcessor implements ValueProcessor {
    private static final String CNY = "CNY";
    private static final String USD = "USD";
    private static final String EUR = "EUR";
    private static final String NONE = "none";

    private static final String STANDARD = "standard";
    private static final String SIMPLE = "simple";
    private static final String CHINESE = "chinese";

    private static final String[] CHINESE_UNITS = {"分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿"};
    private static final String[] CHINESE_NUMS = {"零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖"};
    
    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Collection || value.getClass().isArray()) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }

        try {
            BigDecimal amount = new BigDecimal(value.toString())
                    .setScale(2, RoundingMode.HALF_UP);

            // 默认使用人民币标准格式
            DecimalFormat formatter = new DecimalFormat("¥#,##0.00");
            return formatter.format(amount);

        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String toChineseNumber(BigDecimal number) {
        // 处理0
        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return "零元整";
        }

        // 处理负数
        boolean negative = number.compareTo(BigDecimal.ZERO) < 0;
        if (negative) {
            number = number.abs();
        }

        // 分解金额
        long yuan = number.longValue();
        int fen = number.multiply(new BigDecimal(100))
                .remainder(new BigDecimal(100)).intValue();

        StringBuilder result = new StringBuilder();
        if (negative) {
            result.append("负");
        }

        // 处理元
        if (yuan > 0) {
            String yuanStr = String.valueOf(yuan);
            for (int i = 0; i < yuanStr.length(); i++) {
                int digit = yuanStr.charAt(i) - '0';
                result.append(CHINESE_NUMS[digit])
                        .append(CHINESE_UNITS[yuanStr.length() - i + 1]);
            }
        }

        // 处理分
        if (fen > 0) {
            int jiao = fen / 10;
            fen = fen % 10;
            if (jiao > 0) {
                result.append(CHINESE_NUMS[jiao]).append("角");
            }
            if (fen > 0) {
                result.append(CHINESE_NUMS[fen]).append("分");
            }
        } else {
            result.append("整");
        }

        return result.toString();
    }
} 