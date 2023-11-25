package Expense_Tracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExpenseTrackerGUI extends JFrame {
    private JTextField initialBalanceField;
    private JButton setInitialBalanceButton;
    private ExpenseTracker tracker;
    private JTextField amountField, customCategoryField, incomeSourceField;
    private JComboBox<String> dateComboBox, typeComboBox, categoryComboBox;
    private JButton addTransactionButton, showSummaryButton;
    private JTextArea summaryArea;

    public ExpenseTrackerGUI() {
        tracker = new ExpenseTracker();
        initializeComponents();
        layoutComponents();
        addListeners();
        finalLayout();
    }

    private void initializeComponents() {
        initialBalanceField = new JTextField(10);
        setInitialBalanceButton = new JButton("Set Initial Balance");
        amountField = new JTextField(10);
        customCategoryField = new JTextField(10);
        incomeSourceField = new JTextField(10);
        dateComboBox = new JComboBox<>(getDateOptions().toArray(new String[0]));
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});
        categoryComboBox = new JComboBox<>(new String[]{"Food", "Transport", "Utilities", "Other"});
        addTransactionButton = new JButton("Add Transaction");
        showSummaryButton = new JButton("Show Summary");
        summaryArea = new JTextArea(10, 30);
        summaryArea.setEditable(false);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));
        setTitle("Expense Tracker Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        inputPanel.add(createRowPanel(new JLabel("Set Initial Balance:"), initialBalanceField, setInitialBalanceButton));
        inputPanel.add(createRowPanel(new JLabel("Type:"), typeComboBox));
        inputPanel.add(createRowPanel(new JLabel("Amount:"), amountField));
        inputPanel.add(createRowPanel(new JLabel("Category:"), categoryComboBox));
        inputPanel.add(createRowPanel(new JLabel("Custom Category:"), customCategoryField));
        inputPanel.add(createRowPanel(new JLabel("Income Source:"), incomeSourceField));
        inputPanel.add(createRowPanel(new JLabel("Date:"), dateComboBox));
        inputPanel.add(createRowPanel(addTransactionButton, showSummaryButton));

        JScrollPane scrollPane = new JScrollPane(summaryArea);
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createRowPanel(JComponent... components) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JComponent comp : components) {
            panel.add(comp);
        }
        return panel;
    }

    private void addListeners() {
        setInitialBalanceButton.addActionListener(e -> setInitialBalance());
        typeComboBox.addActionListener(e -> toggleFieldsBasedOnType());
        categoryComboBox.addActionListener(e -> toggleCustomCategoryField());
        addTransactionButton.addActionListener(e -> addTransaction());
        showSummaryButton.addActionListener(e -> showSummary());
    }

    private void finalLayout() {
        pack();
        setLocationRelativeTo(null); 
    }

    private void setInitialBalance() {
        try {
            double initialBalance = Double.parseDouble(initialBalanceField.getText());
            tracker.setInitialBalance(initialBalance);
            initialBalanceField.setEnabled(false);
            setInitialBalanceButton.setEnabled(false);
            enableComponents(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid balance format");
        }
    }

    private void toggleFieldsBasedOnType() {
        boolean isIncome = "Income".equals(typeComboBox.getSelectedItem());
        categoryComboBox.setEnabled(!isIncome);
        customCategoryField.setEnabled(false);
        incomeSourceField.setEnabled(isIncome);
    }

    private void toggleCustomCategoryField() {
        boolean isOther = "Other".equals(categoryComboBox.getSelectedItem());
        customCategoryField.setEnabled(isOther);
    }

    private void addTransaction() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String category = "Other".equals(categoryComboBox.getSelectedItem()) ?
                    customCategoryField.getText() : (String) categoryComboBox.getSelectedItem();
            String date = (String) dateComboBox.getSelectedItem();
            String type = (String) typeComboBox.getSelectedItem();

            tracker.addTransaction(type, amount, category, date);
            resetInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format");
        }
    }

    private void resetInputFields() {
        amountField.setText("");
        customCategoryField.setText("");
        customCategoryField.setEnabled(false);
        categoryComboBox.setSelectedIndex(0);
        typeComboBox.setSelectedIndex(0);
        incomeSourceField.setText("");
        incomeSourceField.setEnabled(false);
    }

    private void showSummary() {
        summaryArea.setText(tracker.getSummary());
    }

    private void enableComponents(boolean enable) {
        amountField.setEnabled(enable);
        categoryComboBox.setEnabled(enable);
        dateComboBox.setEnabled(enable);
        typeComboBox.setEnabled(enable);
        addTransactionButton.setEnabled(enable);
        showSummaryButton.setEnabled(enable);
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
        SwingUtilities.invokeLater(() -> new ExpenseTrackerGUI().setVisible(true));
    }
}

class ExpenseTracker {
    // Placeholder for your ExpenseTracker class
    void setInitialBalance(double initialBalance) {
        // Implementation
    }

    void addTransaction(String type, double amount, String category, String date) {
        // Implementation
    }

    String getSummary() {
        // Implementation
        return "";
    }
}
