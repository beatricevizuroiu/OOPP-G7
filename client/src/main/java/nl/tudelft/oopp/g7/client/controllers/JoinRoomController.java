package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.communication.RoomServerCommunication;
import nl.tudelft.oopp.g7.client.logic.JoinRoomLogic;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.RoomJoinInfo;
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

        // if the user presses OK, the go to Student View
        if (JoinRoomLogic.joinRoomConfirmation(nickname, roomId)) {

            RoomJoinInfo roomJoinInfo = RoomServerCommunication.joinRoom(roomId.getText(), roomPassword.getText(), nickname.getText());

            // Store all the entered information
            JoinRoomLogic.joinRoomStoreLocalData(nickname, roomId, roomJoinInfo);

            Scene scene = EntryRoomDisplay.getCurrentScene();
            Stage stage = EntryRoomDisplay.getCurrentStage();

            // Send password to the server to determine whether the user is a Student or a Moderator and send the user to the right UI
            if (roomJoinInfo.getRole() == UserRole.STUDENT) {
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

        // if the user presses OK, the go to Student View
        if (JoinRoomLogic.joinRoomConfirmation(nickname, roomId)) {

            RoomJoinInfo roomJoinInfo = RoomServerCommunication.joinRoom(roomId.getText(), roomPassword.getText(), nickname.getText());

            // Store all the entered information
            JoinRoomLogic.joinRoomStoreLocalData(nickname, roomId, roomJoinInfo);

            Scene scene = EntryRoomDisplay.getCurrentScene();
            Stage stage = EntryRoomDisplay.getCurrentStage();

            // Send password to the server to determine whether the user is a Student or a Moderator and send the user to the right UI
            if (roomJoinInfo.getRole() == UserRole.STUDENT) {
                EntryRoomDisplay.setCurrentScene("/StudentViewUI(DARKMODE).fxml");
            } else {
                EntryRoomDisplay.setCurrentScene("/TAViewUI(DARKMODE).fxml");
            }
        }
    }

    /**
     * Handle button action for going back to Entry page from Light.
     *
     * @param event the event
     */
    public void handleBackButton(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/entryPage.fxml");
    }

    /**
     * Handle button action for going back to Entry page from Dark.
     *
     * @param event the event
     */
    public void handleBackButtonDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/entryPage(DARKMODE).fxml");
    }
}
