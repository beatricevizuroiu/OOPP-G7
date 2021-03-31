package nl.tudelft.oopp.g7.client.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.communication.ModeratorServerCommunication;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.ExportQuestion;
import nl.tudelft.oopp.g7.common.Question;

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
                HBox questionNode = FXMLLoader.load(ModeratorViewLogic.class.getResource(componentName));

                Text upvoteCount = (Text) questionNode.lookup("#QuestionUpvoteCount");
                Text body = (Text) questionNode.lookup("#QuestionText");
                Text authorText = (Text) questionNode.lookup("#QuestionAuthor");

                upvoteCount.setText(Integer.toString(Math.min(question.getUpvotes(), 999)));
                body.setText(question.getText());

                if (!LocalData.userMap.containsKey(question.getAuthorId())) {
                    LocalData.userMap.put(question.getAuthorId(), ServerCommunication.retrieveUserById(roomID, question.getAuthorId()));
                }
                authorText.setText(LocalData.userMap.get(question.getAuthorId()).getNickname() + " asks");

                questionNodes.add(questionNode);
            }
        } catch (IOException ignored) {
            System.err.println("A problem occurred while retrieving questions.");
        }

        // Store the current position of the user in the scroll list
        questionList.setVvalue(scrollHeight + 0);
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
}
