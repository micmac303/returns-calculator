package com.bestdeals.returnscalculator.services;

import org.springframework.stereotype.Component;

import static java.lang.Math.pow;

@Component
public class CompoundInterestCalculator implements InterestCalculator {

    @Override
    public long calculateInterest(long principal, float interestRate, int timesApplied, int years) {


        long interest = (long)(pow(1 + interestRate / timesApplied, timesApplied * years) * principal) - principal;
        return interest;
    }
}
