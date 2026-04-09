package com.fitness.controllers;

import com.fitness.services.AuthService;
import com.fitness.models.User;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Фитнес-приложение - Вход");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Логин");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");
        Button loginBtn = new Button("Войти");
        Label message = new Label();

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (AuthService.getInstance().login(username, password)) {
                User user = AuthService.getInstance().getCurrentUser();
                openDashboard(primaryStage, user);
            } else {
                message.setText("Неверный логин или пароль");
            }
        });

        VBox vbox = new VBox(10, usernameField, passwordField, loginBtn, message);
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openDashboard(Stage stage, User user) {
        switch (user.getRole()) {
            case "ADMIN":
                new AdminDashboardController().start(stage);
                break;
            case "TRAINER":
                new TrainerDashboardController().start(stage);
                break;
            case "CLIENT":
                new ClientDashboardController().start(stage);
                break;
        }
    }
}