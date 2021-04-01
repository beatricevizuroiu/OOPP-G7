package nl.tudelft.oopp.g7.client.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.UserInfo;
import nl.tudelft.oopp.g7.common.UserRole;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentViewLogic {
    /**
     * Retrieves all questions from the server, puts them into the question panel and also adds events listeners to the upvote buttons.
     * @param roomID ID of the room questions are in
     * @param questionContainer VBox containing the UI elements.
     * @param questionList ScrollPane containing the whole list of questions.
     */
    public static void retrieveAllQuestions(String roomID, VBox questionContainer, ScrollPane questionList) {
        // Store the current position of the user in the scroll list
        double scrollHeight = questionList.getVvalue();

        List<Question> questions = StudentServerCommunication.retrieveAllQuestions(roomID);
        List<Node> questionNodes = questionContainer.getChildren();

        questionNodes.clear();

        String componentName = EntryRoomDisplay.isDarkMode() ? "/components/StudentQuestion(DARKMODE).fxml" : "/components/StudentQuestion.fxml";

        try {
            for (Question question : questions) {
                SharedLogic.addQuestionToUI(
                        roomID,
                        questionNodes,
                        componentName,
                        question,
                        questionContainer,
                        questionList
                );
            }
        } catch (IOException ignored) {
            System.err.println("A problem occurred.");
        }

        // Return the user to their original position in the scroll list
        questionList.setVvalue(scrollHeight + 0);
    }

    /**
     * Upvotes a question and refreshes the question list.
     * @param roomID ID of the room question is in.
     * @param questionId ID of the specified question.
     * @param questionContainer VBox containing the UI elements.
     * @param questionList ScrollPane containing the whole list of questions.
     */
    public static void upvoteQuestion(String roomID, int questionId, VBox questionContainer, ScrollPane questionList) {
        ServerCommunication.upvoteQuestion(roomID, questionId);
        retrieveAllQuestions(roomID, questionContainer, questionList);
    }

    /**
     * Deletes a question and refreshes the question list.
     * @param roomID ID of the room question is in.
     * @param questionId ID of the specified question.
     * @param questionContainer VBox containing the UI elements.
     * @param questionList ScrollPane containing the whole list of questions.
     */
    public static void deleteQuestion(String roomID, int questionId, VBox questionContainer, ScrollPane questionList) {
        ServerCommunication.deleteQuestion(roomID, questionId);
        retrieveAllQuestions(roomID, questionContainer, questionList);
    }

    /**
     * Retrieves all users from the server and puts them into the user information list.
     * @param roomID ID of the room users belong
     * @param userListContainer VBox containing the UI elements
     */
    public static void retrieveAllUsers(String roomID, VBox userListContainer) {
        // list of questions containing the questions received from the server
        List<UserInfo> userInfoList = ServerCommunication.retrieveAllUsers(roomID);
        List<Node> userInfoNodes = userListContainer.getChildren();

        // Sort the user list with User Role as primary and Nickname secondary keys
        userInfoList = userInfoList.stream().sorted((Comparator<UserInfo>) (o1, o2) -> {
            // If roles are same compare alphabetically
            if (o1.getUserRole() == o2.getUserRole()) {
                return o1.getNickname().compareToIgnoreCase(o2.getNickname());
            }

            // MODERATOR comes before STUDENT
            return o1.getUserRole() == UserRole.MODERATOR ? -1 : 1;
        }).collect(Collectors.toList());

        userInfoNodes.clear();

        String componentName =  "/components/StudentUserInfoContainer.fxml";

        try {
            for (UserInfo userInfo : userInfoList) {
                SharedLogic.addUserInfoToUI(
                        userInfoNodes,
                        componentName,
                        userInfo
                );
            }
        } catch (IOException ignored) {
            System.err.println("A problem occurred");
        }
    }
}
