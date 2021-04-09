package nl.tudelft.oopp.g7.client.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.common.Question;

import java.io.IOException;
import java.util.List;

public class AnsweredQuestionsLogic {
    /**
     * Retrieves all answered questions from the server and displays them in a scroll pane.
     * @param answeredQuestionContainer VBox containing UI elements
     * @param answeredQuestionList      ScrollPane that will contain the questions
     */
    public static void retrieveAllAnsweredQuestions(boolean isStudent, VBox answeredQuestionContainer, ScrollPane answeredQuestionList) {
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
                Text questionAnswer = (Text) questionNode.lookup("#QuestionAnswer");

                if (questionAnswer.getText().equals("")) {
                    questionAnswer.setText("Marked as answered.");
                }

                if (isStudent) {
                    MenuButton menuButton = (MenuButton) questionNode.lookup("#MenuButton");
                    Button markAsAnsweredBtn = (Button) questionNode.lookup("#MarkAsAnsweredBtn");
                    Button questionReplyBtn = (Button) questionNode.lookup("#QuestionReplyBtn");

                    menuButton.setVisible(false);
                    markAsAnsweredBtn.setVisible(false);
                    questionReplyBtn.setVisible(false);
                }

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
