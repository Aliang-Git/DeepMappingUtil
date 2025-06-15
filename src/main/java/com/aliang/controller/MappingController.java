package com.aliang.controller;

import com.aliang.model.*;
import com.aliang.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping")
public class MappingController {
    @Autowired
    private ProductMappingService productMappingService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/process")
    public MappingResultVO process(@RequestParam String code,
                                   @RequestBody MappingProcessDTO body) {
        MappingResultVO vo = new MappingResultVO();
        vo.setResult(productMappingService.processMapping(code, body.getSource(), body.getTargetTemplate()));
        return vo;
    }

    // 查询映射规则
    @GetMapping("/rule/{code}")
    public MappingRulePO getRule(@PathVariable String code) {
        Query query = new Query(Criteria.where("code").is(code));
        return mongoTemplate.findOne(query, MappingRulePO.class, "mapping_rules");
    }

    // 新增映射规则
    @PostMapping("/rule")
    public String addRule(@RequestBody MappingRulePO rule) {
        mongoTemplate.insert(rule, "mapping_rules");
        return "success";
    }

    // 修改映射规则
    @PutMapping("/rule/{code}")
    public String updateRule(@PathVariable String code, @RequestBody MappingRulePO rule) {
        Query query = new Query(Criteria.where("code").is(code));
        Update update = new Update();
        update.set("mappings", rule.getMappings());
        mongoTemplate.upsert(query, update, "mapping_rules");
        return "success";
    }

    // 删除映射规则
    @DeleteMapping("/rule/{code}")
    public String deleteRule(@PathVariable String code) {
        Query query = new Query(Criteria.where("code").is(code));
        mongoTemplate.remove(query, "mapping_rules");
        return "success";
    }
} 