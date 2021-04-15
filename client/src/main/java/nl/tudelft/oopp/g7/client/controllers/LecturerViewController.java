package nl.tudelft.oopp.g7.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.MainApp;
import nl.tudelft.oopp.g7.client.logic.LecturerViewLogic;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.ModeratorViewLogic;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;
import nl.tudelft.oopp.g7.common.UserInfo;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The type Lecturer view controller.
 */
public class LecturerViewController {
    private final String roomID;
    private final String nickname;
    private final String moderatorPassword;
    private final String studentPassword;
    private HashMap<String, UserInfo> userMap = new HashMap<>();

    /**
     * The Question list.
     */
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
    /**
     * The Poll window.
     */
    public VBox pollWindow;
    @FXML
    public Text courseName;

    /**
     * The constructor for LecturerViewController.
     */
    public LecturerViewController() {
        roomID = LocalData.getRoomID();
        nickname = LocalData.getNickname();
        moderatorPassword = LocalData.getModeratorPassword();
        studentPassword = LocalData.getStudentPassword();
    }

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
        ModeratorViewLogic.retrieveServerData(roomID, answerBox, postAnswerButton, questionContainer, questionList, pollWindow);
    }

    /**
     * Speed indicator to show the lecturer if he is going too fast.
     */
    public void speed(){
        LecturerViewLogic.speedIndicator(roomID, circle1, circle2, circle3, circle4);
    }

    /**
     * Display link and passwords.
     */
    public void displayLinkAndPassword() {
        ModeratorViewLogic.displayLinkAndPasswords();
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
        MainApp.setCurrentScene("/views/HelpFileLecturer.fxml");
    }

    /**
     * Handle button action for Answered Questions Button light Mode.
     */
    public void answeredQuestionList() {
        // if Answered questions is clicked, change to Answered Questions (lightmode) scene
        MainApp.setCurrentScene("/views/AnsweredQuestionsModerator.fxml");
    }

    /**
     * Handle button action for List Users Button light Mode.
     */
    public void listofUsers() {
        // if list of Users is clicked, change to List of Users scene
        MainApp.setCurrentScene("/views/ListUsersModerator.fxml");
    }

    /**
     * Handle button action for creating a poll.
     */
    public void createPoll() {
        MainApp.setCurrentScene("/views/CreatePoll.fxml");
    }

    /**
     * Change the sorting mode.
     * @param event the event
     */
    public void switchSortingMode(ActionEvent event) {
        SharedLogic.switchSortingMode();
        retrieveQuestions();
    }

    /**
     * Handle button action for exporting questions.
     */
    public void exportQuestions() {
        ModeratorViewLogic.exportQuestions(roomID);
    }

    /**
     * Handle button action for closing a room.
     */
    public void closeRoom() {
        ModeratorViewLogic.closeRoom(roomID);
    }

    public void switchView(ActionEvent actionEvent) {
        MainApp.setCurrentScene("/views/TAViewUI.fxml");
    }
}
