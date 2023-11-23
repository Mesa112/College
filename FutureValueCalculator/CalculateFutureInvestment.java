package FutureValueCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
class CalculateFutureInvestment extends FutureValueCalculatora {
    private double convertYears;
    private double convertYearlyToMonthly;

    public CalculateFutureInvestment(double monthlyInvestment, double years, double interest) {
        super(monthlyInvestment, years, interest);
        this.convertYears = years * 12;
        this.convertYearlyToMonthly = interest / 12 / 100;
    }

    public double getConvertYears() {
        return convertYears;
    }

    public double getConvertYearlyToMonthly() {
        return convertYearlyToMonthly;
    }
}