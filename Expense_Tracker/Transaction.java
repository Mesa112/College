package Expense_Tracker;

public class Transaction {
    String type; 
    double amount;
    String category;
    String date; 

    public Transaction(String type, double amount, String category, String date) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = date;
       
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

