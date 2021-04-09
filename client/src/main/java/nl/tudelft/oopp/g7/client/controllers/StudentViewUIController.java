package nl.tudelft.oopp.g7.client.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;
import nl.tudelft.oopp.g7.client.logic.StudentViewLogic;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.QuestionText;

import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The type Student view ui controller.
 */
public class StudentViewUIController {

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

    private final String roomID;
    private final String nickname;

    /**
     * Constructor for StudentViewUIController.
     */
    public StudentViewUIController() {
        roomID = LocalData.getRoomID();
        nickname = LocalData.getNickname();
    }

    /**
     * Start-up routine.
     */
    public void initialize() {
        courseName.setText(LocalData.getRoomName());
        // Start a timer and create a separate thread on it to automatically call retrieveQuestions()
        Timer timer = new Timer(true);

        StudentViewUIController reference = this;
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
        StudentViewLogic.retrieveServerData(roomID, questionContainer, questionList, pollWindow);
    }

    /**
     * Send question.
     */
    public void sendQuestion() {
        StudentViewLogic.sendQuestion(roomID, answerBox);
        retrieveQuestions();

    }


    /**
     * Upvote questions.
     *
     * @param questionId the id of the question that is being upvoted
     */
    public void upvoteQuestion(int questionId) {
        StudentViewLogic.upvoteQuestion(roomID, questionId, questionContainer, questionList);
    }

    /**
     * Remove Upvote of questions.
     *
     * @param questionId the id of the question that is being down-voted
     */
    public void removeUpvoteQuestion(int questionId) {
        StudentViewLogic.removeUpvoteQuestion(roomID, questionId, questionContainer, questionList);
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
     * Handle button action for button Mode from Light to Dark.
     * @param event the event
     */
    public void handleButtonMode(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/StudentViewUI(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Mode from Dark to Light.
     * @param event the event
     */
    public void handleButtonMode2(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/StudentViewUI.fxml");
    }

    /**
     * Handle button action for Help Button Light Mode.
     * @param event the event
     */
    public void handleHelpButtonLight(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Help is clicked, change to Help scene
        EntryRoomDisplay.setCurrentScene("/HelpFileStudent.fxml");
    }

    /**
     * Handle button action for Help Button Dark Mode.
     * @param event the event
     */
    public void handleHelpButtonDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Help is clicked, change to Help scene
        EntryRoomDisplay.setCurrentScene("/HelpFileStudent(DARKMODE).fxml");
    }

    /**
     * Handle button action for Answered Questions Button light Mode.
     */
    public void answeredQuestionList() {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Answered questions is clicked, change to Answered Questions (lightmode) scene
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestionsStudent.fxml");
    }

    /**
     * Handle button action for Answered Questions Button Dark Mode.
     */
    public void answeredQuestionListDark() {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Answered questions is clicked, change to Answered Questions (darkmode) scene
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestionsStudent(DARKMODE).fxml");
    }

    /**
     * Handle button action for List Users Button light Mode.
     */
    public void listofUsers() {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if list of Users is clicked, change to List of Users scene
        EntryRoomDisplay.setCurrentScene("/ListUsersStudent.fxml");
    }

    /**
     * Handle button action for List Users Button dark Mode.
     */
    public void listofUsersDark() {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if list of Users is clicked, change to List of Users scene
        EntryRoomDisplay.setCurrentScene("/ListUsersStudent(DARKMODE).fxml");
    }

    /**
     * Handle button action for deleting a question.
     */
    public void deleteQuestion() {
//        StudentViewLogic.deleteQuestion(roomID, questionId, questionContainer, questionList);
    }

    /**
     * Handle button slower.
     */
    public void goSlower() {

        colorSlow.setVisible(true);

        PauseTransition transition = new PauseTransition(Duration.seconds(3));
        transition.setOnFinished(event -> {
            colorSlow.setVisible(false);
        });
        ServerCommunication.setSpeed(roomID, 1);

        transition.play();
    }

    /**
     * Handle button faster.
     */
    public void goFaster() {

        colorFast.setVisible(true);

        PauseTransition transition = new PauseTransition(Duration.seconds(3));
        transition.setOnFinished(event -> {
            colorFast.setVisible(false);
        });
        ServerCommunication.setSpeed(roomID, -1);

        transition.play();
    }
}
