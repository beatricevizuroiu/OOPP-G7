package nl.tudelft.oopp.g7.client.controllers;

import javafx.scene.control.Alert;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;

public class MainSceneController {

    /**
     * Handles clicking the button.
     */
    public void buttonClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quote for you");
        alert.setHeaderText(null);
        alert.setContentText(ServerCommunication.getQuote());
        alert.showAndWait();
    }
}
