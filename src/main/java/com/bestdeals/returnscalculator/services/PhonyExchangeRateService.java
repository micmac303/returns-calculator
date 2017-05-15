package com.bestdeals.returnscalculator.services;

import org.springframework.stereotype.Service;

@Service
public class PhonyExchangeRateService implements ExchangeRateService {

    @Override
    public float exchangeRate() {
        return 1.5f;
    }
}
