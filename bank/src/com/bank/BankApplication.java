package com.bank;

import com.bank.accounts.SavingsAccount;
import com.bank.customers.Customer;

import static com.bank.util.BankUtil.generateAccountNumber;
import static com.bank.util.BankUtil.validateMinimumBalance;

public class BankApplication {
	public static void main(String[] args) {
		Customer customer = new Customer(1, "Aarav");

		SavingsAccount savings = new SavingsAccount(
				generateAccountNumber(),
				1000.0,
				4.0
		);
		customer.linkAccount(savings);

		System.out.println("Customer: " + customer.getCustomerId() + " - " + customer.getName());
		System.out.println("Account (via getter): " + savings.getAccountNumber());
		System.out.println("Opening balance: " + savings.getBalance());

		savings.deposit(250.0);
		System.out.println("After deposit(250): " + savings.getBalance());

		boolean withdrew = savings.withdraw(400.0);
		System.out.println("withdraw(400) success? " + withdrew);
		System.out.println("After withdrawal: " + savings.getBalance());

		double interest = savings.calculateInterest();
		System.out.println("Interest at " + savings.getInterestRate() + "%: " + interest);

		System.out.println("Meets minimum balance? " + validateMinimumBalance(savings.getBalance()));

		// --- Access control behavior demo ---
		// The fields below exist, but are intentionally not publicly accessible.
		// Uncomment to observe compiler errors about access control.

		// System.out.println(savings.accountNumber); // package-private: not accessible from com.bank
		// System.out.println(savings.balance);       // private in Account: not accessible here

		System.out.println("Access fields safely via getters: accountNumber="
				+ savings.getAccountNumber() + ", balance=" + savings.getBalance());
	}
}
