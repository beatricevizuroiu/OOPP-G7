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
     * Handles clicking the button Create.
     */
    public void buttonClicked() {

        // show confirmation type pop-up with what you entered as text
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // title of pop-up
        alert.setTitle("Create Room");
        alert.setHeaderText(null);

        // body of pop-up with what the user entered
        alert.setContentText("You are creating the room for Course: " + roomName.getText() +
                " held by lecturer: " + lecturerName.getText() + ".");

        // set types of buttons for the pop-up
        ButtonType okButton = new ButtonType ("OK");
        ButtonType cancelButton = new ButtonType ("Cancel");

        alert.getButtonTypes().setAll(okButton, cancelButton);

        // wait for the alert to appear
        alert.showAndWait();

        // if the user presses OK, the go to Lecturer View
        if (alert.getResult() == okButton){
            Scene scene = EntryRoomDisplay.getCurrentScene();
            Stage stage = EntryRoomDisplay.getCurrentStage();

            EntryRoomDisplay.setCurrentScene("/LecturerViewUI.fxml");
        }
    }
}
