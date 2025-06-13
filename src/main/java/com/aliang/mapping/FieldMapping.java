package com.aliang.mapping;

import com.aliang.logger.*;
import com.aliang.logger.imple.*;
import com.aliang.processor.*;
import com.alibaba.fastjson.*;
import lombok.*;

import java.util.*;

/**
 * 定义一个字段从源路径到目标路径的映射，并支持多个处理器
 */
@Data
@AllArgsConstructor
public class FieldMapping {
    private String sourcePath;
    private String targetPath;
    private List<ValueProcessor> processors = new ArrayList<>();

    private MappingLogger logger = new Slf4jMappingLogger();

    public void apply(JSONObject source, JSONObject target) {
        try {
            Object value = JSONPath.eval(source, sourcePath);
            // 判断是否为空值（null 或 空数组）
            boolean isEmpty = value == null ||
                    (value instanceof List && ((List<?>) value).isEmpty());
            if (isEmpty) {
                logger.warn("字段映射跳过：源路径={} 不存在或为空，目标路径={}", sourcePath, targetPath);
                return;
            }
            // 处理器链执行
            for (ValueProcessor processor : processors) {
                value = processor.process(value);
                System.out.println(processor.getClass().getSimpleName()+"执行结束!,value:"+value);
                System.out.println("");
            }
            // 设置目标值
            JSONPath.set(target, targetPath, value);
            // 调试日志（可选）
            logger.debug("字段映射成功：{} -> {}，值={}", sourcePath, targetPath, value);
        } catch (Exception e) {
            logger.warn("字段映射失败：源路径={}，目标路径={}，错误={}", sourcePath, targetPath, e.getMessage(), e);
        }
    }
    public FieldMapping(String sourcePath, String targetPath) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
    }

    public FieldMapping addProcessors(ValueProcessor... processors) {
        this.processors.addAll(Arrays.asList(processors));
        return this;
    }
}
