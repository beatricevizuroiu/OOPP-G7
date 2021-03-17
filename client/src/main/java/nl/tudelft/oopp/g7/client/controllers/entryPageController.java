package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;

import java.net.URL;
import java.util.ResourceBundle;

public class entryPageController {
    @FXML
    private Button createRoomButton;
    @FXML
    private Label label;
//    @FXML
//    private Button joinRoomButton;

    public void handleButtonAction(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        EntryRoomDisplay.setCurrentScene("/createRoom.fxml");
    }

    public void handleButtonAction2(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        EntryRoomDisplay.setCurrentScene("/joinRoom.fxml");
    }
}
