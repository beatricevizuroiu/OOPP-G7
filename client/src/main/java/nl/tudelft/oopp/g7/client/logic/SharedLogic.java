package nl.tudelft.oopp.g7.client.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.UserInfo;
import nl.tudelft.oopp.g7.common.UserRole;

import java.io.IOException;
import java.util.List;

public class SharedLogic {
    private static boolean isNicknameChosen = false;

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

    /**
     * Create a new user information node and add it to the nodes to be displayed.
     * @param userInfoNodes A list of user info nodes the new node should be added to.
     * @param componentName The name of the FXML of that should be used to create the question node.
     * @param userInfo {@link UserInfo} object to create the user information node from.
     * @throws IOException If JavaFX cannot load the FXML file.
     */
    public static void addUserInfoToUI(List<Node> userInfoNodes, String componentName, UserInfo userInfo) throws IOException {
        // TODO: It can be handled better by assigning random nicknames at room join
        // Right now, it doesn't display if someone doesn't have a nickname
        if (userInfo.getNickname().isEmpty() || userInfo.getNickname() == null) {
            return;
        }

        HBox userInfoContainer = FXMLLoader.load(ModeratorViewLogic.class.getResource(componentName));

        Text userNickname = (Text) userInfoContainer.lookup("#UserNickname");
        Text userRoleInfo = (Text) userInfoContainer.lookup("#UserRoleInfo");

        String userRoleText = userInfo.getUserRole() == UserRole.STUDENT ? "Student" : "Moderator";

        // handle really long cases
        if (userInfo.getNickname().length() > 35) {
            userNickname.setText(userInfo.getNickname().substring(0, 35) + "... ");
        } else {
            userNickname.setText(userInfo.getNickname() + " ");
        }

        // if the user is current current user, add a little icon
        if (!isNicknameChosen && userInfo.getNickname().equals(LocalData.getNickname())) {
            // first of identical nicknames is taken as local nickname
            isNicknameChosen = true;

            SVGPath star = (SVGPath) userInfoContainer.lookup("#StarIcon");
            star.setContent("M 8.5 12.234375 L 12.878906 14.875 L 11.714844 9.894531 L 15.582031 6.546875 L 10.492188 6.113281 L 8.5 1.417969 L 6.507812 6.113281 L 1.417969 6.546875 L 5.285156 9.894531 L 4.121094 14.875 Z M 8.5 12.234375");
        }

        userRoleInfo.setText(userRoleText);

        userInfoNodes.add(userInfoContainer);
    }
}
