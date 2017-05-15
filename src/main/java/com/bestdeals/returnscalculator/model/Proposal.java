package com.bestdeals.returnscalculator.model;

public class Proposal {

    private long clientId;
    private long principal;
    private String interestRate;
    private int timesApplied;
    private int years;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getPrincipal() {
        return principal;
    }

    public void setPrincipal(long principal) {
        this.principal = principal;
    }

    public float getInterestRate() {
        return Float.valueOf(interestRate);
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public int getTimesApplied() {
        return timesApplied;
    }

    public void setTimesApplied(int timesApplied) {
        this.timesApplied = timesApplied;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }
}