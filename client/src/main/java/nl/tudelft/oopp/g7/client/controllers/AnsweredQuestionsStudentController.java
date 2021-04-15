package nl.tudelft.oopp.g7.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.MainApp;
import nl.tudelft.oopp.g7.client.logic.AnsweredQuestionsLogic;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;

import java.util.Timer;
import java.util.TimerTask;

public class AnsweredQuestionsStudentController {

    @FXML
    public ScrollPane answeredQuestionList;
    @FXML
    private VBox answeredQuestionContainer;
    @FXML
    private Text courseName;


    /**
     * Start a timer and create a separate thread on it to automatically refresh answered question list.
     */
    @FXML
    public void initialize() {
        SharedLogic.displayCourseName(courseName);
        Timer timer = new Timer(true);

        AnsweredQuestionsStudentController reference = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(reference::retrieveQuestions);
            }
        }, 0L, 500L);
    }

    /**
     * Retrieves all questions.
     */
    public void retrieveQuestions() {
        AnsweredQuestionsLogic.retrieveAllAnsweredQuestions(true, answeredQuestionContainer, answeredQuestionList);
    }

    /**
     * Handle button action for going back to lecturer view (light).
     * @param event the event
     */
    public void goBackButtonLight(ActionEvent event) {
        // if goBack is clicked, change Scene to StudentViewUI
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
