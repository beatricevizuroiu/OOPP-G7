package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;

public class listUsersController {

    /**
     * Handle button action for going back to lecturer view (light).
     *
     * @param event the event
     */
    public void goBackButtonLight(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/TAViewUI.fxml");
    }

    /**
     * Handle button action for going back to lecturer view (dark).
     *
     * @param event the event
     */
    public void goBackButtonDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/TAViewUI(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Mode from Light.
     *
     * @param event the event
     */
    public void handleButtonMode(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/ListUsers(DARKMODE).fxml");
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
        EntryRoomDisplay.setCurrentScene("/ListUsers.fxml");
    }

    public void handleHelpButtonLight(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Help is clicked, change to Help scene
        EntryRoomDisplay.setCurrentScene("/ListUsers.fxml");
    }

    public void handleHelpButtonDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Help is clicked, change to Help scene
        EntryRoomDisplay.setCurrentScene("/ListUsers(DARKMODE).fxml");
    }
}
