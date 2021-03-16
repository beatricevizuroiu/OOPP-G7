package nl.tudelft.oopp.g7.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;

public class createRoomController {
    @FXML
    TextField roomName;
    @FXML
    TextField lecturerName;

    /**
     * Handles clicking the button.
     */
    public void buttonClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room created");
        alert.setHeaderText(null);
        alert.setContentText("You are creating the room for Course: " + roomName.getText() +
                " held by lecturer: " + lecturerName.getText() + ".\nDo you want to continue?");
        alert.showAndWait();
    }
}
