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

public class AnsweredQuestionsModeratorController {

    @FXML
    public ScrollPane answeredQuestionList;
    @FXML
    private VBox answeredQuestionContainer;
    @FXML
    private Text courseName;

    /**
     * Startup routine.
     */
    @FXML
    public void initialize() {
        SharedLogic.displayCourseName(courseName);
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
     */
    public void goBack() {
        Views.goBack();
    }

    /**
     * Handle button action for button Mode from Light to Dark.
     */
    public void handleButtonMode(ActionEvent event) {
        LocalData.switchColorScheme();
    }

    /**
     * Handle button action for Help Button Light Mode.
     */
    public void goToHelpPage() {
        Views.navigateTo(Views.HELP);
    }
}
