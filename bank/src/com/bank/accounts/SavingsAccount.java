package com.bank.accounts;

public class SavingsAccount extends Account {
    private final double interestRate;

    public SavingsAccount(int accountNumber, double openingBalance, double interestRate) {
        super(accountNumber, openingBalance);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double calculateInterest() {
        // Note: this.accountNumber is accessible here because it's package-private and we're in the same package.
        // The base class's balance is private, so we use the public getter.
        return getBalance() * (interestRate / 100.0);
    }
}
