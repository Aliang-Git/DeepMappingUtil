# 时序图

```mermaid
sequenceDiagram
    participant Client as 客户端
    participant Controller as MappingController
    participant Service as ProductMappingService
    participant Engine as MappingEngine
    participant Registry as MappingRegistry
    participant MongoDB as MongoDB数据库
    
    Client->>Controller: 发送映射请求
    Controller->>Service: 调用processMapping
    Service->>MongoDB: 获取映射配置
    MongoDB-->>Service: 返回配置
    Service->>Registry: 注册映射规则
    Service->>Engine: 执行映射
    Engine->>Engine: 解析源路径
    Engine->>Engine: 应用处理器
    Engine->>Engine: 应用聚合策略
    Engine-->>Service: 返回结果
    Service-->>Controller: 返回映射结果
    Controller-->>Client: 返回响应
```

## 说明

1. **请求发起**
    - 客户端发送映射请求到`MappingController`
    - 请求包含产品编码、源数据和目标模板

2. **配置获取**
    - `ProductMappingService`从MongoDB获取映射配置
    - 配置包含字段映射规则、处理器和聚合策略

3. **规则注册**
    - 将解析后的配置注册到`MappingRegistry`
    - 注册中心维护产品编码到映射规则的映射关系

4. **映射执行**
    - `MappingEngine`执行实际的映射操作
    - 按顺序执行：路径解析、处理器应用、聚合策略应用

5. **结果返回**
    - 映射结果通过服务层返回给控制器
    - 控制器将结果封装后返回给客户端 