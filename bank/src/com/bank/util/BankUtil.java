package com.bank.util;

import java.util.concurrent.atomic.AtomicInteger;

public class BankUtil {
    private static final AtomicInteger NEXT_ACCOUNT_NUMBER = new AtomicInteger(100000);
    private static final double MINIMUM_BALANCE = 500.0;

    private BankUtil() {
    }

    public static int generateAccountNumber() {
        return NEXT_ACCOUNT_NUMBER.getAndIncrement();
    }

    public static boolean validateMinimumBalance(double balance) {
        return balance >= MINIMUM_BALANCE;
    }

    public static double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

}
