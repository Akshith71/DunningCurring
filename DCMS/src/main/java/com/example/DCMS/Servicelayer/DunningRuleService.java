package com.example.DCMS.Servicelayer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DCMS.Entity.DunningRule;
import com.example.DCMS.Exception.BadRequestException;
import com.example.DCMS.Repository.DunningRuleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DunningRuleService {

	@Autowired
    private  DunningRuleRepository dunningRuleRepository;

    public DunningRule createRule(DunningRule rule) {

        // 1️  Validate range
        if (rule.getStartDay() < 0) {
            throw new BadRequestException("startDay cannot be negative");
        }

        if (rule.getEndDay() < rule.getStartDay()) {
            throw new BadRequestException("endDay cannot be less than startDay");
        }

        // 2️  Validate action
        validateAction(rule.getAction());

        // 3️Overlap check (only for active rules)
        if (rule.isActive()) {
            List<DunningRule> overlappingRules =
                    dunningRuleRepository
                            .findByActiveTrueAndStartDayLessThanEqualAndEndDayGreaterThanEqual(
                                    rule.getEndDay(),
                                    rule.getStartDay()
                            );

            if (!overlappingRules.isEmpty()) {
                throw new BadRequestException(
                        "Dunning rule overlaps with an existing active rule"
                );
            }
        }

        return dunningRuleRepository.save(rule);
    }

    private void validateAction(String action) {
        if (action == null) {
            throw new BadRequestException("action is required");
        }

        // ✅ UPDATE THIS BLOCK to include "NOTIFY"
        if (!action.equals("NONE")
                && !action.equals("NOTIFY")    // <--- ALLOW THIS
                && !action.equals("THROTTLE")
                && !action.equals("BLOCK")) {

            throw new BadRequestException(
                    "Invalid action. Allowed values: NONE, NOTIFY, THROTTLE, BLOCK"
            );
        }
    }

    public List<DunningRule> getActiveRules() {
        return dunningRuleRepository.findAll()
                .stream()
                .filter(DunningRule::isActive)
                .toList();
    }

    public void deactivateRule(Long ruleId) {
        DunningRule rule = dunningRuleRepository.findById(ruleId)
                .orElseThrow(() -> new BadRequestException("Rule not found"));

        rule.setActive(false);
        dunningRuleRepository.save(rule);
    }
}
