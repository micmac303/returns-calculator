package com.bestdeals.returnscalculator.services;

public interface InterestCalculator {

     long calculateInterest(long principal, float interestRate, int timesApplied, int years);
}
