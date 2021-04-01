package nl.tudelft.oopp.g7.client.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class StudentViewLogic {
    private static HashMap<Integer, Integer> selectedPollOptions = new HashMap<>();

    public static void retrieveServerData(String roomID, VBox questionContainer, ScrollPane questionList, VBox pollWindow) {
        retrieveAllQuestions(roomID, questionContainer, questionList);
        retrievePolls(roomID, pollWindow);
    }

    public static void retrievePolls(String roomId, VBox pollWindowContainer) {
        PollInfo poll = ServerCommunication.getPoll(roomId);
        if (poll == null) {
            return;
        }
        try {
            String pollWindowComponentName = EntryRoomDisplay.isDarkMode() ? "/components/PollWindow(DARKMODE).fxml" : "/components/PollWindow.fxml";
            HBox pollWindow = FXMLLoader.load(StudentViewLogic.class.getResource(pollWindowComponentName));

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
                    StudentServerCommunication.answerPoll(roomId, pollOption.getId());
                    selectedPollOptions.put(poll.getId(), pollOption.getId());
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
     * @param roomID ID of the room questions are in
     * @param questionContainer VBox containing the UI elements.
     * @param questionList ScrollPane containing the whole list of questions.
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
                HBox questionNode = FXMLLoader.load(StudentViewLogic.class.getResource(componentName));

                Button upvoteBtn = (Button) questionNode.lookup("#QuestionUpvoteBtn");
                Text upvoteCount = (Text) questionNode.lookup("#QuestionUpvoteCount");
                Text body = (Text) questionNode.lookup("#QuestionText");
                Text authorText = (Text) questionNode.lookup("#QuestionAuthor");

                // add event listeners for upvoting buttons
                upvoteBtn.setOnAction((event) -> upvoteQuestion(roomID, question.getId(), questionContainer, questionList));

                // cap the number of upvotes at 999
                upvoteCount.setText(Integer.toString(Math.min(question.getUpvotes(), 999)));
                body.setText(question.getText());

                if (!LocalData.userMap.containsKey(question.getAuthorId())) {
                    LocalData.userMap.put(question.getAuthorId(), ServerCommunication.retrieveUserById(roomID, question.getAuthorId()));
                }

                authorText.setText(LocalData.userMap.get(question.getAuthorId()).getNickname() + " asks");

                questionNodes.add(questionNode);
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
}
