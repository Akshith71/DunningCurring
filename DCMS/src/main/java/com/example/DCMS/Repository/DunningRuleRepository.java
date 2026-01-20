package com.example.DCMS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DCMS.Entity.DunningRule;

public interface DunningRuleRepository extends JpaRepository<DunningRule, Long> {

    List<DunningRule> findByActiveTrue();

    List<DunningRule> findByStartDayLessThanEqualAndEndDayGreaterThanEqual(
            int overdueDays1, int overdueDays2
    );
    List<DunningRule> findByActiveTrueAndStartDayLessThanEqualAndEndDayGreaterThanEqual(
            int endDay, int startDay
    );
}

