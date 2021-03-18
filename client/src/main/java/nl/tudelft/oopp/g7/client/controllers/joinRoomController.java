package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.communication.RoomServerCommunication;
import nl.tudelft.oopp.g7.client.communication.localData;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.RoomJoinRequest;
import nl.tudelft.oopp.g7.common.UserRole;

import java.io.IOException;

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

        // show confirmation type pop-up with what you entered as text
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // title of pop-up
        alert.setTitle("Join Room");
        alert.setHeaderText(null);

        // body of pop-up with what the user entered
        if(nickname.getText() == null || nickname.getText().equals("")){
            alert.setContentText("You are joining room: " + roomId.getText());
        }else {
            alert.setContentText("You are joining room: " + roomId.getText() + " as: " + nickname.getText());
        }

        // set types of buttons for the pop-up
        ButtonType okButton = new ButtonType ("OK");
        ButtonType cancelButton = new ButtonType ("Cancel");

        alert.getButtonTypes().setAll(okButton, cancelButton);

        // wait for the alert to appear
        alert.showAndWait();

        // if the user presses OK, the go to Student View
        if (alert.getResult() == okButton) {

            // Store all the entered information
            localData.setNickname(nickname.getText());
            localData.setRoomID(roomId.getText());
            localData.setPassword(roomPassword.getText());

            Scene scene = EntryRoomDisplay.getCurrentScene();
            Stage stage = EntryRoomDisplay.getCurrentStage();

            // Send password to the server to determine whether the user is a Student or a Moderator and send the user to the right UI
            if (RoomServerCommunication.joinRoom(roomId.getText(), new RoomJoinRequest(roomPassword.getText())).getRole() == UserRole.STUDENT) {
                EntryRoomDisplay.setCurrentScene("/StudentViewUI.fxml");
            } else {
                EntryRoomDisplay.setCurrentScene("/TAViewUI.fxml");
            }
        }
    }
}
