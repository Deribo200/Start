package com.example.loginjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private Button button_signup;

    @FXML
    private Button button_back;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField pf_password;

    @FXML
    private TextField tf_note;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_signup.setOnAction(this::handleSignUp);
        button_back.setOnAction(event -> DBUtils.changeScene(event, "hello-view.fxml", "Log In", null, null));
    }

    private void handleSignUp(ActionEvent event) {
        String username = tf_username.getText().trim();
        String password = pf_password.getText().trim();
        String note = tf_note.getText().trim();

        if (validateInputs(username, password, note)) {
            DBUtils.signupUser(event, username, password, note);
        } else {
            showAlert("Please fill in all fields.");
        }
    }

    private boolean validateInputs(String username, String password, String note) {
        return !username.isEmpty() && !password.isEmpty() && !note.isEmpty();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
