package nl.tudelft.oopp.g7.client.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.g7.client.communication.ModeratorServerCommunication;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.BanReason;
import nl.tudelft.oopp.g7.common.ExportQuestion;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModeratorViewLogic {
    private static int i = 1;

    /**
     * Retrieves all questions from the server and puts them into the question panel.
     * @param roomID ID of the room questions are in
     * @param questionContainer VBox containing the UI elements.
     * @param questionList ScrollPane containing the whole list of questions.
     */
    public static void retrieveAllQuestions(String roomID, VBox questionContainer, ScrollPane questionList) {
        // Store the current position of the user in the scroll list
        double scrollHeight = questionList.getVvalue();

        // list of questions containing the questions received from the server
        List<Question> questions = ModeratorServerCommunication.retrieveAllQuestions(roomID);
        List<Node> questionNodes = questionContainer.getChildren();

        questionNodes.clear();

        String componentName = EntryRoomDisplay.isDarkMode() ? "/components/LecturerQuestion(DARKMODE).fxml" : "/components/LecturerQuestion.fxml";

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
            System.err.println("A problem occurred while retrieving questions.");
        }

        // Store the current position of the user in the scroll list
        questionList.setVvalue(scrollHeight + 0);
    }

    /**
     * Deletes a question and refreshes the question list.
     * @param roomID ID of the room question is in.
     * @param questionID ID of the specified question.
     * @param questionContainer VBox containing the UI elements.
     * @param questionList ScrollPane containing the whole list of questions.
     */
    public static void deleteQuestion(String roomID, int questionID, VBox questionContainer, ScrollPane questionList) {
        ServerCommunication.deleteQuestion(roomID, questionID);
        retrieveAllQuestions(roomID, questionContainer, questionList);
    }

    /**
     * Edits a question and refreshes the question list.
     * @param roomID ID of the room question is in.
     * @param questionID ID of the specified question.
     * @param questionContainer VBox containing the UI elements.
     * @param questionList ScrollPane containing the whole list of questions.
     */
    public static void editQuestion(String roomID, int questionID,QuestionText questionText, VBox questionContainer, ScrollPane questionList) {
        ServerCommunication.editQuestion(roomID, questionID, questionText);
        retrieveAllQuestions(roomID, questionContainer, questionList);
    }

    public static void answerQuestion (String roomID, int questionID,QuestionText questionText, VBox questionContainer, ScrollPane questionList){
        ServerCommunication.answerQuestion(roomID, questionID, questionText);
        retrieveAllQuestions(roomID, questionContainer, questionList);
    }

    /**
     * Export all Questions in a Room to a JSON formatted text file.
     * @param roomID the roomID of the Room to export the Questions from.
     */
    public static void exportQuestions(String roomID) {

        List<Question> questions = StudentServerCommunication.retrieveAllQuestions(roomID);
        List<ExportQuestion> exportQuestions = new ArrayList<>();
        String userId;
        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").setPrettyPrinting().create();

        try {
            FileWriter fileWriter = new FileWriter(LocalData.getRoomName() + i++ + ".txt");
            for (Question question : questions) {

                userId = question.getAuthorId();
                if (!LocalData.userMap.containsKey(userId)) {
                    LocalData.userMap.put(userId, ServerCommunication.retrieveUserById(roomID, userId));
                }

                exportQuestions.add(new ExportQuestion(
                        LocalData.userMap.get(userId).getNickname(),
                        question.getText(),
                        question.getAnswer(),
                        question.getPostedAt(),
                        question.getUpvotes(),
                        question.isAnswered(),
                        question.isEdited()
                        )
                );
            }
            gson.toJson(exportQuestions, fileWriter);
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("File couldn't be written!" + e);
        }
    }

    /**
     * Ban a user.
     * @param roomID ID of the room user is in.
     * @param userID ID of the user that will be banned.
     */
    public static void banUser(String roomID, String userID) {
        ModeratorServerCommunication.banUser(roomID, userID, banReasonDialog());
    }

    /**
     * Create a pop-up window that asks for banning reason. Leaving blank will result in an empty response.
     * @return a ban reason
     */
    public static BanReason banReasonDialog() {
        String defaultValue = "Leave blank for default reason.";

        TextInputDialog textInputDialog = new TextInputDialog(defaultValue);
        textInputDialog.setHeaderText("Please provide a reason for banning.");
        textInputDialog.showAndWait();

        String reason = textInputDialog.getEditor().getText();

        // check for defaulting
        if (reason.equals(defaultValue)) {
            reason = "";
        }

        return new BanReason(reason);
    }

    /**
     * Displays an alert box that show links and passwords.
     */
    public static void displayLinkAndPasswords() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Taken from StackOverflow to make alert box text editable
        // https://stackoverflow.com/a/45621264/14196175
        TextArea textArea = new TextArea(String.format("""
                        Room ID: %s
                        Moderator Password: %s
                        Student Password: %s""",
                LocalData.getRoomID(), LocalData.getModeratorPassword(), LocalData.getStudentPassword()));
        textArea.setEditable(false);
        textArea.setWrapText(true);

        GridPane gridPane = new GridPane();
        gridPane.add(textArea, 0, 0);

        alert.setTitle("Invitations");
        alert.setHeaderText("Room Join Information:");
        alert.getDialogPane().setContent(gridPane);
        // TODO: ask help for a better solution
        alert.getDialogPane().setPrefHeight(200);

        alert.showAndWait();
    }
}
