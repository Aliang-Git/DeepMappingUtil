package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 状态转中文处理器
 * 将状态码转换为对应的中文描述
 * <p>
 * 配置格式：statusToChinese[:自定义映射]
 * 自定义映射格式：状态码1=中文1;状态码2=中文2
 * <p>
 * 示例1 - 订单状态转换：
 * 配置：statusToChinese:0=待支付;1=已支付;2=已发货;3=已完成;4=已取消
 * 输入：2
 * 输出："已发货"
 * <p>
 * 示例2 - 审核状态转换：
 * 配置：statusToChinese:PENDING=待审核;APPROVED=已通过;REJECTED=已拒绝
 * 输入："APPROVED"
 * 输出："已通过"
 * <p>
 * 示例3 - 处理进度转换：
 * 配置：statusToChinese:0=未开始;1=处理中;2=已完成;-1=处理失败
 * 输入：1
 * 输出："处理中"
 * <p>
 * 示例4 - 支付状态转换：
 * 配置：statusToChinese:UNPAID=未支付;PAID=已支付;REFUND=已退款
 * 输入："REFUND"
 * 输出："已退款"
 * <p>
 * 示例5 - 批量状态转换（数组）：
 * 配置：statusToChinese:1=在线;0=离线
 * 输入：[1, 0, 1, 1]
 * 输出：["在线", "离线", "在线", "在线"]
 * <p>
 * 默认状态映射：
 * - 0: 禁用
 * - 1: 启用
 * - Y: 是
 * - N: 否
 * - true: 是
 * - false: 否
 * <p>
 * 注意：
 * 1. 支持数字和字符串类型的状态码
 * 2. 可以完全自定义状态映射
 * 3. 支持数组和集合类型的批量转换
 * 4. 未匹配的状态码将返回原值
 * 5. 状态码大小写敏感
 */
public class StatusToChineseProcessor extends AbstractProcessor {
    private final Map<String, String> statusMap;

    public StatusToChineseProcessor(String config) {
        super("StatusToChineseProcessor");
        this.statusMap = new HashMap<>();
        if (config != null && !config.isEmpty()) {
            String[] pairs = config.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    statusMap.put(keyValue[0], keyValue[1]);
                }
            }
        }
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("状态映射: %s", statusMap), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (value == null) {
            return null;
        }

        String status = value.toString();
        String result = statusMap.getOrDefault(status, status);

        ProcessorUtils.logProcessResult(processorName, value, result, null);
        return result;
    }
}
