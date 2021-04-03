package nl.tudelft.oopp.g7.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.communication.ModeratorServerCommunication;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.ModeratorViewLogic;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.common.UserInfo;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TAViewController {
    private final String roomID;
    private final String nickname;
    private final String moderatorPassword;
    private final String studentPassword;

    @FXML
    public ScrollPane questionList;
    @FXML
    private VBox questionContainer;

    /**
     * The constructor for TAViewController.
     */
    public TAViewController() {
        roomID = LocalData.getRoomID();
        nickname = LocalData.getNickname();
        moderatorPassword = LocalData.getModeratorPassword();
        studentPassword = LocalData.getStudentPassword();

        System.out.println(roomID);

        // Start a timer and create a separate thread on it to automatically call retrieveQuestions()
        Timer timer = new Timer(true);

        TAViewController reference = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(reference::retrieveQuestions);
            }
        }, 0L, 500L);
    }

    /**
     * Retrieve all questions to List sorted by new.
     */
    public void retrieveQuestions() {
        // Retrieve all of the questions and then put them into question pane
        ModeratorViewLogic.retrieveAllQuestions(roomID, questionContainer, questionList);
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
        EntryRoomDisplay.setCurrentScene("/TAViewUI(DARKMODE).fxml");
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
        EntryRoomDisplay.setCurrentScene("/TAViewUI.fxml");
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
        EntryRoomDisplay.setCurrentScene("/HelpFileTA.fxml");
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
        EntryRoomDisplay.setCurrentScene("/HelpFileTA(DARKMODE).fxml");
    }

    /**
     * Handle button action for Answered Questions Button light Mode.
     *
     */
    public void answeredQuestionList(){
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Answered questions is clicked, change to Answered Questions (lightmode) scene
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestionsModerator.fxml");
    }

    /**
     * Handle button action for Answered Questions Button Dark Mode.
     *
     */
    public void answeredQuestionListDark(){
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Answered questions is clicked, change to Answered Questions (darkmode) scene
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestionsModerator(DARKMODE).fxml");
    }

    /**
     * Handle button action for List Users Button light Mode.
     *
     */
    public void listofUsers () {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if list of Users is clicked, change to List of Users scene
        EntryRoomDisplay.setCurrentScene("/ListUsers.fxml");
    }

    /**
     * Handle button action for List Users Button dark Mode.
     *
     */
    public void listofUsersDark () {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if list of Users is clicked, change to List of Users scene
        EntryRoomDisplay.setCurrentScene("/ListUsers(DARKMODE).fxml");
    }

    /**
     * Handle button action for deleting a question.
     */
    public void deleteQuestion () {
//        ModeratorViewLogic.deleteQuestion(roomID, questionID, questionContainer, questionList);
    }

    /**
     * Handle button action for editing a question.
     */
    public void editQuestion (){
//        ModeratorViewLogic.editQuestion(roomID, questionID, questionContainer, questionList);
    }

    /**
     * Handle button action for banning a user.
     */
    public void banUser (){
        //TODO
    }

    /**
     * Handle button action for answering a question.
     *
     */
    public void answerQuestion (){
        /*HttpResponse<String> response = ModeratorServerCommunication.answerQuestion(roomID, new QuestionText(answerBox.getText()));
        answerBox.setText("");
        retrieveQuestions();*/
    }

    /**
     * Handle button action for creating a poll.
     *
     */
    public void createPoll(){
        //TODO
    }

    /**
     * Handle button action for exporting questions.
     */
    public void exportQuestions(){
        ModeratorViewLogic.exportQuestions(roomID);
    }

}
