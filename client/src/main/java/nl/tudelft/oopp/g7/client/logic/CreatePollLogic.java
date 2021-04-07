package nl.tudelft.oopp.g7.client.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.communication.ModeratorServerCommunication;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.OptionsPosition;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreatePollLogic {
    private static String editing = "";

    public static void setResultPublicity(AtomicBoolean publicResults, Button publicResultsButton) {
        publicResults.set(!publicResults.get());

        if(publicResults.get()) {
            publicResultsButton.setText("Results: Visible");
            return;
        }

        publicResultsButton.setText("Results: Invisible");
    }

    public static void reset() {
        editing = "";
    }

    public static void sendPoll(List<String> pollOptions, TextArea questionArea, AtomicBoolean publicResults) {
        String question = questionArea.getText();
        if ("".equals(question)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Enter question");
            alert.setHeaderText("Enter question");
            alert.setContentText("Please enter a question to create a poll.");

            alert.showAndWait();
            return;
        }

        ModeratorServerCommunication.createPoll(LocalData.getRoomID(), question, pollOptions.toArray(new String[0]), publicResults.get());

        EntryRoomDisplay.setCurrentScene(EntryRoomDisplay.isDarkMode() ? "/TAViewUI(DARKMODE).fxml" : "/TAViewUI.fxml");
    }

    public static void addOption(List<String> pollOptions, TextArea optionArea, VBox optionContainer) {
        String text = optionArea.getText();

        if (text == null || "".equals(text))
            return;

        if (pollOptions.contains(text) && !editing.equals(text))
            return;

        optionArea.clear();

        if (!"".equals(editing)) {
            int index = pollOptions.indexOf(editing);
            pollOptions.set(index, text);
            editing = "";
        } else {
            pollOptions.add(text);
        }

        reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    private static void deleteOption(String option, List<String> pollOptions, VBox optionContainer, TextArea optionArea) {
        pollOptions.remove(option);

        if (editing.equals(option))
            editing = "";

        reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    private static void editOption(String option, VBox optionContainer, List<String> pollOptions, TextArea optionArea) {
        editing = option;
        optionArea.setText(option);
        reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    public static void reDrawOptionList(VBox optionContainer, List<String> pollOptions, TextArea optionArea) {
        List<Node> optionList = optionContainer.getChildren();

        optionList.clear();

        try {
            for (String option : pollOptions) {
                String optionText = option.substring(0, Math.min(option.length(), 20));

                String componentName = EntryRoomDisplay.isDarkMode() ? "/components/PollOptionExample(DARKMODE).fxml" : "/components/PollOptionExample.fxml";
                HBox optionNode = FXMLLoader.load(CreatePollLogic.class.getResource(componentName));

                Text pollOptionText = (Text) optionNode.lookup("#PollOptionLabel");
                pollOptionText.setText(optionText);

                Button pollOptionDeleteBtn = (Button) optionNode.lookup("#DeleteButton");
                pollOptionDeleteBtn.setOnAction((event) -> deleteOption(option, pollOptions, optionContainer, optionArea));

                optionNode.setOnMouseClicked((event) -> editOption(option, optionContainer, pollOptions, optionArea));

                if (option.equals(editing)) {
                    optionNode.getStyleClass().add("selected");
                }

                optionNode.setPrefWidth(328);

                optionList.add(optionNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
