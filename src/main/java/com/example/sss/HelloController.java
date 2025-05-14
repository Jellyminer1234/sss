package com.example.sss;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.Month;

public class HelloController {

    @FXML private TextField expenseAmount;
    @FXML private PieChart expensePieChart;
    @FXML private Label totalSavingsLabel;
    @FXML private TextField allowanceField;
    @FXML private Label remainingBalanceLabel;
    @FXML private Label foodTotalLabel;
    @FXML private Label travelTotalLabel;
    @FXML private Label rentTotalLabel;
    @FXML private Label totalSpentLabel;

    private ObservableList<PieChart.Data> expenseData = FXCollections.observableArrayList();

    private double weeklyExpenses = 0.0;
    private double monthlyExpenses = 0.0;
    private double totalMonthlySavings = 0.0;
    private double allowanceAmount = 0.0;
    private double monthlyAllowanceTotal = 0.0; // Accumulate real weekly allowances
    private double foodTotal = 0.0;
    private double travelTotal = 0.0;
    private double rentTotal = 0.0;

    private Month lastRentMonth = null;
    private Month currentMonth = LocalDate.now().getMonth();

    @FXML
    protected void onSetAllowanceClick() {
        try {
            double newAllowance = Double.parseDouble(allowanceField.getText());
            allowanceAmount = newAllowance;
            monthlyAllowanceTotal += newAllowance;

            updateRemainingBalance();
            updateMonthlySavings();
        } catch (NumberFormatException e) {
            remainingBalanceLabel.setText("Invalid allowance input.");
            totalSavingsLabel.setText("Total Monthly Savings: $0.0");
        }
    }

    @FXML
    protected void onAddFoodExpense() {
        double amount = parseAmount();
        if (amount <= 0) return;

        foodTotal += amount;
        foodTotalLabel.setText(String.format("Total Food: $%.2f", foodTotal));
        addExpense("Food", amount);
    }

    @FXML
    protected void onAddTravelExpense() {
        double amount = parseAmount();
        if (amount <= 0) return;

        travelTotal += amount;
        travelTotalLabel.setText(String.format("Total Travel: $%.2f", travelTotal));
        addExpense("Travel", amount);
    }

    @FXML
    protected void onAddRentExpense() {
        if (currentMonth != LocalDate.now().getMonth()) {
            resetMonthlyData();
            currentMonth = LocalDate.now().getMonth();
        }

        Month newMonth = LocalDate.now().getMonth();
        if (lastRentMonth != null && lastRentMonth == newMonth) {
            totalSavingsLabel.setText("Rent already added this month.");
            return;
        }

        double amount = parseAmount();
        if (amount <= 0) return;

        rentTotal += amount;
        rentTotalLabel.setText(String.format("Total Rent: $%.2f", rentTotal));
        lastRentMonth = newMonth;

        addExpense("Rent", amount);
    }

    private void addExpense(String category, double amount) {
        weeklyExpenses += amount;
        monthlyExpenses += amount;

        boolean found = false;
        for (PieChart.Data data : expenseData) {
            if (data.getName().equals(category)) {
                data.setPieValue(data.getPieValue() + amount);
                found = true;
                break;
            }
        }
        if (!found) {
            expenseData.add(new PieChart.Data(category, amount));
        }

        expensePieChart.setData(expenseData);

        updateRemainingBalance();
        updateTotalSpent();
        updateMonthlySavings();
    }

    private double parseAmount() {
        try {
            return Double.parseDouble(expenseAmount.getText());
        } catch (NumberFormatException e) {
            totalSavingsLabel.setText("Invalid amount. Please enter a number.");
            return -1;
        }
    }

    private void updateMonthlySavings() {
        totalMonthlySavings = monthlyAllowanceTotal - monthlyExpenses;
        if (totalMonthlySavings < 0) totalMonthlySavings = 0;
        totalSavingsLabel.setText(String.format("Total Monthly Savings: $%.2f", totalMonthlySavings));
    }

    private void updateRemainingBalance() {
        double remaining = allowanceAmount - weeklyExpenses;
        remainingBalanceLabel.setText(String.format("Remaining Allowance: $%.2f", remaining));
    }

    private void updateTotalSpent() {
        totalSpentLabel.setText(String.format("Total Spent This Week: $%.2f", weeklyExpenses));
    }

    @FXML
    protected void onResetWeeklyExpensesButtonClick() {
        weeklyExpenses = 0.0;
        foodTotal = 0.0;
        travelTotal = 0.0;
        rentTotal = 0.0;

        expenseData.clear();
        expensePieChart.setData(expenseData);

        foodTotalLabel.setText("Total Food: $0.0");
        travelTotalLabel.setText("Total Travel: $0.0");
        rentTotalLabel.setText("Total Rent: $0.0");
        remainingBalanceLabel.setText(String.format("Remaining Allowance: $%.2f", allowanceAmount));
        totalSpentLabel.setText("Total Spent This Week: $0.0");

        // No reset of monthly savings here — monthly data stays.
        updateMonthlySavings();
    }

    private void resetMonthlyData() {
        monthlyExpenses = 0.0;
        monthlyAllowanceTotal = 0.0;
        weeklyExpenses = 0.0;
        foodTotal = 0.0;
        travelTotal = 0.0;
        rentTotal = 0.0;
        expenseData.clear();
        expensePieChart.setData(expenseData);
        updateMonthlySavings();
    }
}
