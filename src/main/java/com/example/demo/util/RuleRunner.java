package com.example.demo.util;

import com.example.demo.aspect.UpdateRecommendationAspect;
import com.example.demo.mapperservice.task.RuleMapper;
import com.example.demo.po.task.Rule;
import com.example.demo.vo.task.RuleVO;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RuleRunner implements ApplicationRunner {
    @Resource
    RuleMapper ruleMapper;
    @Resource
    UpdateRecommendationAspect updateRecommendationAspect;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Rule rule=ruleMapper.selectCurrentRule();
        updateRecommendationAspect.setRule(new RuleVO(rule));
    }
}
