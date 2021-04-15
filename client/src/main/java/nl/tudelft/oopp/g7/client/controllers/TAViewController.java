package nl.tudelft.oopp.g7.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.MainApp;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.ModeratorViewLogic;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;

import java.util.Timer;
import java.util.TimerTask;

public class TAViewController {
    private final String roomID;

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
     * The constructor for TAViewController.
     */
    public TAViewController() {
        roomID = LocalData.getRoomID();
    }

    /**
     * Start-up routine.
     */
    public void initialize() {
        SharedLogic.displayCourseName(courseName);
        // Start a timer and create a separate thread on it to automatically call retrieveQuestions()
        Timer timer = new Timer(true);

        TAViewController reference = this;
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
        ModeratorViewLogic.retrieveServerData(roomID, answerBox, postAnswerButton, questionContainer, questionList, pollWindow);
    }

    /**
     * Display link and passwords.
     */
    public void displayLinkAndPassword() {
        ModeratorViewLogic.displayLinkAndPasswords();
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
        LocalData.switchColorScheme();
    }

    /**
     * Handle button action for Help Button Light Mode.
     * @param event the event
     */
    public void handleHelpButtonLight(ActionEvent event) {
        // if Help is clicked, change to Help scene
        MainApp.setCurrentScene("/views/HelpFileTA.fxml");
    }

    /**
     * Handle button action for Help Button Dark Mode.
     * @param event the event
     */
    public void handleHelpButtonDark(ActionEvent event) {
        // if Help is clicked, change to Help scene
        MainApp.setCurrentScene("/HelpFileTA(DARKMODE).fxml");
    }

    /**
     * Handle button action for Answered Questions Button light Mode.
     */
    public void answeredQuestionList() {
        // if Answered questions is clicked, change to Answered Questions (lightmode) scene
        MainApp.setCurrentScene("/views/AnsweredQuestionsModerator.fxml");
    }

    /**
     * Handle button action for Answered Questions Button Dark Mode.
     */
    public void answeredQuestionListDark() {
        // if Answered questions is clicked, change to Answered Questions (darkmode) scene
        MainApp.setCurrentScene("/AnsweredQuestionsModerator(DARKMODE).fxml");
    }

    /**
     * Handle button action for List Users Button light Mode.
     */
    public void listofUsers() {
        // if list of Users is clicked, change to List of Users scene
        MainApp.setCurrentScene("/views/ListUsersModerator.fxml");
    }

    /**
     * Handle button action for List Users Button dark Mode.
     */
    public void listofUsersDark() {
        // if list of Users is clicked, change to List of Users scene
        MainApp.setCurrentScene("/ListUsersModerator(DARKMODE).fxml");
    }

    /**
     * Handle button action for creating a poll.
     */
    public void createPoll() {
        MainApp.setCurrentScene("/views/CreatePoll.fxml");
    }

    /**
     * Handle button action for creating a poll.
     */
    public void createPoll2() {
        MainApp.setCurrentScene("/CreatePoll(DARKMODE).fxml");
    }

    /**
     * Handle button action for exporting questions.
     */
    public void exportQuestions() {
        ModeratorViewLogic.exportQuestions(roomID);
    }

    public void switchView(ActionEvent actionEvent) {
        MainApp.setCurrentScene("/views/LecturerViewUI.fxml");
    }

    public void switchView2(ActionEvent actionEvent) {
        MainApp.setCurrentScene("/LecturerViewUI(DARKMODE).fxml");
    }
}
