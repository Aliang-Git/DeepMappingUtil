package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;

/**
 * 折扣处理器
 * 对数值应用折扣率进行计算
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
public class DiscountProcessor implements ValueProcessor {
    private final double discountRate;

    public DiscountProcessor(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null; // 空值直接返回
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue() * discountRate;
        }
        try {
            // 尝试将字符串转为 double
            double number = Double.parseDouble(value.toString());
            return number * discountRate;
        } catch (NumberFormatException e) {
            // 无法转换为数字时返回原值，而不是抛异常
            return value;
        }
    }
}
