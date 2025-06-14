package com.aliang.rule.processor.impl;

import com.aliang.rule.processor.*;
import com.fasterxml.jackson.databind.*;

/**
 * JSON格式化处理器
 * 将对象转换为JSON字符串，支持美化输出
 * <p>
 * 配置格式：json:format,nulls
 * format: 是否美化输出，可选值：
 * - pretty: 美化输出
 * - compact: 紧凑输出
 * nulls: 是否包含null值，可选值：
 * - include: 包含null值
 * - exclude: 排除null值
 * <p>
 * 示例1 - 基本对象转换：
 * 配置：json:compact,exclude
 * 输入：{"name": "John", "age": 30}
 * 输出：'{"name":"John","age":30}'
 * <p>
 * 示例2 - 美化输出：
 * 配置：json:pretty,exclude
 * 输入：{"name": "John", "age": 30}
 * 输出：
 * {
 * "name": "John",
 * "age": 30
 * }
 * <p>
 * 示例3 - 包含null值：
 * 配置：json:compact,include
 * 输入：{"name": "John", "email": null}
 * 输出：'{"name":"John","email":null}'
 * <p>
 * 示例4 - 复杂对象：
 * 配置：json:pretty,exclude
 * 输入：{"user": {"name": "John", "roles": ["admin", "user"]}}
 * 输出：
 * {
 * "user": {
 * "name": "John",
 * "roles": ["admin", "user"]
 * }
 * }
 * <p>
 * 示例5 - 数组处理：
 * 配置：json:compact,exclude
 * 输入：[{"id": 1}, {"id": 2}]
 * 输出：'[{"id":1},{"id":2}]'
 * <p>
 * 特殊情况处理：
 * 1. 非法JSON：
 * 输入："invalid json"
 * 输出：null
 * <p>
 * 2. 空对象：
 * 输入：{}
 * 输出："{}"
 * <p>
 * 3. 空数组：
 * 输入：[]
 * 输出："[]"
 * <p>
 * 注意：
 * 1. 支持任意Java对象的JSON序列化
 * 2. 支持嵌套对象和数组
 * 3. 支持日期类型的标准格式化
 * 4. null输入将返回null
 * 5. 非法JSON将返回null
 */
public class JsonProcessor implements ValueProcessor {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public Object doProcess(Object value) {
        if (value == null) {
            return null;
        }

        try {
            // 默认使用紧凑格式
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            return null;
        }
    }
} 