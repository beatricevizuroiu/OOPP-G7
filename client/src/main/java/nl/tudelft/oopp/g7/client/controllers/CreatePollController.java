package nl.tudelft.oopp.g7.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.Views;
import nl.tudelft.oopp.g7.client.logic.CreatePollLogic;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;
import nl.tudelft.oopp.g7.common.OptionsPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreatePollController {
    @FXML
    private TextArea questionArea;
    @FXML
    private Text courseName;
    @FXML
    private TextArea optionArea;
    @FXML
    public VBox optionContainer;
    @FXML
    public Button publicResultsButton;
    public List<String> pollOptions;

    public OptionsPosition optionsPosition;
    public AtomicBoolean publicResults = new AtomicBoolean(true);

    /**
     * Construct CreatePollController.
     */
    public CreatePollController() {
        pollOptions = new ArrayList<>();
        optionsPosition = new OptionsPosition();

        CreatePollLogic.reset();
    }

    public void initialize() {
        SharedLogic.displayCourseName(courseName);
    }

    /**
     * Send the Poll that has been made.
     */

    public void sendPoll() {
        CreatePollLogic.sendPoll(pollOptions, questionArea, publicResults);
    }

    /**
     * Toggle whether students can see the results of a Poll while it is active.
     */
    public void setResultPublicity() {
        CreatePollLogic.setResultPublicity(publicResults, publicResultsButton);
    }

    /**
     * Go to the previous window.
     */
    public void back() {
        Views.goBack();
    }

    /**
     * Add an option to the current Poll.
     */
    public void addOption() {
        CreatePollLogic.addOption(pollOptions, optionArea, optionContainer);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     */
    public void presetABC() {
        pollOptions.clear();
        pollOptions.add("A");
        pollOptions.add("B");
        pollOptions.add("C");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     */
    public void presetABCD() {
        pollOptions.clear();
        pollOptions.add("A");
        pollOptions.add("B");
        pollOptions.add("C");
        pollOptions.add("D");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     */
    public void presetABCDE() {
        pollOptions.clear();
        pollOptions.add("A");
        pollOptions.add("B");
        pollOptions.add("C");
        pollOptions.add("D");
        pollOptions.add("E");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     */
    public void presetABCDEF() {
        pollOptions.clear();
        pollOptions.add("A");
        pollOptions.add("B");
        pollOptions.add("C");
        pollOptions.add("D");
        pollOptions.add("E");
        pollOptions.add("F");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     */
    public void presetYesNo() {
        pollOptions.clear();
        pollOptions.add("Yes");
        pollOptions.add("No");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    /**
     * Use the preset as Poll options for the current Poll.
     */
    public void presetTrueFalse() {
        pollOptions.clear();
        pollOptions.add("True");
        pollOptions.add("False");
        CreatePollLogic.reDrawOptionList(optionContainer, pollOptions, optionArea);
    }

    public void goBack() {
        Views.goBack();
    }

    public void changeMode() {
        LocalData.switchColorScheme();
    }

    public void goToHelpPage() {
        Views.navigateTo(Views.HELP);
    }
}
