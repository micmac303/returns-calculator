package com.bestdeals.returnscalculator.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompoundInterestCalculatorTest {

    //System under test
    private CompoundInterestCalculator compoundInterestCalculator;

    @Before
    public void setUp() {
        compoundInterestCalculator = new CompoundInterestCalculator();
    }


    @Test
    public void testKnownCompoundInterestOutputsReturnedFromSpecificInputs() {

        long principal = 100000;
        float interestRate = 0.01f;
        int numberOfTimesApplied = 1;
        int years = 5;

        long expected = 5101;

        assertEquals(expected, compoundInterestCalculator.calculateInterest(principal, interestRate, numberOfTimesApplied, years));

        principal = 200000;
        interestRate = 0.02f;
        numberOfTimesApplied = 1;
        years = 5;

        expected = 20816;

        assertEquals(expected, compoundInterestCalculator.calculateInterest(principal, interestRate, numberOfTimesApplied, years));

        principal = 500000;
        interestRate = 0.01f;
        numberOfTimesApplied = 2;
        years = 5;

        expected = 25570;

        assertEquals(expected, compoundInterestCalculator.calculateInterest(principal, interestRate, numberOfTimesApplied, years));
    }

    @After
    public void tearDown() {
        compoundInterestCalculator = null;
    }
}