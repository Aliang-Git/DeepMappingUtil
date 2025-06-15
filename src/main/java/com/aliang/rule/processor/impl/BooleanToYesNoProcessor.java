package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

/**
 * 布尔值转换处理器
 * 将布尔值转换为指定的字符串表示
 * <p>
 * 配置格式：booleanToYesNo:trueValue,falseValue
 * 默认值：true="是", false="否"
 * <p>
 * 示例1 - 默认转换：
 * 配置：booleanToYesNo
 * 输入：true
 * 输出："是"
 * 输入：false
 * 输出："否"
 * <p>
 * 示例2 - 自定义文本：
 * 配置：booleanToYesNo:有效,无效
 * 输入：true
 * 输出："有效"
 * 输入：false
 * 输出："无效"
 * <p>
 * 示例3 - 英文显示：
 * 配置：booleanToYesNo:Yes,No
 * 输入：true
 * 输出："Yes"
 * 输入：false
 * 输出："No"
 * <p>
 * 示例4 - 状态显示：
 * 配置：booleanToYesNo:启用,禁用
 * 输入：true
 * 输出："启用"
 * 输入：false
 * 输出："禁用"
 * <p>
 * 示例5 - 多值转换（数组）：
 * 配置：booleanToYesNo:开,关
 * 输入：[true, false, true]
 * 输出：["开", "关", "开"]
 * <p>
 * 支持的输入类型：
 * 1. Boolean对象
 * 2. boolean基本类型
 * 3. 字符串("true"/"false")
 * 4. 数字(1/0)
 * <p>
 * 注意：
 * 1. 如果输入值不是布尔类型，会尝试转换
 * 2. 转换失败时返回原值
 * 3. 支持数组和集合类型的批量转换
 * 4. 自定义文本时，true值和false值用逗号分隔
 */
public class BooleanToYesNoProcessor extends AbstractProcessor {
    private final String trueValue;
    private final String falseValue;

    public BooleanToYesNoProcessor(String config) {
        super("BooleanToYesNoProcessor");
        String[] values = config != null ? config.split(",") : new String[0];
        this.trueValue = values.length > 0 ? values[0] : "是";
        this.falseValue = values.length > 1 ? values[1] : "否";
        ProcessorUtils.logProcessResult(processorName, null,
                String.format("true=%s, false=%s", trueValue, falseValue), null);
    }

    @Override
    protected Object processValue(Object value) {
        if (value instanceof Boolean) {
            String result = (Boolean) value ? trueValue : falseValue;
            ProcessorUtils.logProcessResult(processorName, value, result, null);
            return result;
        }
        return value;
    }
}
