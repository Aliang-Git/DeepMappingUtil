# 数据映射转换工具

## 项目介绍
这是一个用于数据映射和转换的工具，支持复杂的字段映射、值处理和聚合操作。

## 系统架构
系统由以下核心组件组成：
- 映射引擎：负责执行字段映射和转换
- 处理器链：处理字段值的转换
- 聚合策略：处理集合类型数据的聚合操作

## 快速开始
1. 添加依赖
```xml
<dependency>
    <groupId>com.aliang</groupId>
    <artifactId>data-mapping</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. 创建映射配置
```json
{
  "mappings": [
    {
      "source": "user.name",
      "target": "fullName",
      "processors": ["prefix:USER_"]
    },
    {
      "source": "orders",
      "target": "totalAmount",
      "aggregation": "sum",
      "processors": ["roundtwodecimal"]
    }
  ]
}
```

3. 执行转换
```java
DataMapper mapper = new DataMapper();
Object result = mapper.map(sourceData, mappingConfig);
```

## 配置说明

### 字段映射
```json
{
  "source": "源字段路径",
  "target": "目标字段路径",
  "processors": ["处理器列表"],
  "aggregation": "聚合策略"
}
```

### 处理器配置
处理器支持以下格式：
- 简单处理器：`"processorName"`
- 带参数处理器：`"processorName:param1,param2"`

#### 内置处理器
1. 基础转换
   - `uppercase`: 转换为大写
   - `lowercase`: 转换为小写
   - `capitalize`: 首字母大写

2. 数值处理
   - `multiplybyten`: 数值乘以10
   - `roundtwodecimal`: 保留两位小数
   - `range:min,max,targetMin,targetMax`: 数值范围映射
     - 示例：`"range:0,100,0,1"` 将0-100范围映射到0-1

3. 日期处理
   - `dateformat:pattern`: 日期格式化
     - 示例：`"dateformat:yyyy-MM-dd"`

4. 字符串处理
   - `prefix:value`: 添加前缀
   - `suffix:value`: 添加后缀
   - `substring:start,end`: 字符串截取
     - 示例：`"substring:0,5"` 截取前5个字符
   - `replace:target,replacement`: 字符串替换
     - 示例：`"replace:old,new"` 将"old"替换为"new"`

5. 特殊处理
   - `booleantoyesno`: 布尔值转是/否
   - `mapvalue:key1=value1;key2=value2`: 值映射
     - 示例：`"mapvalue:active=启用;inactive=禁用"`

### 聚合策略
支持以下聚合操作：
- `sum`: 求和
- `average`: 平均值
- `max`: 最大值
- `min`: 最小值
- `first`: 第一个元素
- `last`: 最后一个元素
- `count`: 元素计数
- `concat`: 字符串连接
- `join`: 使用分隔符连接字符串

## 高级特性

### 处理器链
可以组合多个处理器形成处理链：
```json
{
  "processors": [
    "multiplybyten",
    "roundtwodecimal"
  ]
}
```

### 嵌套映射
支持处理嵌套对象和数组：
```json
{
  "source": "user.orders[].items[].price",
  "target": "totalPrice",
  "aggregation": "sum"
}
```

### 条件映射
支持基于条件的映射：
```json
{
  "source": "status",
  "target": "statusText",
  "processors": ["mapvalue:active=启用;inactive=禁用"]
}
```

## 最佳实践
1. 处理器顺序
   - 数值处理：先进行数值计算，最后进行精度处理
   - 字符串处理：注意处理顺序，避免重复处理

2. 聚合策略
   - 数值聚合：使用 `sum`、`average` 等
   - 字符串聚合：使用 `concat` 或 `join`
   - 集合处理：使用 `count` 统计数量

3. 性能优化
   - 合理使用处理器链
   - 避免不必要的类型转换
   - 使用适当的聚合策略

## 常见问题
1. 处理器参数格式
   - 使用逗号分隔多个参数
   - 使用分号分隔键值对
   - 注意特殊字符的转义

2. 日期格式化
   - 使用标准的日期格式模式
   - 注意时区问题

3. 数值处理
   - 注意精度问题
   - 合理使用范围映射

## 贡献指南
欢迎提交 Issue 和 Pull Request 来帮助改进项目。

## 许可证
MIT License 