package nl.tudelft.oopp.g7.client.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nl.tudelft.oopp.g7.client.Views;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;
import nl.tudelft.oopp.g7.client.logic.StudentViewLogic;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The type Student view ui controller.
 */
public class StudentHomeController {

    // the place for the questions
    @FXML
    private VBox questionContainer;

    @FXML
    public ScrollPane questionList;

    // area to type the question
    @FXML
    public TextArea answerBox;

    @FXML
    private Text courseName;

    @FXML
    public VBox pollWindow;

    @FXML
    public Button colorSlow;

    @FXML
    public Button colorFast;

    /**
     * Start-up routine.
     */
    public void initialize() {
        SharedLogic.displayCourseName(courseName);
        // Start a timer and create a separate thread on it to automatically call retrieveQuestions()
        Timer timer = new Timer(true);

        StudentHomeController reference = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(reference::retrieveQuestions);
            }
        }, 0L, 5000L);
    }

    /**
     * Retrieve all questions to List sorted by new.
     */
    public void retrieveQuestions() {
        StudentViewLogic.retrieveServerData(LocalData.getRoomID(), questionContainer, questionList, pollWindow);
    }

    /**
     * Send question.
     */
    public void sendQuestion() {
        StudentViewLogic.sendQuestion(LocalData.getRoomID(), answerBox);
        retrieveQuestions();

    }

    /**
     * Change the sorting mode.
     */
    public void switchSortingMode() {
        SharedLogic.switchSortingMode();
        retrieveQuestions();
    }

    /**
     * Handle button action for button Mode from Light to Dark.
     */
    public void changeMode() {
        LocalData.switchColorScheme();
    }

    /**
     * Handle button action for Help Button Light Mode.
     */
    public void gotoHelp() {
        Views.navigateTo(Views.HELP);
    }

    /**
     * Handle button action for Answered Questions Button light Mode.
     */
    public void answeredQuestionList() {
        Views.navigateTo(Views.ANSWERED_QUESTIONS);
    }

    /**
     * Handle button action for List Users Button light Mode.
     */
    public void userList() {
        Views.navigateTo(Views.USER_LIST);

    }

    /**
     * Handle button slower.
     */
    public void goSlower() {
        colorSlow.setVisible(true);

        PauseTransition transition = new PauseTransition(Duration.seconds(3));
        transition.setOnFinished(event -> colorSlow.setVisible(false));
        ServerCommunication.setSpeed(LocalData.getRoomID(), 1);

        transition.play();
    }

    /**
     * Handle button faster.
     */
    public void goFaster() {
        colorFast.setVisible(true);

        PauseTransition transition = new PauseTransition(Duration.seconds(3));
        transition.setOnFinished(event -> colorFast.setVisible(false));
        ServerCommunication.setSpeed(LocalData.getRoomID(), -1);

        transition.play();
    }
}
