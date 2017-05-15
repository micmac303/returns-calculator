package com.bestdeals.returnscalculator.services;

public interface ExchangeRateService {

    //Should take two currencies as argument but for simplicity
    //as this would have to be an external service outside the scope of
    //this assignment simple implementation returns hardcoded value of 1.5
    float exchangeRate();
}
