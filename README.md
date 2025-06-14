# 字段映射转换工具

这是一个强大的字段映射转换工具，支持多种字段处理器和聚合策略，可以灵活地处理数据转换和聚合需求。

## 功能特点

- 支持JSONPath表达式进行字段映射
- 提供多种字段处理器进行数据转换
- 支持多种聚合策略处理数组数据
- 支持处理器和聚合策略的链式调用
- 提供详细的日志记录功能
- 支持产品级别的映射规则管理

## 配置示例

### 基本配置结构

```json
{
   "code": "DEMO001",
  "mappings": [
    {
      "sourcePath": "$.source.field",
      "targetPath": "$.target.field",
      "processors": ["trim", "lowercase"],
      "aggregationStrategies": ["sum"]
    }
  ]
}
```

## 字段处理器示例

### 1. 格式化处理器 (FormatProcessor)

将输入值按照指定格式进行格式化。

```json
{
  "sourcePath": "$.price",
  "targetPath": "$.formattedPrice",
  "processors": ["format:￥%.2f"]
}
```

示例数据：
- 输入：`{"price": 99.9}`
- 输出：`{"formattedPrice": "￥99.90"}`

### 2. 子字符串处理器 (SubstringProcessor)

截取字符串的指定部分。

```json
{
  "sourcePath": "$.code",
  "targetPath": "$.shortCode",
  "processors": ["substring:0,4"]
}
```

示例数据：
- 输入：`{"code": "ABC12345"}`
- 输出：`{"shortCode": "ABC1"}`

### 3. 替换处理器 (ReplaceProcessor)

替换字符串中的指定内容。

```json
{
  "sourcePath": "$.status",
  "targetPath": "$.displayStatus",
  "processors": ["replace:0,未开始", "replace:1,进行中", "replace:2,已完成"]
}
```

示例数据：
- 输入：`{"status": "1"}`
- 输出：`{"displayStatus": "进行中"}`

### 4. 范围处理器 (RangeProcessor)

将数值从一个范围映射到另一个范围。

```json
{
  "sourcePath": "$.score",
  "targetPath": "$.grade",
  "processors": ["range:0,100,1,5"]
}
```

示例数据：
- 输入：`{"score": 85}`
- 输出：`{"grade": 4}`

### 5. 数值计算处理器 (MultiplyByTenProcessor)

对数值进行倍数计算。

```json
{
  "sourcePath": "$.amount",
  "targetPath": "$.amountInCents",
   "processors": [
      "multiplybyten:100"
   ]
}
```

示例数据：
- 输入：`{"amount": 99.99}`
- 输出：`{"amountInCents": 9999}`

## 聚合策略示例

### 1. 求和 (SUM)

对数组中的数值进行求和。

```json
{
  "sourcePath": "$.items[*].price",
  "targetPath": "$.totalPrice",
  "aggregationStrategies": ["sum"]
}
```

示例数据：
- 输入：`{"items": [{"price": 100}, {"price": 200}, {"price": 300}]}`
- 输出：`{"totalPrice": 600}`

### 2. 平均值 (AVERAGE)

计算数组中数值的平均值。

```json
{
  "sourcePath": "$.scores[*]",
  "targetPath": "$.averageScore",
  "aggregationStrategies": ["average"]
}
```

示例数据：
- 输入：`{"scores": [60, 70, 80, 90]}`
- 输出：`{"averageScore": 75}`

### 3. 最大值/最小值 (MAX/MIN)

获取数组中的最大值或最小值。

```json
{
  "sourcePath": "$.temperatures[*]",
  "targetPath": "$.highestTemp",
  "aggregationStrategies": ["max"]
}
```

示例数据：
- 输入：`{"temperatures": [22, 25, 23, 28, 24]}`
- 输出：`{"highestTemp": 28}`

### 4. 连接 (JOIN/CONCAT)

将数组元素连接成字符串。

```json
{
  "sourcePath": "$.tags[*]",
  "targetPath": "$.tagString",
  "aggregationStrategies": ["join"]
}
```

示例数据：
- 输入：`{"tags": ["java", "spring", "mysql"]}`
- 输出：`{"tagString": "java,spring,mysql"}`

### 5. 计数 (COUNT)

统计数组元素的数量。

```json
{
  "sourcePath": "$.items[*]",
  "targetPath": "$.itemCount",
  "aggregationStrategies": ["count"]
}
```

示例数据：
- 输入：`{"items": ["a", "b", "c", "d"]}`
- 输出：`{"itemCount": 4}`

### 6. 分组 (GROUP)

保持数组原样返回。

```json
{
  "sourcePath": "$.data[*].value",
  "targetPath": "$.groupedData",
  "aggregationStrategies": ["group"]
}
```

示例数据：
- 输入：`{"data": [{"value": 1}, {"value": 2}, {"value": 3}]}`
- 输出：`{"groupedData": [1, 2, 3]}`

### 7. 减法 (SUBTRACT)

第一个值减去其余值。

```json
{
  "sourcePath": "$.values[*]",
  "targetPath": "$.difference",
  "aggregationStrategies": ["subtract"]
}
```

示例数据：
- 输入：`{"values": [100, 20, 30]}`
- 输出：`{"difference": 50}` // 100 - 20 - 30 = 50

## 组合使用示例

可以组合多个处理器和聚合策略来实现复杂的转换需求。

```json
{
  "sourcePath": "$.items[*].price",
  "targetPath": "$.summary",
   "processors": [
      "multiplybyten:100",
      "format:￥%.2f"
   ],
  "aggregationStrategies": ["sum"]
}
```

示例数据：
- 输入：`{"items": [{"price": 99.9}, {"price": 199.9}]}`
- 输出：`{"summary": "￥29980.00"}`

## 错误处理

- 当处理器执行失败时，相应的字段会被标记为无效字段
- 当聚合策略执行失败时，相应的字段会被标记为无效字段
- 所有错误都会被记录到日志中，方便排查问题

## 日志记录

系统会记录以下类型的日志：
- 字段处理器的处理过程
- 聚合策略的处理过程
- 字段映射的路径信息
- 产品信息
- 错误信息

## 最佳实践

1. 合理使用处理器链
   - 按照数据处理的逻辑顺序组织处理器
   - 避免不必要的处理器调用

2. 选择合适的聚合策略
   - 根据业务需求选择合适的聚合策略
   - 注意数据类型的兼容性

3. 配置文件管理
   - 按产品代码组织配置文件
   - 保持配置文件的结构清晰

4. 错误处理
   - 及时处理无效字段
   - 定期检查错误日志

5. 性能优化
   - 避免过长的处理器链
   - 合理使用聚合策略

## 贡献指南
欢迎提交 Issue 和 Pull Request 来帮助改进项目。

## 许可证
MIT License 