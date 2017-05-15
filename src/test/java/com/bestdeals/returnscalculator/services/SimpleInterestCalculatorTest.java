package com.bestdeals.returnscalculator.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleInterestCalculatorTest {

    //System under test
    SimpleInterestCalculator simpleInterestCalculator;

    @Before
    public void setUp() {

        simpleInterestCalculator = new SimpleInterestCalculator();
    }

    @Test
    public void testKnownSimpleInterestOutputsReturnedFromSpecificInputs() {

        long principal = 100000;
        float interestRate = 0.01f;
        int numberOfTimesApplied = 1;
        int years = 5;

        long expected = 5000;

        assertEquals(expected, simpleInterestCalculator.calculateInterest(principal, interestRate, numberOfTimesApplied, years));

        principal = 200000;
        interestRate = 0.02f;
        numberOfTimesApplied = 1;
        years = 5;

        expected = 20000;

        assertEquals(expected, simpleInterestCalculator.calculateInterest(principal, interestRate, numberOfTimesApplied, years));

        principal = 500000;
        interestRate = 0.03f;
        numberOfTimesApplied = 2;
        years = 5;

        expected = 150000;

        assertEquals(expected, simpleInterestCalculator.calculateInterest(principal, interestRate, numberOfTimesApplied, years));
    }

    @After
    public void tearDown() {
        simpleInterestCalculator = null;
    }

}