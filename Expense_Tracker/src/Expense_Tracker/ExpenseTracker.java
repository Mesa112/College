package Expense_Tracker;
import java.util.ArrayList;

import java.util.Scanner;

public class ExpenseTracker {
 private ArrayList<Transaction> transactions;

 public ExpenseTracker() {
     transactions = new ArrayList<>();
 }

 public void addTransaction(String type, double amount, String category, String date) {
     transactions.add(new Transaction(type, amount, category, date));
 }

 public void printSummary() {
	    double totalIncome = 0;
	    double totalExpense = 0;

	    // Iterate over all transactions to calculate totals
	    for (Transaction transaction : transactions) {
	        if ("Income".equals(transaction.getType())) {
	            totalIncome += transaction.getAmount();
	        } else if ("Expense".equals(transaction.getType())) {
	            totalExpense += transaction.getAmount();
	        }
	    }

	    // Calculate net flow
	    double netFlow = totalIncome - totalExpense;

	    // Print the summary
	    System.out.println("----- Summary -----");
	    System.out.printf("Total Income: $%.2f%n", totalIncome);
	    System.out.printf("Total Expense: $%.2f%n", totalExpense);
	    System.out.printf("Net Flow: $%.2f%n", netFlow);
	    System.out.println("-------------------");
	}

}

 