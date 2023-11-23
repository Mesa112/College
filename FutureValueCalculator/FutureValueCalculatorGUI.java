package FutureValueCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FutureValueCalculatorGUI extends JFrame implements ActionListener {
    private JTextField investmentField, yearsField, interestField;
    private JButton calculateButton, clearButton;
    private JTextArea resultTextArea;
    private List<String> results = new ArrayList<>();
    private JPanel inputPanel; // Declare inputPanel at the class level

    public FutureValueCalculatorGUI() {
        setTitle("Future Value Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize inputPanel
        inputPanel = new JPanel(new GridLayout(3, 2, 5, 5)); // add some padding

        // Create input fields for user input
        investmentField = createInputField("Monthly Investment:");
        yearsField = createInputField("Number of Years:");
        interestField = createInputField("Yearly Interest Rate (%):");

        // Create buttons and add action listeners
        JPanel buttonPanel = new JPanel(new FlowLayout());
        calculateButton = new JButton("Calculate");
        clearButton = new JButton("Clear");

        calculateButton.addActionListener(this);
        clearButton.addActionListener(this);

        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);

        // Create result text area
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);

        // Add components to the main frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(resultTextArea), BorderLayout.SOUTH);
    }

    private JTextField createInputField(String label) {
        JPanel panel = new JPanel(new FlowLayout());
        JLabel lbl = new JLabel(label);
        JTextField field = new JTextField(10);

        panel.add(lbl);
        panel.add(field);

        inputPanel.add(panel); // Add the panel to inputPanel

        return field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calculateButton) {
            try {
                double monthlyInvestment = Double.parseDouble(investmentField.getText());
                double years = Double.parseDouble(yearsField.getText());
                double interestRate = Double.parseDouble(interestField.getText());

                // Debug line to check if values are read correctly
                System.out.println("Investment: " + monthlyInvestment + ", Years: " + years + ", Interest Rate: " + interestRate);

                FutureInvestmentResult result = new FutureInvestmentResult(monthlyInvestment, years, interestRate);
                String formattedResult = "Future Value: " + result.getResult();

                // Debug line to check if result is calculated
                System.out.println("Calculated result: " + formattedResult);

                results.add(formattedResult);
                resultTextArea.append(formattedResult + "\n");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers only!");
                ex.printStackTrace(); // Print stack trace for debugging
            } catch (Exception ex) {
                // Catch any other exceptions that might occur
                ex.printStackTrace(); // Print stack trace for debugging
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            }
        } else if (e.getSource() == clearButton) {
            investmentField.setText("");
            yearsField.setText("");
            interestField.setText("");
            resultTextArea.setText("");
            results.clear();
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FutureValueCalculatorGUI calculator = new FutureValueCalculatorGUI();
            calculator.setVisible(true);
        });
    }
}
