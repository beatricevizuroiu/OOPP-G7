package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.MainApp;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;

public class HelpFileController {

    @FXML
    private Text courseName;


    public void initialize() {
        SharedLogic.displayCourseName(courseName);
    }

    /**
     * Handle button action for going back to lecturer view from Dark.
     * @param event the event
     */
    public void handleBackButtonLect(ActionEvent event) {
        // if goBack is clicked, change Scene to LecturerViewUI
        MainApp.setCurrentScene("/views/LecturerViewUI.fxml");
//        TODO (change the fact that it goes to it's doesn't go to lecturer View but to it's previous scene)

    }

    /**
     * Handle button action for going back to TA view from Dark.
     * @param event the event
     */
    public void handleBackButtonTA(ActionEvent event) {
        // if goBack is clicked, change Scene to LecturerViewUI
        MainApp.setCurrentScene("/views/TAViewUI.fxml");
    }

    /**
     * Handle button action for going back to Student view from Light.
     * @param event the event
     */
    public void handleBackButtonStudent(ActionEvent event) {
        // if goBack is clicked, change Scene to LecturerViewUI
        MainApp.setCurrentScene("/views/StudentViewUI.fxml");
    }


    /**
     * Handle button action for button Mode in Light Mode.
     * @param event the event
     */
    public void handleButtonModeTA(ActionEvent event) {
        LocalData.switchColorScheme();
    }

    /**
     * Handle button action for button Mode in Dark Mode.
     * @param event the event
     */
    public void handleButtonModeLecturer(ActionEvent event) {
        LocalData.switchColorScheme();
    }
}
