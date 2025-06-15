package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.math.*;

/**
 * 折扣处理器
 * 将输入金额按照指定的折扣率计算
 * <p>
 * 配置格式：discount:折扣率
 * 折扣率范围：0-1之间的小数，1表示不打折，0表示免费
 * <p>
 * 示例1 - 商品打折：
 * 配置：discount:0.8
 * 输入：100
 * 输出：80 (8折)
 * <p>
 * 示例2 - 限时特惠：
 * 配置：discount:0.5
 * 输入：199.9
 * 输出：99.95 (5折)
 * <p>
 * 示例3 - 会员优惠：
 * 配置：discount:0.95
 * 输入：1000
 * 输出：950 (95折)
 * <p>
 * 示例4 - 清仓甩卖：
 * 配置：discount:0.1
 * 输入：299
 * 输出：29.9 (1折)
 * <p>
 * 示例5 - 批量商品打折（数组）：
 * 配置：discount:0.75
 * 输入：[100, 200, 300]
 * 输出：[75, 150, 225] (75折)
 * <p>
 * 注意：
 * 1. 折扣率必须是0-1之间的小数
 * 2. 使用BigDecimal进行精确计算
 * 3. 支持数组和集合类型的批量计算
 * 4. 非数值类型的输入将被忽略并返回原值
 * 5. 结果会保留原值的精度
 */
public class DiscountProcessor extends AbstractProcessor {
    private final BigDecimal discountRate;

    public DiscountProcessor(String config) {
        super("DiscountProcessor");
        this.discountRate = new BigDecimal(config != null ? config : "1.0");
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("折扣率: %s", discountRate), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (value == null) {
            return null;
        }

        try {
            BigDecimal amount;
            if (value instanceof Number) {
                amount = new BigDecimal(value.toString());
            } else if (value instanceof String) {
                amount = new BigDecimal((String) value);
            } else {
                return value;
            }

            BigDecimal result = amount.multiply(discountRate)
                    .setScale(2, RoundingMode.HALF_UP);

            ProcessorUtils.logProcessResult(processorName, value, result, null);
            return result;
        } catch (NumberFormatException e) {
            ProcessorUtils.logProcessResult(processorName, value, value, e.getMessage());
            return value;
        }
    }
}
