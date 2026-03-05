package com.bank.customers;

import com.bank.accounts.Account;

public class Customer {
    private final int customerId;
    private final String name;

    private Account linkedAccount;

    public Customer(int customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public Account getLinkedAccount() {
        return linkedAccount;
    }

    public void linkAccount(Account account) {
        this.linkedAccount = account;
    }
}
