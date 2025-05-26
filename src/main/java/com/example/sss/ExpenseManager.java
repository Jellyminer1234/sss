package com.example.sss;

import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {
    private List<Expense> expenses = new ArrayList<>();

    // CREATE
    public void addExpense(String category, double amount) {
        expenses.add(new Expense(category, amount));
    }

    // READ
    public List<Expense> getExpenses() {
        return expenses;
    }

    // UPDATE
    public void updateExpense(int index, double newAmount) {
        Expense expense = expenses.get(index);
        expense.setAmount(newAmount);
    }

    // DELETE
    public void deleteExpense(int index) {
        expenses.remove(index);
    }
}
