package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.g7.client.MainApp;
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
     * @param event the event
     */
    public void modeButton(ActionEvent event) {
        LocalData.switchColorScheme();
    }

    /**
     * Handles clicking the button Join from Light.
     */
    public void buttonClicked() {
        JoinRoomLogic.joinRoom(nickname.getText(), roomPassword.getText(), roomId.getText());
    }

    /**
     * Handle back button.
     * @param event the event
     */
    public void backButton(ActionEvent event) {
        MainApp.setCurrentScene("/views/entryPage.fxml");
    }
}
