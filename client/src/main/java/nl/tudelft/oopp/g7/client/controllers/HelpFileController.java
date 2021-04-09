package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;

public class HelpFileController {

    @FXML
    private Text courseName;


    public void initialize() {
        courseName.setText(LocalData.getRoomName());
    }

    /**
     * Handle button action for going back to lecturer view from Dark.
     * @param event the event
     */
    public void handleBackButtonLect(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/LecturerViewUi.fxml");
//        TODO (change the fact that it goes to it's doesn't go to lecturer View but to it's previous scene)

    }

    /**
     * Handle button action for going back to lecturer view from Light.
     * @param event the event
     */
    public void handleBackButtonDarkLect(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/LecturerViewUi(DARKMODE).fxml");
//        TODO (change the fact that it goes to it's doesn't go to lecturer View but to it's previous scene)

    }

    /**
     * Handle button action for going back to TA view from Dark.
     * @param event the event
     */
    public void handleBackButtonTA(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/TAViewUI.fxml");
    }

    /**
     * Handle button action for going back to TA view from Light.
     * @param event the event
     */
    public void handleBackButtonDarkTA(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/TAViewUI(DARKMODE).fxml");
    }

    /**
     * Handle button action for going back to Student view from Light.
     * @param event the event
     */
    public void handleBackButtonDarkStudent(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/StudentViewUI(DARKMODE).fxml");
    }

    /**
     * Handle button action for going back to Student view from Light.
     * @param event the event
     */
    public void handleBackButtonStudent(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/StudentViewUI.fxml");
    }

    /**
     * Handle button action for button Mode from Light.
     * @param event the event
     */
    public void handleButtonModeStudent(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to student dark mode
        EntryRoomDisplay.setCurrentScene("/HelpFileStudent(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Mode from Dark.
     * @param event the event
     */
    public void handleButtonModeStudentDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to student light mode
        EntryRoomDisplay.setCurrentScene("/HelpFileStudent.fxml");
    }

    /**
     * Handle button action for button Mode in Light Mode.
     * @param event the event
     */
    public void handleButtonModeTA(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to TA dark mode
        EntryRoomDisplay.setCurrentScene("/HelpFileTA(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Mode in Dark Mode.
     * @param event the event
     */
    public void handleButtonModeTADark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to TA light mode
        EntryRoomDisplay.setCurrentScene("/HelpFileTA.fxml");
    }

    /**
     * Handle button action for button Mode in Light Mode.
     * @param event the event
     */
    public void handleButtonModeLecturerDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to lecturer dark mode
        EntryRoomDisplay.setCurrentScene("/HelpFileLecturer(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Mode in Dark Mode.
     * @param event the event
     */
    public void handleButtonModeLecturer(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to lecturer light mode
        EntryRoomDisplay.setCurrentScene("/HelpFileLecturer.fxml");
    }
}
