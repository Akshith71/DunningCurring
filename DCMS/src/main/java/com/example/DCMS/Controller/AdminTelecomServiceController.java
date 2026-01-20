package com.example.DCMS.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.DCMS.DTO.CreateTelecomServiceRequest;
import com.example.DCMS.Entity.TelecomService;
import com.example.DCMS.Servicelayer.TelecomServiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
public class AdminTelecomServiceController {

	@Autowired
    private  TelecomServiceService telecomServiceService;

    // ✅ ADMIN ONLY (secured by SecurityConfig)
    @PostMapping
    public TelecomService createService(
            @RequestBody CreateTelecomServiceRequest request
    ) {
        return telecomServiceService.createService(
                request.getCustomerId(),
                request.getServiceType(),
                request.getIdentifier()
        );
    }
    @PutMapping("/{serviceId}/activate")
    public TelecomService activateService(@PathVariable Long serviceId) {
        return telecomServiceService.activate(serviceId);
    }

    // 🔹 THROTTLE
    @PutMapping("/{serviceId}/throttle")
    public TelecomService throttleService(@PathVariable Long serviceId) {
        return telecomServiceService.throttle(serviceId);
    }

    // 🔹 BLOCK
    @PutMapping("/{serviceId}/block")
    public TelecomService blockService(@PathVariable Long serviceId) {
        return telecomServiceService.block(serviceId);
    }

}

