package nl.tudelft.oopp.g7.client.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.common.Question;

import java.io.IOException;
import java.util.List;

public class SharedLogic {

    public static void addQuestionToUI(String roomID, List<Node> questionNodes, String componentName, Question question, VBox questionContainer, ScrollPane questionList) throws IOException {
        HBox questionNode = FXMLLoader.load(ModeratorViewLogic.class.getResource(componentName));

        Text upvoteCount = (Text) questionNode.lookup("#QuestionUpvoteCount");
        Text body = (Text) questionNode.lookup("#QuestionText");
        Text authorText = (Text) questionNode.lookup("#QuestionAuthor");
        VBox answerBox = (VBox) questionNode.lookup("#QuestionAnswerBox");
        Text answerText = (Text) questionNode.lookup("#QuestionAnswer");

        Button upvoteBtn = (Button) questionNode.lookup("#QuestionUpvoteBtn");

        if (upvoteBtn != null) {
            upvoteBtn.setOnAction((event) -> StudentViewLogic.upvoteQuestion(roomID, question.getId(), questionContainer, questionList));
        }

        upvoteCount.setText(Integer.toString(Math.min(question.getUpvotes(), 999)));
        body.setText(question.getText());

        if (!LocalData.userMap.containsKey(question.getAuthorId())) {
            LocalData.userMap.put(question.getAuthorId(), ServerCommunication.retrieveUserById(roomID, question.getAuthorId()));
        }
        authorText.setText(LocalData.userMap.get(question.getAuthorId()).getNickname() + " asks");

        if ("".equals(question.getAnswer())) {
            answerBox.setMaxSize(0, 0);
            answerBox.setMinSize(0, 0);
            answerBox.getChildren().clear();
        } else {
            answerText.setText(question.getAnswer());
        }

        questionNodes.add(questionNode);
    }
}
