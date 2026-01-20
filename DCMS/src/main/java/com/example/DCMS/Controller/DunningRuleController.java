package com.example.DCMS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.DCMS.DTO.CreateDunningRuleRequest;
import com.example.DCMS.Entity.DunningRule;
import com.example.DCMS.Servicelayer.DunningRuleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/rules")
@RequiredArgsConstructor
public class DunningRuleController {

	@Autowired
    private  DunningRuleService dunningRuleService;

    @PostMapping(consumes = "application/json")
    public DunningRule createRule(
            @RequestBody CreateDunningRuleRequest request
    ) {
        DunningRule rule = new DunningRule();
        rule.setStartDay(request.getStartDay());
        rule.setEndDay(request.getEndDay());
        rule.setAction(request.getAction());
        rule.setChannel(request.getChannel());
        rule.setActive(request.isActive());

        return dunningRuleService.createRule(rule);
    }

    @GetMapping
    public List<DunningRule> getActiveRules() {
        return dunningRuleService.getActiveRules();
    }

    @DeleteMapping("/{ruleId}")
    public void deactivateRule(@PathVariable Long ruleId) {
        dunningRuleService.deactivateRule(ruleId);
    }
}
