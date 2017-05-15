package com.bestdeals.returnscalculator.controllers;

import com.bestdeals.returnscalculator.model.Deal;
import com.bestdeals.returnscalculator.persistence.DealRepository;
import com.bestdeals.returnscalculator.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TotalController {

    @Autowired
    private DealRepository dealRepository;
    @Autowired
    private ExchangeRateService exchangeRateService;

    @RequestMapping("/total")
    public @ResponseBody Map<String, Long> total() {

        long totalInPence = dealRepository.findAll().stream().map(Deal::getReturnAmount).collect(Collectors.summingLong(Long::longValue));

        Map<String, Long> response = new HashMap<>();
        long totalInCents = (long)(exchangeRateService.exchangeRate() * totalInPence);
        response.put("total", totalInCents);

        return response;
    }
}
