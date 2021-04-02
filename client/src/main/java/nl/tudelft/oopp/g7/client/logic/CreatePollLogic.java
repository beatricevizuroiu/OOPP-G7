package nl.tudelft.oopp.g7.client.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

public class CreatePollLogic {

    public static void nextOption(List<String> pollOptions, OptionsPosition optionsPosition, VBox optionContainer, TextArea optionTextBox) {

        List<Node> optionList = optionContainer.getChildren();

        try {
            String componentName = EntryRoomDisplay.isDarkMode() ? "/components/PollOptionExample(DARKMODE).fxml" : "/components/PollOptionExample.fxml";
            HBox optionNode = FXMLLoader.load(CreatePollLogic.class.getResource(componentName));

            Text pollOptionText = (Text) optionNode.lookup("#PollOptionLabel");
            pollOptionText.setText(optionTextBox.getText());

            if (!(optionsPosition.getPosition() >= optionList.size())) {
                optionList.remove(optionsPosition.getPosition());
            }

            optionList.add(optionsPosition.getPosition(), optionNode);

            if (optionsPosition.getPosition() >= pollOptions.size()) {
                pollOptions.add(optionTextBox.getText());
            } else {
                pollOptions.set(optionsPosition.getPosition(), optionTextBox.getText());
            }

        } catch (IOException ignored) {
            System.err.println("A problem occurred");
        }

        optionsPosition.increment();
        if (optionsPosition.getPosition() < pollOptions.size()) {
            optionTextBox.setText(pollOptions.get(optionsPosition.getPosition()));
        } else {
            optionTextBox.setText("");
        }
    }

    public static void previousOption(List<String> pollOptions, OptionsPosition optionsPosition, VBox optionContainer, TextArea optionTextBox) {

        if(optionsPosition.getPosition() == 0) {
            return;
        }

        if (optionsPosition.getPosition() == pollOptions.size()) {
            nextOption(pollOptions, optionsPosition, optionContainer, optionTextBox);
        }

        List<Node> optionList = optionContainer.getChildren();

        if (optionTextBox.getText() != "")
        try {
            String componentName = EntryRoomDisplay.isDarkMode() ? "/components/PollOptionExample(DARKMODE).fxml" : "/components/PollOptionExample.fxml";
            HBox optionNode = FXMLLoader.load(CreatePollLogic.class.getResource(componentName));

            if (optionsPosition.getPosition() == pollOptions.size()) {
                pollOptions.add(optionTextBox.getText());
            } else {
                pollOptions.set(optionsPosition.getPosition(), optionTextBox.getText());
            }
            Text pollOptionText = (Text) optionNode.lookup("#PollOptionLabel");
            pollOptionText.setText(optionTextBox.getText());

            optionList.remove(optionsPosition.getPosition());
            optionList.add(optionsPosition.getPosition(), optionNode);

        } catch (IOException ignored) {
            System.err.println("A problem occurred");
        }

        optionsPosition.decrement();
        optionTextBox.setText(pollOptions.get(optionsPosition.getPosition()));
    }

    public static void deleteLast(List<String> pollOptions, OptionsPosition optionsPosition, VBox optionContainer) {

        while (optionsPosition.getPosition() >= (pollOptions.size())) {
            optionsPosition.decrement();
        }
        List<Node> optionList = optionContainer.getChildren();
        optionList.remove(optionList.size() - 1);
        pollOptions.remove(pollOptions.size() - 1);


    }

    public static boolean setResultPublicity(boolean publicResults, Button publicResultsButton) {
        publicResults = !publicResults;
        if(publicResults) {
            publicResultsButton.setText("Results: Visible");
            return true;
        }
        publicResultsButton.setText("Results: Invisible");
        return false;
    }

    public static void sendPoll(List<String> pollOptions, TextArea questionArea, boolean publicResults) {
        ModeratorServerCommunication.createPoll(LocalData.getRoomID(), questionArea.getText(), pollOptions.toArray(new String[0]), publicResults);
    }
}
