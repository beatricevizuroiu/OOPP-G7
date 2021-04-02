package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;


public class ListUsersModeratorController {
    @FXML
    private VBox listUsersContainer;

    private final String component = "ModeratorUserInfoContainer.fxml";
    private final String componentDark = "ModeratorUserInfoContainer(DARKMODE).fxml";

    /**
     * Special FXML method that will run as soon as listUsersContainer is populated.
     */
    @FXML
    public void initialize() {
        retrieveAllUsers();
    }

    /**
     * Retrieve all of the users in the room.
     */
    public void retrieveAllUsers() {
        SharedLogic.retrieveAllUsers(LocalData.getRoomID(), listUsersContainer, component, componentDark);
    }

    /**
     * Handle button action for going back to lecturer view (light).
     *
     * @param event the event
     */
    public void goBackButtonLight(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI or TAViewUI
        if (LocalData.isLecturer()) {
            EntryRoomDisplay.setCurrentScene("/LecturerViewUI.fxml");
        } else {
            EntryRoomDisplay.setCurrentScene("/TAViewUI.fxml");
        }
    }

    /**
     * Handle button action for going back to lecturer view (dark).
     *
     * @param event the event
     */
    public void goBackButtonDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI or TAViewUI
        if (LocalData.isLecturer()) {
            EntryRoomDisplay.setCurrentScene("/LecturerViewUI(DARKMODE).fxml");
        } else {
            EntryRoomDisplay.setCurrentScene("/TAViewUI(DARKMODE).fxml");
        }
    }

    /**
     * Handle button action for button Mode from Light to Dark.
     *
     * @param event the event
     */
    public void handleButtonMode(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/ListUsersModerator(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Mode from Dark to Light.
     *
     * @param event the event
     */
    public void handleButtonMode2(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/ListUsersModerator.fxml");
    }

    /**
     * Handle button action for Help Button Light Mode.
     *
     * @param event the event
     */
    public void handleHelpButtonLight(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Help is clicked, change to Help scene
        if (LocalData.isLecturer()) {
            EntryRoomDisplay.setCurrentScene("/HelpFileLecturer.fxml");
        } else {
            EntryRoomDisplay.setCurrentScene("/HelpFileTA.fxml");
        }
    }

    /**
     * Handle button action for Help Button Dark Mode.
     *
     * @param event the event
     */
    public void handleHelpButtonDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Help is clicked, change to Help scene
        if (LocalData.isLecturer()) {
            EntryRoomDisplay.setCurrentScene("/HelpFileLecturer.fxml");
        } else {
            EntryRoomDisplay.setCurrentScene("/HelpFileTA.fxml");
        }
    }
}
