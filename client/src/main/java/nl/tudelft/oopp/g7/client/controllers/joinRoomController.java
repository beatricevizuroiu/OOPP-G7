package nl.tudelft.oopp.g7.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class joinRoomController {
    @FXML
    TextField nickname;
    @FXML
    TextField roomId;
    @FXML
    TextField roomPassword;

    /**
     * Handles clicking the button.
     */
    public void buttonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Create Room");
        alert.setHeaderText(null);
        if(nickname.getText() != null){
            alert.setContentText("You are joining room: " + roomId.getText() + " as: " + nickname.getText());
        }else {
            alert.setContentText("You are joining room: " + roomId.getText());
        }
        alert.showAndWait();
    }
}
