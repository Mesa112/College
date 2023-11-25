package Expense_Tracker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Scanner;

public class ExpenseTracker {
    private HashMap<String, List<Transaction>> transactions;
    private double balance; // Added to keep track of the current balance

    public ExpenseTracker() {
        transactions = new HashMap<>();
        balance = 0; // Initial balance can be set to 0 or any other starting value
    }
    public void setInitialBalance(double initialBalance) {
        this.balance = initialBalance;
    }


    public void addTransaction(String type, double amount, String category, String date) {
        // Update balance based on transaction type
        if ("Income".equalsIgnoreCase(type)) {
            balance += amount;
        } else if ("Expense".equalsIgnoreCase(type)) {
            balance -= amount;
        }

        Transaction newTransaction = new Transaction(type, amount, category, date);
        if (!transactions.containsKey(category)) {
            transactions.put(category, new ArrayList<>());
        }
        transactions.get(category).add(newTransaction);
    }

    public double getBalance() {
        return balance;
    }
 // In your ExpenseTracker class

    public String getSummary() {
        StringBuilder summaryBuilder = new StringBuilder();
        double totalIncome = 0;
        double totalExpense = 0;

        for (List<Transaction> transList : transactions.values()) {
            for (Transaction trans : transList) {
                if ("Income".equalsIgnoreCase(trans.getType())) {
                    totalIncome += trans.getAmount();
                } else if ("Expense".equalsIgnoreCase(trans.getType())) {
                    totalExpense += trans.getAmount();
                }
            }
        }

        double netFlow = totalIncome - totalExpense; // Calculate net flow without initial balance
        double finalBalance = balance + netFlow; // Final balance accounts for initial balance

        summaryBuilder.append("----- Summary -----\n");
        summaryBuilder.append("Initial Balance: $").append(String.format("%.2f", balance)).append("\n");
        summaryBuilder.append("Total Income: $").append(String.format("%.2f", totalIncome)).append("\n");
        summaryBuilder.append("Total Expense: $").append(String.format("%.2f", totalExpense)).append("\n");
        summaryBuilder.append("Net Flow: $").append(String.format("%.2f", netFlow)).append("\n");
        summaryBuilder.append("Final Balance: $").append(String.format("%.2f", finalBalance)).append("\n");
        summaryBuilder.append("-------------------\n");

        return summaryBuilder.toString();
    }
}