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

    /**
     * Create a new question node and add it to the list of question nodes to be displayed.
     * @param roomID The room ID that the questions belong to.
     * @param questionNodes The list of questions nodes that the new question node should be added to.
     * @param componentName The name of the FXML of that should be used to create the question node.
     * @param question The {@link Question} object to create the question node from.
     * @param questionContainer The container that contains the list of questions.
     * @param questionList The javaFX {@link ScrollPane} that wraps the list of questions to add scrolling.
     * @throws IOException If JavaFX can not load the FXML file.
     */
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
