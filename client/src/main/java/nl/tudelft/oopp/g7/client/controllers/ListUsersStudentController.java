package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;


public class ListUsersStudentController {
    @FXML
    private VBox listUsersContainer;

    private final String component = "StudentUserInfoContainer.fxml";
    private final String componentDark = "StudentUserInfoContainer(DARKMODE).fxml";

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

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/StudentViewUI.fxml");
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
        EntryRoomDisplay.setCurrentScene("/StudentViewUI(DARKMODE).fxml");
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
        EntryRoomDisplay.setCurrentScene("/ListUsersStudent(DARKMODE).fxml");
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
        EntryRoomDisplay.setCurrentScene("/ListUsersStudent.fxml");
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
        EntryRoomDisplay.setCurrentScene("/HelpFileStudent.fxml");
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
        EntryRoomDisplay.setCurrentScene("/HelpFileStudent(DARKMODE).fxml");
    }
}

