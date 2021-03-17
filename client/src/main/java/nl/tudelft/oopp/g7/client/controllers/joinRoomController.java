package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;

public class joinRoomController {
    @FXML
    TextField nickname;
    @FXML
    TextField roomId;
    @FXML
    TextField roomPassword;

    /**
     * Handles clicking the button Join.
     */
    public void buttonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Join Room");
        alert.setHeaderText(null);
        if(nickname.getText() == "Enter nickname"){
            alert.setContentText("You are joining room: " + roomId.getText());
        }else {
            alert.setContentText("You are joining room: " + roomId.getText() + " as: " + nickname.getText());
        }

        ButtonType okButton = new ButtonType ("OK");
        ButtonType cancelButton = new ButtonType ("Cancel");

        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait();

        // if the user presses OK, the go to Student View
        if (alert.getResult() == okButton){
            Scene scene = EntryRoomDisplay.getCurrentScene();
            Stage stage = EntryRoomDisplay.getCurrentStage();

            EntryRoomDisplay.setCurrentScene("/StudentViewUI.fxml");
        }
    }
}
