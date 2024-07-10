package com.example.loginjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DBUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String note) {
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();

            if (username != null && note != null) {
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username, note);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Could not load the view!");
            return;
        }

        if (root != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } else {
            showAlert(Alert.AlertType.ERROR, "Could not load the view!");
        }
    }

    public static void signupUser(ActionEvent event, String username, String password, String note) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty() || note == null || note.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill all the fields!");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "root");
             PreparedStatement psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {

            psCheckUserExists.setString(1, username);
            try (ResultSet resultSet = psCheckUserExists.executeQuery()) {
                if (resultSet.isBeforeFirst()) {
                    showAlert(Alert.AlertType.ERROR, "Username is already taken!");
                } else {
                    try (PreparedStatement psInsert = connection.prepareStatement("INSERT INTO users (username, password, note) VALUES (?, ?, ?)")) {
                        psInsert.setString(1, username);
                        psInsert.setString(2, password);
                        psInsert.setString(3, note);
                        psInsert.executeUpdate();
                        changeScene(event, "logged-In.fxml", "Welcome", username, note);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database error occurred!");
        }
    }

    public static void logInUser(ActionEvent event, String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill all the fields!");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "root");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT password, note FROM users WHERE username = ?")) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    showAlert(Alert.AlertType.ERROR, "Incorrect credentials!");
                } else {
                    while (resultSet.next()) {
                        String retrievedPassword = resultSet.getString("password");
                        String retrievedNote = resultSet.getString("note");
                        if (retrievedPassword.equals(password)) {
                            changeScene(event, "logged-In.fxml", "Welcome", username, retrievedNote);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Incorrect credentials!");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database error occurred!");
        }
    }

    private static void showAlert(Alert.AlertType alertType, String content) {
        Alert alert = new Alert(alertType);
        alert.setContentText(content);
        alert.show();
    }

    private static void closeResources(ResultSet resultSet, PreparedStatement... statements) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (PreparedStatement statement : statements) {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
