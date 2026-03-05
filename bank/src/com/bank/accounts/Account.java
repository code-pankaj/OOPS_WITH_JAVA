package com.bank.accounts;

import com.bank.util.BankUtil;

public class Account {
    // package-private on purpose to demonstrate package access behavior
    int accountNumber;

    private double balance;

    public Account(int accountNumber, double openingBalance) {
        this.accountNumber = accountNumber;
        this.balance = openingBalance;

        if (!BankUtil.validateMinimumBalance(this.balance)) {
            throw new IllegalArgumentException(
                    "Opening balance must be at least " + BankUtil.getMinimumBalance());
        }
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        double newBalance = balance - amount;
        if (!BankUtil.validateMinimumBalance(newBalance)) {
            return false;
        }

        balance = newBalance;
        return true;
    }
}
