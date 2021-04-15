package nl.tudelft.oopp.g7.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.Views;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;

public class HelpFileController {

    @FXML
    private Text courseName;


    public void initialize() {
        SharedLogic.displayCourseName(courseName);
    }

    public void goBack() {
        Views.goBack();
    }

    public void changeMode() {
        LocalData.switchColorScheme();
    }
}
