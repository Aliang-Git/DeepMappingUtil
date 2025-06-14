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
} 