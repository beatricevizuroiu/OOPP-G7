package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.communication.RoomServerCommunication;
import nl.tudelft.oopp.g7.client.communication.LocalData;
import nl.tudelft.oopp.g7.client.logic.JoinRoomLogic;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.RoomJoinRequest;
import nl.tudelft.oopp.g7.common.UserRole;

public class JoinRoomController {
    @FXML
    TextField nickname;
    @FXML
    TextField roomId;
    @FXML
    TextField roomPassword;

    /**
     * Handle button action for button Mode from Light.
     *
     * @param event the event
     */
    public void handleButtonMode(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/joinRoom(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Mode from Dark.
     *
     * @param event the event
     */
    public void handleButtonMode2(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/joinRoom.fxml");
    }

    /**
     * Handles clicking the button Join from Light.
     */
    public void buttonClicked() {

        boolean isConfirmed = JoinRoomLogic.joinRoomConfirmation(nickname, roomId);

        // if the user presses OK, the go to Student View
        if (isConfirmed) {

            // Store all the entered information
            JoinRoomLogic.joinRoomStoreLocalData(nickname, roomId, roomPassword);

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

    /**
     * Handles clicking the button Join from Light.
     */
    public void buttonClickedDark() {

        boolean isConfirmed = JoinRoomLogic.joinRoomConfirmation(nickname, roomId);

        // if the user presses OK, the go to Student View
        if (isConfirmed) {

            // Store all the entered information
            JoinRoomLogic.joinRoomStoreLocalData(nickname, roomId, roomPassword);

            Scene scene = EntryRoomDisplay.getCurrentScene();
            Stage stage = EntryRoomDisplay.getCurrentStage();

            // Send password to the server to determine whether the user is a Student or a Moderator and send the user to the right UI
            if (RoomServerCommunication.joinRoom(roomId.getText(), new RoomJoinRequest(roomPassword.getText())).getRole() == UserRole.STUDENT) {
                EntryRoomDisplay.setCurrentScene("/StudentViewUI(DARKMODE).fxml");
            } else {
                EntryRoomDisplay.setCurrentScene("/TAViewUI(DARKMODE).fxml");
            }
        }
    }
}
