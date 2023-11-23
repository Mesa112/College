package FutureValueCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class FutureValueCalculatora {
    private double monthlyInvestment;
    private double years;
    private double interest;

    public FutureValueCalculatora(double monthlyInvestment, double years, double interest) {
        this.monthlyInvestment = monthlyInvestment;
        this.years = years;
        this.interest = interest;
    }

    public double getMonthlyInvestment() {
        return monthlyInvestment;
    }
    public double getInterest() {
        return interest;
    }

    public void setMonthlyInvestment(double monthlyInvestment) {
        this.monthlyInvestment = monthlyInvestment;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
    public static int getYears(String Prompt) {
        int i = 0;
        while (true) {
            System.out.println(Prompt);
            try {
                i = Integer.parseInt(input.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error invalid integer. Try Again.");
            }
        }
        return i;
    }

}