package com.example.DCMS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DCMS.Servicelayer.DunningEngineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/dunning")
@RequiredArgsConstructor
public class DunningController {

	@Autowired
    private DunningEngineService dunningEngineService;

    @PostMapping("/apply/{customerId}")
    public String applyDunning(@PathVariable Long customerId) {
        dunningEngineService.applyDunning(customerId);
        return "Dunning applied successfully";
    }
}
