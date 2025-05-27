package com.example.sss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        loadLoginScene();
    }

    public static void loadLoginScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 400, 300);
        mainStage.setTitle("Login - Student Savings System");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void loadMainScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        mainStage.setTitle("Student Savings System");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
