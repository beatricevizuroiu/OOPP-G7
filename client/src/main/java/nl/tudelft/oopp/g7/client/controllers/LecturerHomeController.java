package nl.tudelft.oopp.g7.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.Views;
import nl.tudelft.oopp.g7.client.logic.LecturerViewLogic;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.ModeratorViewLogic;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The type Lecturer view controller.
 */
public class LecturerHomeController {
    @FXML
    public ScrollPane questionList;
    @FXML
    private VBox questionContainer;
    @FXML
    private TextArea answerBox;
    @FXML
    private Button postAnswerButton;
    @FXML
    private Circle circle1;
    @FXML
    private Circle circle2;
    @FXML
    private Circle circle3;
    @FXML
    private Circle circle4;
    @FXML
    public VBox pollWindow;
    @FXML
    public Text courseName;

    /**
     * Start-up routine.
     */
    public void initialize() {
        SharedLogic.displayCourseName(courseName);
        // Start a timer and create a separate thread on it to automatically call retrieveQuestions()
        Timer timer = new Timer(true);

        LecturerViewController reference = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(reference::retrieveQuestions);
                Platform.runLater(reference::speed);
            }
        }, 0L, 5000L);
    }

    /**
     * Update the list of questions sorted by most upvotes.
     */
    public void retrieveQuestions() {
        // Retrieve all of the questions and then put them into question pane
        ModeratorViewLogic.retrieveServerData(LocalData.getRoomID(), answerBox, postAnswerButton, questionContainer, questionList, pollWindow);
    }

    /**
     * Speed indicator to show the lecturer if he is going too fast.
     */
    public void speed(){
        LecturerViewLogic.speedIndicator(LocalData.getRoomID(), circle1, circle2, circle3, circle4);
    }

    /**
     * Display link and passwords.
     */
    public void displayLinkAndPassword() {
        ModeratorViewLogic.displayLinkAndPasswords();
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
    public void gotoHelpPage() {
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
     * Handle button action for creating a poll.
     */
    public void createPoll() {
        Views.navigateTo(Views.CREATE_POLL);
    }

    /**
     * Change the sorting mode.
     */
    public void switchSortingMode() {
        SharedLogic.switchSortingMode();
        retrieveQuestions();
    }

    /**
     * Handle button action for exporting questions.
     */
    public void exportQuestions() {
        ModeratorViewLogic.exportQuestions(LocalData.getRoomID());
    }

    /**
     * Handle button action for closing a room.
     */
    public void closeRoom() {
        ModeratorViewLogic.closeRoom(LocalData.getRoomID());
    }

    public void switchView() {
        LocalData.switchRole();
    }
}
