package nl.tudelft.oopp.g7.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.client.communication.LocalData;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.Question;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TAViewController {
    private final String roomID;
    private final String nickname;
    private final String moderatorPassword;
    private final String studentPassword;
    private int i = 0;

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
        Timer timer = new Timer();

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

        // Store the current position of the user in the scroll list
        double scrollHeight = questionList.getVvalue();

        // list of questions containing the questions received from the server
        List<Question> questions = StudentServerCommunication.retrieveAllQuestions(roomID);
        List<Node> questionNodes = questionContainer.getChildren();

        questionNodes.clear();

        try {
            for (Question question : questions) {
                HBox questionNode = FXMLLoader.load(getClass().getResource("/components/LecturerQuestion.fxml"));

                Text upvoteCount = (Text) questionNode.lookup("#QuestionUpvoteCount");
                Text body = (Text) questionNode.lookup("#QuestionText");

                upvoteCount.setText(Integer.toString(Math.min(question.getUpvotes(), 999)));
                body.setText(question.getText());

                questionNodes.add(questionNode);
            }
        } catch (IOException ignored) {
            System.err.println("A problem occurred");
        }

        // Return the user to their original position in the scroll list
        questionList.setVvalue(scrollHeight + 0);
    }

    /**
     * Handle button action for button Mode from Light.
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
     * Handle button action for button Mode from Dark.
     *
     * @param event the event
     */
    public void handleButtonMode2(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/TAViewUI.fxml");
    }

    // This is for the help button I am not actually asking for help
    public void help(){
        //TODO
    }

    public void answeredQuestionList(){
        //TODO
    }

    public void listofUsers () {
        //TODO
    }

    public void modetoDark () {
        //TODO (go to dark mode)
    }

    public void modetoLight () {
        //TODO (go to light mode)
    }

    public void createPoll(){
        //TODO
    }

    public void exportQuestions(){
        //TODO
    }
}
