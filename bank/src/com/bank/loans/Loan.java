package com.bank.loans;

public class Loan {
    double loanAmount;

    private static final double DEFAULT_ANNUAL_INTEREST_RATE_PERCENT = 10.0;
    private static final int DEFAULT_TENURE_MONTHS = 12;

    public Loan(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double calculateEMI(){
        if (loanAmount <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }

        double monthlyRate = (DEFAULT_ANNUAL_INTEREST_RATE_PERCENT / 100.0) / 12.0;
        int months = DEFAULT_TENURE_MONTHS;

        if (monthlyRate == 0.0) {
            return loanAmount / months;
        }

        double factor = Math.pow(1.0 + monthlyRate, months);
        return (loanAmount * monthlyRate * factor) / (factor - 1.0);
    }

}
