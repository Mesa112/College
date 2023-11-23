package FutureValueCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class FutureInvestmentResult extends CalculateFutureInvestment {
    private double result;

    public FutureInvestmentResult(double monthlyInvestment, double years, double interest) {
        super(monthlyInvestment, years, interest);
        this.result = calculateFutureValue();
    }

    public String getResult() {
        return String.format("$%.2f", result);
    }

    public double calculateFutureValue() {
        double futureValue = 0;
        int months = (int) getConvertYears();
        double monthlyInterestRate = getConvertYearlyToMonthly();
        for (int i = 1; i <= months; i++) {
            futureValue = (futureValue + getMonthlyInvestment()) * (1 + monthlyInterestRate);
        }
        return futureValue;
    }
}
