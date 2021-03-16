package nl.tudelft.oopp.g7.client.controllers;

import javafx.scene.control.Alert;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;

public class createRoomController {
    /**
     * Handles clicking the button.
     */
    public void buttonClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room created");
        alert.setHeaderText(null);
        alert.setContentText("Your room is created");
        alert.showAndWait();
    }
}
