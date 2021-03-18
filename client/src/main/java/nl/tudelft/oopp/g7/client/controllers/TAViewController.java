package nl.tudelft.oopp.g7.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.client.communication.localData;
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
        roomID = localData.getRoomID();
        nickname = localData.getNickname();
        moderatorPassword = localData.getModeratorPassword();
        studentPassword = localData.getStudentPassword();

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
}
