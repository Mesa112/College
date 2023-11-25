package Expense_Tracker;

public class Transaction {
	double initialAmount;
    String type; // "Income" or "Expense"
    double amount;
    String category; // For example: "Food", "Salary", etc.
    String date; // For simplicity, we'll use a string here

    public Transaction(String type, double amount, String category, String date) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.initialAmount = initialAmount;
       
    }
    public String getType() {
    	return type;
    }
    public double getAmount() {
    	return amount;
    }
    public String getCategory() {
    	return category;
    }
    public String getDate() {
    	return date;
    }
    public double getInitialAmount() {
    	return initialAmount;
    }
    
}

