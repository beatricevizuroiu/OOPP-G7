package nl.tudelft.oopp.g7.client.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.PollInfo;
import nl.tudelft.oopp.g7.common.PollOption;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class StudentViewLogic {
    private static final HashMap<Integer, Integer> selectedPollOptions = new HashMap<>();

    public static void retrieveServerData(String roomID, VBox questionContainer, ScrollPane questionList, VBox pollWindow) {
        retrieveAllQuestions(roomID, questionContainer, questionList);
        retrievePolls(roomID, pollWindow);
    }

    /**
     * Send questions in a Room.
     * @param roomId              The roomId of the Room to get the Poll from.
     * @param answerBox            The answerBox to answer the questions.
     */
    private void sendQuestion(String roomId, TextArea answerBox) {
        HttpResponse<String> response = StudentServerCommunication.askQuestion(roomId, new QuestionText(answerBox.getText()));
        if (response.statusCode() == 429) {
            Optional<String> header = response.headers().firstValue("X-Ratelimit-Expires");
            if (header.isPresent()) {
                int timeLeft = Integer.parseInt(header.get());

                Alert alert = new Alert(Alert.AlertType.WARNING);

                // body of pop-up with what the user entered
                alert.setContentText("You are asking questions too fast!\n"
                        + "Time remaining until you can ask a new question: "
                        + timeLeft
                        + " second(s)");

                // set types of buttons for the pop-up
                ButtonType okButton = new ButtonType("OK");

                alert.getButtonTypes().setAll(okButton);

                // wait for the alert to appear
                alert.showAndWait();
                return;
            }
            System.err.println("A rate limit status was returned but the rate limit header does not exist!");
        }

        if (response.statusCode() == 401) {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            // body of pop-up with what the user entered
            alert.setContentText("You are not allowed to ask a Question!\n"
                    + "The Room might be closed or you are currently banned.");

            // set types of buttons for the pop-up
            ButtonType okButton = new ButtonType ("OK");

            alert.getButtonTypes().setAll(okButton);

            // wait for the alert to appear
            alert.showAndWait();
            return;
        }
        answerBox.setText("");
    }

    /**
     * Retrieve any active Poll in a Room.
     * @param roomId              The roomId of the Room to get the Poll from.
     * @param pollWindowContainer The UI element to put the Poll in.
     */
    public static void retrievePolls(String roomId, VBox pollWindowContainer) {
        PollInfo poll = ServerCommunication.getPoll(roomId);
        if (poll == null) {
            return;
        }
        try {
            String pollWindowComponentName = EntryRoomDisplay.isDarkMode() ? "/components/PollWindow(DARKMODE).fxml" : "/components/PollWindow.fxml";
            HBox pollWindow = FXMLLoader.load(StudentViewLogic.class.getResource(pollWindowComponentName));

            Text pollLabel = (Text) pollWindow.lookup("#pollLabelText");
            HBox pollTextBox = (HBox) pollWindow.lookup("#pollTextBox");

            if (!poll.isAcceptingAnswers()) {
                pollTextBox.setStyle("-fx-background-color: #cf5454");
                pollLabel.setText("CLOSED POLL");
            } else {
                pollTextBox.setStyle("-fx-background-color: #9251ba");
                pollLabel.setText("POLL");
            }

            pollWindow.lookup("#deleteButton").setVisible(false);

            HBox optionContainer = (HBox) pollWindow.lookup("#PollWindowOptionContainer");
            List<Node> optionNodes = optionContainer.getChildren();

            optionNodes.clear();

            int columns = (int) Math.max(1, pollWindowContainer.getWidth() / 350);
            int rows = (int) Math.ceil(poll.getOptions().length * 1.0F / columns);

            String componentName = EntryRoomDisplay.isDarkMode() ? "/components/PollOption(DARKMODE).fxml" : "/components/PollOption.fxml";

            Text pollQuestionText = (Text) pollWindow.lookup("#PollQuestion");
            pollQuestionText.setText(poll.getQuestion());

            int selectedPollOption = selectedPollOptions.getOrDefault(poll.getId(), -1);

            int i = 0;
            VBox currContainer = new VBox();
            currContainer.setSpacing(10);
            HBox.setHgrow(currContainer, Priority.ALWAYS);
            for (PollOption pollOption : poll.getOptions()) {
                if (i == rows) {
                    optionNodes.add(currContainer);
                    currContainer = new VBox();
                    currContainer.setSpacing(10);
                    HBox.setHgrow(currContainer, Priority.ALWAYS);
                    i = 0;
                }
                i++;

                HBox optionNode = FXMLLoader.load(StudentViewLogic.class.getResource(componentName));

                Text pollOptionText = (Text) optionNode.lookup("#PollOptionLabel");
                Text pollOptionCount = (Text) optionNode.lookup("#PollOptionCount");

                String optionText = pollOption.getText();
                optionText = optionText.substring(0, Math.min(optionText.length(), 20));

                pollOptionText.setText(optionText);
                pollOptionCount.setText(Integer.toString(pollOption.getResultCount()));

                optionNode.getStyleClass().remove("selected");

                if (selectedPollOption == pollOption.getId()) {
                    optionNode.getStyleClass().add("selected");
                }

                optionNode.setOnMouseClicked((event) -> {
                    if (poll.isAcceptingAnswers()) {
                        StudentServerCommunication.answerPoll(roomId, pollOption.getId());
                        selectedPollOptions.put(poll.getId(), pollOption.getId());
                    }
                });

                currContainer.getChildren().add(optionNode);
            }

            optionNodes.add(currContainer);

            List<Node> children = pollWindowContainer.getChildren();
            children.clear();
            children.add(pollWindow);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Retrieves all questions from the server, puts them into the question panel and also adds events listeners to the upvote buttons.
     * @param roomID            ID of the room questions are in
     * @param questionContainer VBox containing the UI elements.
     * @param questionList      ScrollPane containing the whole list of questions.
     */
    private static void retrieveAllQuestions(String roomID, VBox questionContainer, ScrollPane questionList) {
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
                        null,
                        null,
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
     * @param roomID            ID of the room question is in.
     * @param questionId        ID of the specified question.
     * @param questionContainer VBox containing the UI elements.
     * @param questionList      ScrollPane containing the whole list of questions.
     */
    public static void upvoteQuestion(String roomID, int questionId, VBox questionContainer, ScrollPane questionList) {
        ServerCommunication.upvoteQuestion(roomID, questionId);
        LocalData.upvotedQuestions.add(questionId);
        retrieveAllQuestions(roomID, questionContainer, questionList);
    }

    /**
     * removes the upvote of a question and refreshes the question list.
     * @param roomID            ID of the room question is in.
     * @param questionId        ID of the specified question.
     * @param questionContainer VBox containing the UI elements.
     * @param questionList      ScrollPane containing the whole list of questions.
     */
    public static void removeUpvoteQuestion(String roomID, int questionId, VBox questionContainer, ScrollPane questionList) {
        ServerCommunication.removeUpvoteQuestion(roomID, questionId);
        LocalData.upvotedQuestions.remove(questionId);
        retrieveAllQuestions(roomID, questionContainer, questionList);
    }

    /**
     * Deletes a question and refreshes the question list.
     * @param roomID            ID of the room question is in.
     * @param questionId        ID of the specified question.
     * @param questionContainer VBox containing the UI elements.
     * @param questionList      ScrollPane containing the whole list of questions.
     */
    public static void deleteQuestionStudent(String roomID, int questionId, VBox questionContainer, ScrollPane questionList) {
        ServerCommunication.deleteQuestion(roomID, questionId);
        StudentViewLogic.retrieveAllQuestions(roomID, questionContainer, questionList);
    }
}
