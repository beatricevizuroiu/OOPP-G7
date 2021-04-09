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
    public Button whiteFast;

    @FXML
    public Button colorFast;

    @FXML
    public Button whiteSlow;

    @FXML
    public Button colorSlow;

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
        courseName.setText(LocalData.getRoomName());
        StudentViewLogic.retrieveServerData(roomID, questionContainer, questionList, pollWindow);
    }

    /**
     * Send question. TODO: DO THE LOGIC THING
     */
    public void sendQuestion() {
        HttpResponse<String> response = StudentServerCommunication.askQuestion(roomID, new QuestionText(answerBox.getText()));
        if (response.statusCode() == 429) {
            Optional<String> header = response.headers().firstValue("X-Ratelimit-Expires");
            if (header.isPresent()) {
                int timeLeft = Integer.parseInt(header.get());

                Alert alert = new Alert(Alert.AlertType.WARNING);

                // body of pop-up with what the user entered
                alert.setContentText("You are asking questions too fast!\n"
                        + "Time remaining until you can ask a new question: "
                        + timeLeft
                        + " second(s)");

                // set types of buttons for the pop-up
                ButtonType okButton = new ButtonType("OK");

                alert.getButtonTypes().setAll(okButton);

                // wait for the alert to appear
                alert.showAndWait();
                return;
            }
            System.err.println("A rate limit status was returned but the rate limit header does not exist!");
        }
        answerBox.setText("");
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
