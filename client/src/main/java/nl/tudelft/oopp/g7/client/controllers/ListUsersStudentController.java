package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.MainApp;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;


public class ListUsersStudentController {
    @FXML
    private VBox listUsersContainer;
    @FXML
    private Text courseName;

    private final String component = "StudentUserInfoContainer.fxml";
    private final String componentDark = "StudentUserInfoContainer(DARKMODE).fxml";

    /**
     * Special FXML method that will run as soon as listUsersContainer is populated.
     */
    @FXML
    public void initialize() {
        SharedLogic.displayCourseName(courseName);
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
     * @param event the event
     */
    public void goBackButtonLight(ActionEvent event) {
        // if goBack is clicked, change Scene to LecturerViewUI
        MainApp.setCurrentScene("/views/StudentViewUI.fxml");
    }


    /**
     * Handle button action for button Mode from Light to Dark.
     * @param event the event
     */
    public void handleButtonMode(ActionEvent event) {
        LocalData.switchColorScheme();
    }

    /**
     * Handle button action for Help Button Light Mode.
     * @param event the event
     */
    public void handleHelpButtonLight(ActionEvent event) {
        // if Help is clicked, change to Help scene
        MainApp.setCurrentScene("/views/HelpFileStudent.fxml");
    }
}