package com.bestdeals.returnscalculator.services;

import org.springframework.stereotype.Component;

@Component
public class SimpleInterestCalculator implements InterestCalculator {

    @Override
    public long calculateInterest(long principal, float interestRate, int timesApplied, int years) {

        long interest = (long)(principal * interestRate * timesApplied * years);
        return interest;
    }
}
