package nl.tudelft.oopp.g7.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.client.logic.AnsweredQuestionsLogic;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.Question;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AnsweredQuestionsModeratorController {

    @FXML
    public ScrollPane answeredQuestionList;
    @FXML
    private VBox answeredQuestionContainer;

    /**
     * Startup routine.
     */
    @FXML
    public void initialize() {
        // Start a timer and create a separate thread on it to automatically call retrieveQuestions()
        Timer timer = new Timer(true);

        AnsweredQuestionsModeratorController reference = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(reference::retrieveQuestions);
            }
        }, 0L, 500L);
    }

    /**
     * Retrieve Questions.
     */
    public void retrieveQuestions() {
        AnsweredQuestionsLogic.retrieveAllAnsweredQuestions(false, answeredQuestionContainer, answeredQuestionList);
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
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestionsModerator(DARKMODE).fxml");
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
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestionsModerator.fxml");
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
            EntryRoomDisplay.setCurrentScene("/HelpFileLecturer(DARKMODE).fxml");
        } else {
            EntryRoomDisplay.setCurrentScene("/HelpFileTA(DARKMODE).fxml");
        }
    }
}
