package nl.tudelft.oopp.g7.client.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.common.Question;

import java.io.IOException;
import java.util.List;

public class AnsweredQuestionsLogic {
    public static void retrieveAllAnsweredQuestions(VBox answeredQuestionContainer, ScrollPane answeredQuestionList) {
        // Store the current position of the user in the scroll list
        double scrollHeight = answeredQuestionList.getVvalue();

        // list of questions containing the questions received from the server
        List<Question> questions = ServerCommunication.retrieveAllAnsweredQuestions(LocalData.getRoomID());
        List<Node> questionNodes = answeredQuestionContainer.getChildren();

        questionNodes.clear();

        try {
            for (Question question : questions) {
                HBox questionNode = FXMLLoader.load(AnsweredQuestionsLogic.class.getResource("/components/LecturerQuestion.fxml"));

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
        answeredQuestionList.setVvalue(scrollHeight + 0);
    }
}
