package com.example.sss;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML private TextField expenseAmount;
    @FXML private PieChart expensePieChart;
    @FXML private Label totalSavingsLabel;          // repurposed for error messages or blank
    @FXML private TextField allowanceField;
    @FXML private Label remainingBalanceLabel;
    @FXML private Label foodTotalLabel;
    @FXML private Label travelTotalLabel;
    @FXML private Label rentTotalLabel;
    @FXML private Label totalSpentLabel;

    private ObservableList<PieChart.Data> expenseData = FXCollections.observableArrayList();

    private double weeklyExpenses = 0.0;
    private double allowanceAmount = 0.0;   // weekly allowance
    private double foodTotal = 0.0;
    private double travelTotal = 0.0;
    private double rentTotal = 0.0;

    // Store expense history as strings (category + amount)
    private List<String> expenseHistory = new ArrayList<>();

    // Store weekly summary history as objects with numeric values
    private static class WeeklySummary {
        double spent;
        double remaining;
        WeeklySummary(double spent, double remaining) {
            this.spent = spent;
            this.remaining = remaining;
        }
    }
    private List<WeeklySummary> weeklySummaryHistory = new ArrayList<>();

    @FXML
    protected void onSetAllowanceClick() {
        try {
            allowanceAmount = Double.parseDouble(allowanceField.getText());
            clearMessages();
            updateRemainingBalance();
        } catch (NumberFormatException e) {
            showError("Invalid allowance input.");
        }
    }

    @FXML
    protected void onAddFoodExpense() {
        double amount = parseAmount();
        if (amount <= 0) return;

        foodTotal += amount;
        foodTotalLabel.setText(String.format("Total Food: ₱%.2f", foodTotal));
        addExpense("Food", amount);
    }

    @FXML
    protected void onAddTravelExpense() {
        double amount = parseAmount();
        if (amount <= 0) return;

        travelTotal += amount;
        travelTotalLabel.setText(String.format("Total Travel: ₱%.2f", travelTotal));
        addExpense("Travel", amount);
    }

    @FXML
    protected void onAddRentExpense() {
        double amount = parseAmount();
        if (amount <= 0) return;

        rentTotal += amount;
        rentTotalLabel.setText(String.format("Total Rent: ₱%.2f", rentTotal));
        addExpense("Rent", amount);
    }

    private void addExpense(String category, double amount) {
        weeklyExpenses += amount;

        // Add to detailed history
        expenseHistory.add(String.format("%s: ₱%.2f", category, amount));

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
    }

    private double parseAmount() {
        try {
            clearMessages();
            return Double.parseDouble(expenseAmount.getText());
        } catch (NumberFormatException e) {
            showError("Invalid amount. Please enter a number.");
            return -1;
        }
    }

    private void updateRemainingBalance() {
        double remaining = allowanceAmount - weeklyExpenses;
        remainingBalanceLabel.setText(String.format("Remaining Allowance: ₱%.2f", remaining));
    }

    private void updateTotalSpent() {
        totalSpentLabel.setText(String.format("Total Spent This Week: ₱%.2f", weeklyExpenses));
    }

    @FXML
    protected void onResetWeeklyExpensesButtonClick() {
        double remaining = allowanceAmount - weeklyExpenses;
        if (remaining < 0) remaining = 0.0;

        // Store numeric summary instead of string
        weeklySummaryHistory.add(new WeeklySummary(weeklyExpenses, remaining));

        // Clear weekly data
        weeklyExpenses = 0.0;
        foodTotal = 0.0;
        travelTotal = 0.0;
        rentTotal = 0.0;

        expenseData.clear();
        expensePieChart.setData(expenseData);

        foodTotalLabel.setText("Total Food: ₱0.00");
        travelTotalLabel.setText("Total Travel: ₱0.00");
        rentTotalLabel.setText("Total Rent: ₱0.00");
        remainingBalanceLabel.setText(String.format("Remaining Allowance: ₱%.2f", allowanceAmount));
        totalSpentLabel.setText("Total Spent This Week: ₱0.00");
        totalSavingsLabel.setText("");  // clear message

        expenseHistory.clear();  // Clear detailed history on reset
    }

    @FXML
    protected void onShowHistoryButtonClick() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Expense and Summary History");
        alert.setHeaderText(null);

        StringBuilder message = new StringBuilder();

        // Weekly summaries only if exists
        if (!weeklySummaryHistory.isEmpty()) {
            message.append("Weekly Summaries:\n");
            int weekNum = 1;
            for (WeeklySummary ws : weeklySummaryHistory) {
                message.append(String.format("Week %d - Total Spent: ₱%.2f, Remaining: ₱%.2f\n",
                        weekNum++, ws.spent, ws.remaining));
            }
            message.append("\n");
        } else {
            message.append("No weekly summaries recorded yet.\n\n");
        }

        // Monthly summaries aggregated every 4 weeks
        if (weeklySummaryHistory.size() >= 4) {
            message.append("Monthly Summaries (Every 4 Weeks):\n");
            int monthCount = 1;
            for (int i = 0; i < weeklySummaryHistory.size(); i += 4) {
                double totalSpent = 0;
                double totalRemaining = 0;

                int end = Math.min(i + 4, weeklySummaryHistory.size());
                for (int j = i; j < end; j++) {
                    WeeklySummary ws = weeklySummaryHistory.get(j);
                    totalSpent += ws.spent;
                    totalRemaining += ws.remaining;
                }
                message.append(String.format("Month %d - Total Spent: ₱%.2f, Total Savings: ₱%.2f\n",
                        monthCount++, totalSpent, totalRemaining));
            }
            message.append("\n");
        } else {
            message.append("Not enough data for monthly summaries (need at least 4 weeks).\n\n");
        }

        if (!expenseHistory.isEmpty()) {
            message.append("Current Week Expenses:\n");
            for (String record : expenseHistory) {
                message.append(record).append("\n");
            }
        }

        if (message.length() == 0) {
            message.append("No expense history available.");
        }

        alert.setContentText(message.toString());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void showError(String message) {
        totalSavingsLabel.setText(message);
        remainingBalanceLabel.setText("");
    }

    private void clearMessages() {
        totalSavingsLabel.setText("");
        remainingBalanceLabel.setText(String.format("Remaining Allowance: ₱%.2f", allowanceAmount));
    }
}
