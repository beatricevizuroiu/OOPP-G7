package nl.tudelft.oopp.g7.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;

public class createRoomController {
    @FXML
    TextField roomName;
    @FXML
    TextField lecturerName;

    /**
     * Handles clicking the button.
     */
    public void buttonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Create Room");
        alert.setHeaderText(null);
        alert.setContentText("You are creating the room for Course: " + roomName.getText() +
                " held by lecturer: " + lecturerName.getText() + ".");

        ButtonType okButton = new ButtonType ("OK");
        ButtonType cancelButton = new ButtonType ("Cancel");

        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait();

        // if the user presses OK, the go to Student View
        if (alert.getResult() == okButton){
            Scene scene = EntryRoomDisplay.getCurrentScene();
            Stage stage = EntryRoomDisplay.getCurrentStage();

            EntryRoomDisplay.setCurrentScene("/LecturerViewUI.fxml");
        }
    }
}
