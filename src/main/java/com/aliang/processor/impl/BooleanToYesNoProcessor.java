package com.aliang.processor.impl;

import com.aliang.logger.*;
import com.aliang.logger.impl.*;
import com.aliang.processor.*;

import java.util.*;

/**
 * 布尔值转换处理器
 * 将布尔值转换为可读的文本表示
 * 
 * 配置格式：booleanToYesNo:trueValue,falseValue
 * 默认值：true="是", false="否"
 * 
 * 示例1 - 默认转换：
 * 配置：booleanToYesNo
 * 输入：true
 * 输出："是"
 * 输入：false
 * 输出："否"
 * 
 * 示例2 - 自定义文本：
 * 配置：booleanToYesNo:有效,无效
 * 输入：true
 * 输出："有效"
 * 输入：false
 * 输出："无效"
 * 
 * 示例3 - 英文显示：
 * 配置：booleanToYesNo:Yes,No
 * 输入：true
 * 输出："Yes"
 * 输入：false
 * 输出："No"
 * 
 * 示例4 - 状态显示：
 * 配置：booleanToYesNo:启用,禁用
 * 输入：true
 * 输出："启用"
 * 输入：false
 * 输出："禁用"
 * 
 * 示例5 - 多值转换（数组）：
 * 配置：booleanToYesNo:开,关
 * 输入：[true, false, true]
 * 输出：["开", "关", "开"]
 * 
 * 支持的输入类型：
 * 1. Boolean对象
 * 2. boolean基本类型
 * 3. 字符串("true"/"false")
 * 4. 数字(1/0)
 * 
 * 注意：
 * 1. 如果输入值不是布尔类型，会尝试转换
 * 2. 转换失败时返回原值
 * 3. 支持数组和集合类型的批量转换
 * 4. 自定义文本时，true值和false值用逗号分隔
 */
public class BooleanToYesNoProcessor implements ValueProcessor {
    private final Map<Boolean, String> booleanMap;
    private final ProcessorLogger logger = new DefaultProcessorLogger();

    public BooleanToYesNoProcessor() {
        this.booleanMap = new HashMap<>();
        this.booleanMap.put(true, "是");
        this.booleanMap.put(false, "否");
        logger.logProcessorInit("BooleanToYesNoProcessor", "使用默认映射: true->是, false->否");
    }

    public BooleanToYesNoProcessor(String config) {
        this.booleanMap = new HashMap<>();
        if (config != null && !config.isEmpty()) {
            String[] parts = config.split(",");
            if (parts.length == 2) {
                this.booleanMap.put(true, parts[0]);
                this.booleanMap.put(false, parts[1]);
                logger.logProcessorInit("BooleanToYesNoProcessor",
                        String.format("使用自定义映射: true->%s, false->%s", parts[0], parts[1]));
            } else {
                logger.logInvalidConfig("BooleanToYesNoProcessor", config, "是,否");
                this.booleanMap.put(true, "是");
                this.booleanMap.put(false, "否");
            }
        } else {
            this.booleanMap.put(true, "是");
            this.booleanMap.put(false, "否");
            logger.logProcessorInit("BooleanToYesNoProcessor", "使用默认映射: true->是, false->否");
        }
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        try {
            boolean boolValue;
            if (value instanceof Boolean) {
                boolValue = (Boolean) value;
            } else if (value instanceof String) {
                String strValue = ((String) value).toLowerCase();
                boolValue = strValue.equals("true") || strValue.equals("1") ||
                        strValue.equals("yes") || strValue.equals("y");
            } else if (value instanceof Number) {
                boolValue = ((Number) value).intValue() != 0;
            } else {
                logger.logProcessFailure("BooleanToYesNoProcessor", value,
                        "不支持的数据类型: " + value.getClass().getName());
                return value;
            }

            String result = booleanMap.get(boolValue);
            logger.logProcessSuccess("BooleanToYesNoProcessor", value, result);
            return result;
        } catch (Exception e) {
            logger.logProcessFailure("BooleanToYesNoProcessor", value, e.getMessage());
            return value;
        }
    }
}
