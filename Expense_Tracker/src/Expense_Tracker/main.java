package Expense_Tracker;

import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        Scanner scanner = new Scanner(System.in);
        boolean initialAmountAdded = false; // Flag to track if initial amount has been added

        while (true) {
            System.out.println("Expense Tracker Application");
            if (!initialAmountAdded) {
                System.out.println("0. Add Initial Amount");
            }
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. Show Summary");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 0 && !initialAmountAdded) {
                System.out.print("Enter initial amount: ");
                double initialAmount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                tracker.addTransaction("Income", initialAmount, "Initial", "N/A");
                initialAmountAdded = true; // Set the flag to true after adding initial amount
            } else {
                switch (choice) {
                    case 1:
                    case 2:
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Enter category: ");
                        String category = scanner.nextLine();

                        System.out.print("Enter date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();

                        String type = (choice == 1) ? "Income" : "Expense";
                        tracker.addTransaction(type, amount, category, date);
                        break;
                    case 3:
                        tracker.printSummary();
                        break;
                    case 4:
                        System.out.println("Exiting application...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }
}
