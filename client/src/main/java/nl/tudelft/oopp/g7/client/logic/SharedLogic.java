package nl.tudelft.oopp.g7.client.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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
import java.util.stream.Collectors;

public class SharedLogic {
    /**
     * Create a new question node and add it to the list of question nodes to be displayed.
     * @param roomID The room ID that the questions belong to.
     * @param questionNodes The list of questions nodes that the new question node should be added to.
     * @param componentName The name of the FXML of that should be used to create the question node.
     * @param question The {@link Question} object to create the question node from
     * @param textArea TextArea representing answerBox
     * @param answerButton post answer button
     * @param questionContainer The container that contains the list of questions.
     * @param questionList The javaFX {@link ScrollPane} that wraps the list of questions to add scrolling.
     * @throws IOException If JavaFX can not load the FXML file.
     */
    public static void addQuestionToUI(String roomID, List<Node> questionNodes, String componentName, Question question, TextArea textArea, Button answerButton,
                                       VBox questionContainer, ScrollPane questionList) throws IOException{
        HBox questionNode = FXMLLoader.load(ModeratorViewLogic.class.getResource(componentName));

        Text upvoteCount = (Text) questionNode.lookup("#QuestionUpvoteCount");
        Text body = (Text) questionNode.lookup("#QuestionText");
        Text authorText = (Text) questionNode.lookup("#QuestionAuthor");
        VBox answerBox = (VBox) questionNode.lookup("#QuestionAnswerBox");
        Text answerText = (Text) questionNode.lookup("#QuestionAnswer");

        Button upvoteBtn = (Button) questionNode.lookup("#QuestionUpvoteBtn");


        if (upvoteBtn != null) { // upvoteBtn means student view
            upvoteBtn.setOnAction((event) -> StudentViewLogic.upvoteQuestion(roomID, question.getId(), questionContainer, questionList));

            // set delete button for student
            if (question.getAuthorId().equals(LocalData.getUserID())) {
                Button deleteBtn = (Button) questionNode.lookup("#StudentDeleteBtn");
                deleteBtn.setOnAction((event) -> StudentViewLogic.deleteQuestionStudent(roomID, question.getId(), questionContainer, questionList));
                deleteBtn.setVisible(true);
            }
        } else { // no upvoteBtn in mod view
            // set reply button
            Button replyButton = (Button) questionNode.lookup("#QuestionReplyBtn");
            replyButton.setOnAction((event) -> ModeratorViewLogic.answerQuestion(roomID, question, textArea, answerButton, questionContainer, questionList));

            MenuButton menuButton = (MenuButton) questionNode.lookup("#MenuButton");
            // set edit button
            menuButton.getItems().get(0).setOnAction((event) -> ModeratorViewLogic.editQuestion(roomID, question, textArea, answerButton, questionContainer, questionList));
            // set delete button
            menuButton.getItems().get(1).setOnAction((event) -> ModeratorViewLogic.deleteQuestionMod(roomID, question.getId(), textArea, answerButton, questionContainer, questionList));
            // set ban button
            menuButton.getItems().get(2).setOnAction((event) -> ModeratorViewLogic.banUser(roomID, question.getAuthorId()));
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
     * Retrieves all users from the server and puts them into the user information list.
     * @param roomID ID of the room users belong
     * @param userListContainer VBox containing the UI elements
     */
    public static void retrieveAllUsers(String roomID, VBox userListContainer, String component, String componentDarkMode) {
        // list of questions containing the questions received from the server
        List<UserInfo> userInfoList = ServerCommunication.retrieveAllUsers(roomID);
        List<Node> userInfoNodes = userListContainer.getChildren();

        // Sort the user list with User Role as primary and Nickname secondary keys
        userInfoList = userInfoList.stream().sorted((o1, o2) -> {
            // If roles are same compare alphabetically
            if (o1.getUserRole() == o2.getUserRole()) {
                return o1.getNickname().compareToIgnoreCase(o2.getNickname());
            }

            // MODERATOR comes before STUDENT
            return o1.getUserRole() == UserRole.MODERATOR ? -1 : 1;
        }).collect(Collectors.toList());

        userInfoNodes.clear();

        // implement dark mode
        String componentName =  "/components/" + (userListContainer.getStyleClass().contains("DarkUsersContainer") ? componentDarkMode : component);

        try {
            for (UserInfo userInfo : userInfoList) {
                SharedLogic.addUserInfoToUI(
                        roomID,
                        userInfoNodes,
                        componentName,
                        userInfo
                );
            }
        } catch (IOException ignored) {
            System.err.println("A problem occurred");
        }
    }

    /**
     * Create a new user information node and add it to the nodes to be displayed.
     * @param userInfoNodes A list of user info nodes the new node should be added to.
     * @param componentName The name of the FXML of that should be used to create the question node.
     * @param userInfo {@link UserInfo} object to create the user information node from.
     * @throws IOException If JavaFX cannot load the FXML file.
     */
    public static void addUserInfoToUI(String roomID, List<Node> userInfoNodes, String componentName, UserInfo userInfo) throws IOException {
        if (userInfo.getNickname().isEmpty() || userInfo.getNickname() == null) {
            return;
        }

        HBox userInfoContainer = FXMLLoader.load(ModeratorViewLogic.class.getResource(componentName));

        Text userNickname = (Text) userInfoContainer.lookup("#UserNickname");
        Text userRoleInfo = (Text) userInfoContainer.lookup("#UserRoleInfo");

        Button banButton = (Button) userInfoContainer.lookup("#BanButton");

        // if moderator view is used add event listeners
        if (banButton != null) {
            banButton.setOnAction((event) -> ModeratorViewLogic.banUser(roomID, userInfo.getId()));
        }

        String userRoleText = userInfo.getUserRole() == UserRole.STUDENT ? "Student" : "Moderator";

        // handle really long cases
        if (userInfo.getNickname().length() > 35) {
            userNickname.setText(userInfo.getNickname().substring(0, 35) + "... ");
        } else {
            userNickname.setText(userInfo.getNickname() + " ");
        }

        // if the user is current current user, add a little icon
        if (userInfo.getId().equals(LocalData.getUserID())) {
            if (banButton != null) {
                banButton.setVisible(false);
            }

            SVGPath star = (SVGPath) userInfoContainer.lookup("#StarIcon");
            star.setContent("M 8.5 12.234375 L 12.878906 14.875 L 11.714844 9.894531 L 15.582031 6.546875 L 10.492188 6.113281 L 8.5 1.417969 L 6.507812 6.113281 L 1.417969 6.546875 L 5.285156 9.894531 L 4.121094 14.875 Z M 8.5 12.234375");
        }

        userRoleInfo.setText(userRoleText);

        userInfoNodes.add(userInfoContainer);
    }
}
