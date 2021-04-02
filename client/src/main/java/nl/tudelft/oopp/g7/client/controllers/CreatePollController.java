package nl.tudelft.oopp.g7.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.logic.CreatePollLogic;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.OptionsPosition;

import java.util.ArrayList;
import java.util.List;

public class CreatePollController {
    @FXML
    private TextArea questionArea;
    @FXML
    private TextArea optionArea;
    @FXML
    public VBox optionContainer;
    @FXML
    public Button publicResultsButton;
    public List<String> pollOptions;

    public OptionsPosition optionsPosition;
    public boolean publicResults = true;

    public CreatePollController() {
        pollOptions = new ArrayList<>();
        optionsPosition = new OptionsPosition();
    }

    public void nextOption() {
        CreatePollLogic.nextOption(pollOptions, optionsPosition, optionContainer, optionArea);
    }

    public void previousOption() {
        CreatePollLogic.previousOption(pollOptions, optionsPosition, optionContainer, optionArea);
    }

    public void sendPoll() {
        CreatePollLogic.sendPoll(pollOptions, questionArea, publicResults);
    }

    public void deleteLast() {
        CreatePollLogic.deleteLast(pollOptions, optionsPosition, optionContainer);
    }

    public void setResultPublicity() {
        publicResults = CreatePollLogic.setResultPublicity(publicResults, publicResultsButton);
    }

    public void back() {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        //TODO: Uncomment when variable gets merged
        String targetFile = /*LocalData.isLecturer ? "/LecturerViewUI.fxml" : */ "/TAViewUI.fxml";
        EntryRoomDisplay.setCurrentScene(targetFile);
    }

}
