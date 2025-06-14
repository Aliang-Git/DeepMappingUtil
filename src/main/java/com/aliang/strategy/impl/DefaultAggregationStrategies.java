package com.aliang.strategy.impl;

import com.aliang.strategy.*;

import java.util.*;

/**
 * 默认聚合策略工厂
 * 提供一组常用的聚合策略实例
 * 
 * 示例场景：电商订单数据处理
 * 
 * 1. SUM - 求和
 * 场景：计算订单总金额
 * 输入：[{"price": 100}, {"price": 200}, {"price": 300}]
 * 配置：strategy = getStrategy("SUM")
 * 输出：600
 * 
 * 2. AVERAGE - 平均值
 * 场景：计算商品平均评分
 * 输入：[4.5, 5.0, 4.0, 4.8]
 * 配置：strategy = getStrategy("AVERAGE")
 * 输出：4.575
 * 
 * 3. MAX - 最大值
 * 场景：获取最高销售额
 * 输入：[1000, 1500, 800, 2000]
 * 配置：strategy = getStrategy("MAX")
 * 输出：2000
 * 
 * 4. MIN - 最小值
 * 场景：获取最低库存量
 * 输入：[100, 50, 75, 25]
 * 配置：strategy = getStrategy("MIN")
 * 输出：25
 * 
 * 5. FIRST - 第一个值
 * 场景：获取最早的订单日期
 * 输入：["2024-03-20", "2024-03-21", "2024-03-22"]
 * 配置：strategy = getStrategy("FIRST")
 * 输出："2024-03-20"
 * 
 * 6. LAST - 最后一个值
 * 场景：获取最近的交易时间
 * 输入：["10:00", "11:30", "14:20", "16:00"]
 * 配置：strategy = getStrategy("LAST")
 * 输出："16:00"
 * 
 * 7. JOIN - 字符串连接（带分隔符）
 * 场景：商品标签组合
 * 输入：["电子产品", "手机", "智能设备"]
 * 配置：strategy = getStrategy("JOIN")
 * 输出："电子产品,手机,智能设备"
 * 
 * 8. GROUP - 保持数组形式
 * 场景：保留商品规格列表
 * 输入：["64G", "128G", "256G"]
 * 配置：strategy = getStrategy("GROUP")
 * 输出：["64G", "128G", "256G"]
 * 
 * 9. COUNT - 计数
 * 场景：统计订单项数量
 * 输入：[item1, item2, item3, item4]
 * 配置：strategy = getStrategy("COUNT")
 * 输出：4
 * 
 * 10. CONCAT - 字符串直接连接
 * 场景：组合商品编码
 * 输入：["A", "B", "C"]
 * 配置：strategy = getStrategy("CONCAT")
 * 输出："ABC"
 * 
 * 11. SUBTRACT - 减法
 * 场景：计算利润（收入减去成本）
 * 输入：[1000, 600, 100] (1000为收入，600和100为成本)
 * 配置：strategy = getStrategy("SUBTRACT")
 * 输出：300 (1000 - 600 - 100)
 * 
 * 高级用法 - 策略组合：
 * 
 * 1. 求和后格式化
 * 场景：计算总金额并格式化
 * 配置：
 * aggregationStrategies: ["sum"]
 * processors: ["format:￥%.2f"]
 * 输入：[100, 200, 300]
 * 输出："￥600.00"
 * 
 * 2. 平均值后转换等级
 * 场景：评分均值转等级
 * 配置：
 * aggregationStrategies: ["average"]
 * processors: ["range:0,5,1,10"]
 * 输入：[4.5, 5.0, 4.0]
 * 输出：8.5
 * 
 * 注意事项：
 * 1. 数值计算使用BigDecimal保证精度
 * 2. 空值和异常处理会返回null或空集合
 * 3. 支持嵌套数据结构的处理
 */
public class DefaultAggregationStrategies {
    private static final Map<String, AggregationStrategy> strategies = new HashMap<>();

    static {
        strategies.put("SUM", new SumAggregationStrategy());
        strategies.put("AVERAGE", new AverageAggregationStrategy());
        strategies.put("MAX", new MaxAggregationStrategy());
        strategies.put("MIN", new MinAggregationStrategy());
        strategies.put("FIRST", new FirstAggregationStrategy());
        strategies.put("LAST", new LastAggregationStrategy());
        strategies.put("JOIN", new JoinAggregationStrategy());
        strategies.put("GROUP", new GroupAggregationStrategy());
        strategies.put("COUNT", new CountAggregationStrategy());
        strategies.put("CONCAT", new ConcatAggregationStrategy());
        strategies.put("SUBTRACT", new SubtractAggregationStrategy());
    }

    private DefaultAggregationStrategies() {
        // 私有构造函数，防止实例化
    }

    /**
     * 获取指定名称的聚合策略
     * @param name 策略名称（大写）
     * @return 聚合策略实例，如果不存在返回null
     */
    public static AggregationStrategy getStrategy(String name) {
        return strategies.get(name.toUpperCase());
    }

    /**
     * 获取所有可用的策略名称
     * @return 策略名称集合
     */
    public static Set<String> getAvailableStrategies() {
        return Collections.unmodifiableSet(strategies.keySet());
    }

    /**
     * 注册新的聚合策略
     * @param name 策略名称（大写）
     * @param strategy 策略实例
     */
    public static void registerStrategy(String name, AggregationStrategy strategy) {
        strategies.put(name.toUpperCase(), strategy);
    }
}
