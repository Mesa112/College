package Expense_Tracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ItemEvent;

public class ExpenseTrackerGUI extends JFrame {
    private ExpenseTracker tracker;
    private JTextField amountField, customCategoryField, incomeSourceField;
    private JComboBox<String> dateComboBox, typeComboBox, categoryComboBox;
    private JButton addTransactionButton, showSummaryButton;
    private JTextArea summaryArea;

    public ExpenseTrackerGUI() {
        tracker = new ExpenseTracker();

        setTitle("Expense Tracker Application");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2)); 
        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField(10);
        inputPanel.add(amountField);

        String[] commonCategories = {"Food", "Transport", "Utilities", "Other"};
        categoryComboBox = new JComboBox<>(commonCategories);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryComboBox);

        // Custom Category Field
        customCategoryField = new JTextField(10);
        customCategoryField.setEnabled(false);
        inputPanel.add(new JLabel("Custom Category:"));
        inputPanel.add(customCategoryField);

        inputPanel.add(new JLabel("Type:"));
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});
        inputPanel.add(typeComboBox);

        // Income Source Field 
        inputPanel.add(new JLabel("Income Source:"));
        incomeSourceField = new JTextField(10);
        incomeSourceField.setEnabled(false);
        inputPanel.add(incomeSourceField);

        inputPanel.add(new JLabel("Date:"));
        dateComboBox = new JComboBox<>(getDateOptions().toArray(new String[0]));
        inputPanel.add(dateComboBox);

        addTransactionButton = new JButton("Add Transaction");
        inputPanel.add(addTransactionButton);

        showSummaryButton = new JButton("Show Summary");
        inputPanel.add(showSummaryButton);

        // Summary Area
        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        typeComboBox.addActionListener(e -> {
            boolean isIncome = "Income".equals(typeComboBox.getSelectedItem());
            categoryComboBox.setEnabled(!isIncome);
            customCategoryField.setEnabled(false);
            incomeSourceField.setEnabled(isIncome);
        });

        // Listener for categoryComboBox
        categoryComboBox.addActionListener(e -> {
            boolean isOther = "Other".equals(categoryComboBox.getSelectedItem());
            customCategoryField.setEnabled(isOther);
        });

        // Add Transaction Button ActionListener
        addTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    String category;
                    if ("Other".equals(categoryComboBox.getSelectedItem())) {
                        category = customCategoryField.getText();
                    } else {
                        category = (String) categoryComboBox.getSelectedItem();
                    }
                    String date = (String) dateComboBox.getSelectedItem();
                    String type = (String) typeComboBox.getSelectedItem();

                    // Assuming you have a method like this in your ExpenseTracker class
                    tracker.addTransaction(type, amount, category, date);

                    // Resetting fields after adding transaction
                    amountField.setText("");
                    customCategoryField.setText("");
                    customCategoryField.setEnabled(false);
                    categoryComboBox.setSelectedIndex(0);
                    typeComboBox.setSelectedIndex(0);
                    incomeSourceField.setText("");
                    incomeSourceField.setEnabled(false);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ExpenseTrackerGUI.this, "Invalid amount format");
                }
            }
        });


        // Show Summary Button ActionListener
        showSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String summary = tracker.getSummary(); // Retrieve the summary from the ExpenseTracker
                summaryArea.setText(summary); // Display the summary in the summaryArea 
            }
        });
    }

    private List<String> getDateOptions() {
        List<String> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 30; i >= 0; i--) {
            dates.add(today.minusDays(i).format(formatter));
        }

        return dates;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExpenseTrackerGUI().setVisible(true);
            }
        });
    }

    // Getters and setters
    public JTextField getAmountField() {
        return amountField;
    }

    public void setAmountField(JTextField amountField) {
        this.amountField = amountField;
    }

    public JComboBox<String> getDateComboBox() {
        return dateComboBox;
    }

    public void setDateComboBox(JComboBox<String> dateComboBox) {
        this.dateComboBox = dateComboBox;
    }

    public JComboBox<String> getTypeComboBox() {
        return typeComboBox;
    }

    public void setTypeComboBox(JComboBox<String> typeComboBox) {
        this.typeComboBox = typeComboBox;
    }

    public JTextArea getSummaryArea() {
        return summaryArea;
    }

    public void setSummaryArea(JTextArea summaryArea) {
        this.summaryArea = summaryArea;
    }
}
