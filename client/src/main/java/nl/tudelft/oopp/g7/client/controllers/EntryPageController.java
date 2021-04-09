package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;

public class EntryPageController {

    /**
     * Handle button action for button Create.
     *
     * @param event the event
     */
    public void handleButtonAction(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Create is clicked, change Scene to Create Room
        EntryRoomDisplay.setCurrentScene("/createRoom.fxml");
    }

    /**
     * Handle button action for button Join.
     *
     * @param event the event
     */
    public void handleButtonAction2(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Join is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/joinRoom.fxml");
    }

    /**
     * Handle button action for button Create.
     *
     * @param event the event
     */
    public void handleButtonActionDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Create is clicked, change Scene to Create Room
        EntryRoomDisplay.setCurrentScene("/createRoom(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Join.
     *
     * @param event the event
     */
    public void handleButtonAction2Dark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Join is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/joinRoom(DARKMODE).fxml");
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
        EntryRoomDisplay.setCurrentScene("/entryPage(DARKMODE).fxml");
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
        EntryRoomDisplay.setCurrentScene("/entryPage.fxml");
    }
}
