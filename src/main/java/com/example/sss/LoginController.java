
package com.example.sss;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // Simple credentials - replace with database checks in real-world applications
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            // Successfully logged in, proceed to Expense Tracker
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Login Success");
            alert.setHeaderText(null);
            alert.setContentText("Welcome!");
            alert.showAndWait();

            // Load Expense Tracker scene (you'll need to set this up in the main app)
            MainApp.loadExpenseTracker();
        } else {
            // Invalid login attempt
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }
}
