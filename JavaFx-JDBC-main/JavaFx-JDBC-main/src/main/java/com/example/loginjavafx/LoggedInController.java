package com.example.loginjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button button_logout;

    @FXML
    private Label label_welcome;

    @FXML
    private Label label_note;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (button_logout != null) {
            button_logout.setOnAction(actionEvent -> DBUtils.changeScene(actionEvent, "hello-view.fxml", "Log In", null, null));
        }
    }

    public void setUserInformation(String username, String note) {
        if (label_welcome != null) {
            label_welcome.setText("Welcome " + (username != null ? username : "User"));
        }
        if (label_note != null) {
            label_note.setText("Your favorite note: " + (note != null ? note : "None"));
        }
    }
}
