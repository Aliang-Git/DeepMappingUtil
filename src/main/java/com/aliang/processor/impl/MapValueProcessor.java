package com.aliang.processor.impl;

import com.aliang.processor.*;
import com.aliang.utils.*;
import org.slf4j.*;

import java.util.*;

/**
 * 值映射处理器
 * 将输入值映射到预定义的目标值
 * 
 * 配置格式：mapvalue:key1=value1;key2=value2;key3=value3
 * 
 * 示例1 - 状态码转中文：
 * 配置：mapvalue:0=未开始;1=进行中;2=已完成;3=已取消
 * 输入：1
 * 输出："进行中"
 * 
 * 示例2 - 布尔值转显示文本：
 * 配置：mapvalue:true=是;false=否
 * 输入：true
 * 输出："是"
 * 
 * 示例3 - 数字等级转文字：
 * 配置：mapvalue:1=差;2=一般;3=良好;4=优秀;5=卓越
 * 输入：4
 * 输出："优秀"
 * 
 * 示例4 - 代码转业务含义：
 * 配置：mapvalue:REG=注册用户;VIP=会员用户;SVIP=超级会员
 * 输入："VIP"
 * 输出："会员用户"
 * 
 * 示例5 - 多值映射（数组）：
 * 配置：mapvalue:A=优;B=良;C=中;D=差
 * 输入：["A", "B", "A", "C"]
 * 输出：["优", "良", "优", "中"]
 * 
 * 注意：
 * 1. 如果输入值在映射表中不存在，将返回原值
 * 2. 映射表中的键值对使用分号(;)分隔
 * 3. 每个键值对使用等号(=)分隔
 * 4. 支持数组和集合类型的批量转换
 * 5. 键和值都支持任意字符串，包括中文
 */
public class MapValueProcessor implements ValueProcessor {
    private final Map<String, String> mapping;

    public MapValueProcessor(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    @Override
    public Object doProcess(Object value) {
        if (value instanceof List<?> || value instanceof Map<?, ?>) {
            return ProcessorUtils.processCollection(value, this::doProcess);
        }
        if (value == null) {
            return null;
        }
        String key = value.toString();
        // 查找映射，如果找不到则返回原值
        return mapping.getOrDefault(key, key);
    }
}
