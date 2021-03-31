package nl.tudelft.oopp.g7.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.ModeratorViewLogic;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;

import java.util.Timer;
import java.util.TimerTask;

public class LecturerViewController {
    private final String roomID;
    private final String nickname;
    private final String moderatorPassword;
    private final String studentPassword;

    @FXML
    public ScrollPane questionList;
    @FXML
    private VBox questionContainer;

    /**
     * The constructor for LecturerViewController.
     */
    public LecturerViewController() {
        roomID = LocalData.getRoomID();
        nickname = LocalData.getNickname();
        moderatorPassword = LocalData.getModeratorPassword();
        studentPassword = LocalData.getStudentPassword();

        System.out.println(roomID);

        // Start a timer and create a separate thread on it to automatically call retrieveQuestions()
        Timer timer = new Timer(true);

        LecturerViewController reference = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(reference::retrieveQuestions);
            }
        }, 0L, 500L);
    }

    /**
     * Update the list of questions sorted by most upvotes.
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
        EntryRoomDisplay.setCurrentScene("/LecturerViewUI(DARKMODE).fxml");
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
        EntryRoomDisplay.setCurrentScene("/LecturerViewUI.fxml");
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
        EntryRoomDisplay.setCurrentScene("/HelpFileLecturer.fxml");
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
        EntryRoomDisplay.setCurrentScene("/HelpFileLecturer(DARKMODE).fxml");
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
     *
     */
    public void deleteQuestion () {
       //ModeratorViewLogic.deleteQuestion(roomID, questionId, questionContainer, questionList);
    /

    /**
     * Handle button action for editing a question.
     */
    public void editQuestion (){
        //TODO
    }

    /**
     * Handle button action for answering a question.
     */
    public void answerQuestion (){
        /*HttpResponse<String> response = ModeratorServerCommunication.answerQuestion(roomID, new QuestionText(answerBox.getText()));
        answerBox.setText("");
        retrieveQuestions();*/
    }

    /**
     * Handle button action for creating a poll.
     */
    public void createPoll(){
        //TODO
    }

    /**
     * Handle button action for exporting questions.
     */
    public void exportQuestions(){
        //TODO
    }

    /**
     * Handle button action for creating a  link.
     */
    public void createLink(){
        //TODO
    }

//    public void speedIndicator(){
//        //TODO
//    }
}
