package nl.tudelft.oopp.g7.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.Views;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.ModeratorViewLogic;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;

import java.util.Timer;
import java.util.TimerTask;

public class ModeratorHomeController {
    @FXML
    public ScrollPane questionList;
    @FXML
    private VBox questionContainer;
    @FXML
    private TextArea answerBox;
    @FXML
    private Text courseName;
    @FXML
    private Button postAnswerButton;
    @FXML
    public VBox pollWindow;

    /**
     * Start-up routine.
     */
    public void initialize() {
        SharedLogic.displayCourseName(courseName);
        // Start a timer and create a separate thread on it to automatically call retrieveQuestions()
        Timer timer = new Timer(true);

        ModeratorHomeController reference = this;
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
        // Retrieve all of the questions and then put them into question pane
        ModeratorViewLogic.retrieveServerData(LocalData.getRoomID(), answerBox, postAnswerButton, questionContainer, questionList, pollWindow);
    }

    /**
     * Display link and passwords.
     */
    public void displayLinkAndPassword() {
        ModeratorViewLogic.displayLinkAndPasswords();
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
    public void gotoHelpPage() {
        Views.navigateTo(Views.HELP);
    }

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
     * Handle button action for creating a poll.
     */
    public void createPoll() {
        Views.navigateTo(Views.CREATE_POLL);
    }

    /**
     * Handle button action for exporting questions.
     */
    public void exportQuestions() {
        ModeratorViewLogic.exportQuestions(LocalData.getRoomID());
    }

    public void switchView() {
        LocalData.switchRole();
    }
}
