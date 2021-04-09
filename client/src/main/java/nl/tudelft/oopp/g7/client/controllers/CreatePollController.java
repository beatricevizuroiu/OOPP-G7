package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.logic.CreatePollLogic;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.OptionsPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreatePollController {
    @FXML
    private TextArea questionArea;
    @FXML
    private Text courseName;
    @FXML
    private TextArea optionArea;
    @FXML
    public VBox optionContainer;
    @FXML
    public Button publicResultsButton;
    public List<String> pollOptions;

    public OptionsPosition optionsPosition;
    public AtomicBoolean publicResults = new AtomicBoolean(true);

    /**
     * Construct CreatePollController.
     */
    public CreatePollController() {
        pollOptions = new ArrayList<>();
        optionsPosition = new OptionsPosition();

        CreatePollLogic.reset();
    }

    public void initialize() {
        courseName.setText(LocalData.getRoomName());
    }

    /**
     * Send the Poll that has been made.
     */

    public void sendPoll() {
        CreatePollLogic.sendPoll(pollOptions, questionArea, publicResults);
    }

    /**
     * Toggle whether students can see the results of a Poll while it is active.
     */
    public void setResultPublicity() {
        CreatePollLogic.setResultPublicity(publicResults, publicResultsButton);
    }

    /**
     * Go to the previous window.
     */
    public void back() {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        String targetFile = LocalData.isLecturer() ? "/LecturerViewUI.fxml" : "/TAViewUI.fxml";
        EntryRoomDisplay.setCurrentScene(targetFile);
    }

    /**
     * Add an option to the current Poll.
     * @param actionEvent Unused.
     */
    public void addOption(ActionEvent actionEvent) {
        CreatePollLogic.addOption(pollOptions, optionArea, optionContainer);

    }

    /**
     * Use the preset as Poll options for the current Poll.
     * @param mouseEvent Unused.
     */
    public void presetABC(MouseEvent mouseEvent) {
        pollOptions.clear();
        pollOptions.add("A");
        pollOptions.add("B");
        pollOptions.add("C");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     * @param mouseEvent Unused.
     */
    public void presetABCD(MouseEvent mouseEvent) {
        pollOptions.clear();
        pollOptions.add("A");
        pollOptions.add("B");
        pollOptions.add("C");
        pollOptions.add("D");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     * @param mouseEvent Unused.
     */
    public void presetABCDE(MouseEvent mouseEvent) {
        pollOptions.clear();
        pollOptions.add("A");
        pollOptions.add("B");
        pollOptions.add("C");
        pollOptions.add("D");
        pollOptions.add("E");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     * @param mouseEvent Unused.
     */
    public void presetABCDEF(MouseEvent mouseEvent) {
        pollOptions.clear();
        pollOptions.add("A");
        pollOptions.add("B");
        pollOptions.add("C");
        pollOptions.add("D");
        pollOptions.add("E");
        pollOptions.add("F");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     * @param mouseEvent Unused.
     */
    public void presetYesNo(MouseEvent mouseEvent) {
        pollOptions.clear();
        pollOptions.add("Yes");
        pollOptions.add("No");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     * @param mouseEvent Unused.
     */
    public void presetTrueFalse(MouseEvent mouseEvent) {
        pollOptions.clear();
        pollOptions.add("True");
        pollOptions.add("False");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }


    /**
     * Handle button action for going back to lecturer view (light).
     * @param event the event.
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
     * @param event the event.
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
     * @param event the event.
     */
    public void handleButtonMode(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/CreatePoll(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Mode from Dark to Light.
     * @param event the event.
     */
    public void handleButtonMode2(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/CreatePoll.fxml");
    }

    /**
     * Handle button action for Help Button Light Mode.
     * @param event the event.
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
     * @param event the event.
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
