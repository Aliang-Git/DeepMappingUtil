package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.aliang.utils.*;

import java.util.*;

/**
 * 映射值处理器
 * 根据映射表将输入值转换为对应的输出值
 * <p>
 * 配置格式：mapvalue:key1=value1;key2=value2;key3=value3
 * <p>
 * 示例1 - 状态码转中文：
 * 配置：mapvalue:0=未开始;1=进行中;2=已完成;3=已取消
 * 输入：1
 * 输出："进行中"
 * <p>
 * 示例2 - 布尔值转显示文本：
 * 配置：mapvalue:true=是;false=否
 * 输入：true
 * 输出："是"
 * <p>
 * 示例3 - 数字等级转文字：
 * 配置：mapvalue:1=差;2=一般;3=良好;4=优秀;5=卓越
 * 输入：4
 * 输出："优秀"
 * <p>
 * 示例4 - 代码转业务含义：
 * 配置：mapvalue:REG=注册用户;VIP=会员用户;SVIP=超级会员
 * 输入："VIP"
 * 输出："会员用户"
 * <p>
 * 示例5 - 多值映射（数组）：
 * 配置：mapvalue:A=优;B=良;C=中;D=差
 * 输入：["A", "B", "A", "C"]
 * 输出：["优", "良", "优", "中"]
 * <p>
 * 注意：
 * 1. 如果输入值在映射表中不存在，将返回原值
 * 2. 映射表中的键值对使用分号(;)分隔
 * 3. 每个键值对使用等号(=)分隔
 * 4. 支持数组和集合类型的批量转换
 * 5. 键和值都支持任意字符串，包括中文
 */
public class MapValueProcessor extends AbstractProcessor {
    private final Map<String, String> mapping;

    public MapValueProcessor(String config) {
        super("MapValueProcessor");
        this.mapping = parseMappingConfig(config);
        ProcessorUtils.logProcessResult(processorName, null, "映射表: " + mapping, null);
    }

    private Map<String, String> parseMappingConfig(String config) {
        Map<String, String> map = new HashMap<>();
        if (config != null && !config.trim().isEmpty()) {
            String[] pairs = config.split(";");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    map.put(keyValue[0], keyValue[1]);
                } else {
                    ProcessorUtils.logProcessResult(processorName, pair, null, "无效的键值对格式");
                }
            }
        }
        return map;
    }

    @Override
    protected Object processValue(Object value) {
        String key = value.toString();
        String result = mapping.getOrDefault(key, key);
        ProcessorUtils.logProcessResult(processorName, value, result, null);
        return result;
    }
}
