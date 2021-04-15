package nl.tudelft.oopp.g7.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.g7.client.Views;
import nl.tudelft.oopp.g7.client.logic.JoinRoomLogic;
import nl.tudelft.oopp.g7.client.logic.LocalData;

public class JoinRoomController {
    @FXML
    TextField nickname;
    @FXML
    TextField roomId;
    @FXML
    TextField roomPassword;

    /**
     * Handle mode button.
     */
    public void changeMode() {
        LocalData.switchColorScheme();
    }

    /**
     * Handles clicking the button Join from Light.
     */
    public void joinRoom() {
        JoinRoomLogic.joinRoom(nickname.getText(), roomPassword.getText(), roomId.getText());
    }

    /**
     * Handle back button.
     */
    public void goBack() {
        Views.goBack();
    }
}
