package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.communication.RoomServerCommunication;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.NewRoom;
import nl.tudelft.oopp.g7.common.Room;

import java.util.Date;

public class CreateRoomController {
    @FXML
    TextField roomName;
    @FXML
    TextField lecturerName;
    @FXML
    TextField studentPassword;
    @FXML
    TextField moderatorPassword;

    /**
     * Handle button action for button Mode from Light.
     *
     * @param event the event
     */
    public void handleButtonMode(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/createRoom(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Mode from Dark.
     *
     * @param event the event
     */
    public void handleButtonMode2(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/createRoom.fxml");
    }

    /**
     * Handles clicking the button Create from Light.
     */
    public void buttonClicked() {

        // show confirmation type pop-up with what you entered as text
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // title of pop-up
        alert.setTitle("Create Room");
        alert.setHeaderText(null);

        // body of pop-up with what the user entered
        alert.setContentText("You are creating the room for Course: " + roomName.getText()
                + " held by lecturer: "
                + lecturerName.getText()
                + ".");

        // set types of buttons for the pop-up
        ButtonType okButton = new ButtonType ("OK");
        ButtonType cancelButton = new ButtonType ("Cancel");

        alert.getButtonTypes().setAll(okButton, cancelButton);

        // wait for the alert to appear
        alert.showAndWait();

        // if the user presses OK, the go to Lecturer View
        if (alert.getResult() == okButton){

            // Put all information for a new room in a NewRoom object and send it to the server to have it create the room
            NewRoom newRoom = new NewRoom(roomName.getText(), studentPassword.getText(), moderatorPassword.getText(), new Date());
            Room room = RoomServerCommunication.createRoom(newRoom);

            // Store all relevant room information for future reference
            LocalData.setNickname(lecturerName.getText());
            LocalData.setRoomID(room.getId());
            LocalData.setStudentPassword(room.getStudentPassword());
            LocalData.setModeratorPassword(room.getModeratorPassword());

            if (true /* TODO: Check if the lecture is not scheduled */ ) {
                RoomServerCommunication.joinRoom(room.getId(), room.getModeratorPassword(), lecturerName.getText());
                
                // Proceed to Lecturer View
                Scene scene = EntryRoomDisplay.getCurrentScene();
                Stage stage = EntryRoomDisplay.getCurrentStage();

                EntryRoomDisplay.setCurrentScene("/LecturerViewUI.fxml");
            } else {
                // TODO: Implement scheduled lecture UI
            }
        }
    }

    /**
     * Handles clicking the button Create from Dark.
     */
    public void buttonClicked2() {

        // show confirmation type pop-up with what you entered as text
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // title of pop-up
        alert.setTitle("Create Room");
        alert.setHeaderText(null);

        // body of pop-up with what the user entered
        alert.setContentText("You are creating the room for Course: " + roomName.getText()
                + " held by lecturer: "
                + lecturerName.getText()
                + ".");

        // set types of buttons for the pop-up
        ButtonType okButton = new ButtonType ("OK");
        ButtonType cancelButton = new ButtonType ("Cancel");

        alert.getButtonTypes().setAll(okButton, cancelButton);

        // wait for the alert to appear
        alert.showAndWait();

        // if the user presses OK, the go to Lecturer View
        if (alert.getResult() == okButton){

            // Put all information for a new room in a NewRoom object and send it to the server to have it create the room
            NewRoom newRoom = new NewRoom(roomName.getText(), studentPassword.getText(), moderatorPassword.getText(), new Date());
            Room room = RoomServerCommunication.createRoom(newRoom);

            // Store all relevant room information for future reference
            LocalData.setNickname(lecturerName.getText());
            LocalData.setRoomID(room.getId());
            LocalData.setStudentPassword(room.getStudentPassword());
            LocalData.setModeratorPassword(room.getModeratorPassword());

            if (true /* TODO: Check if the lecture is not scheduled */ ) {
                RoomServerCommunication.joinRoom(room.getId(), room.getModeratorPassword(), lecturerName.getText());

                // Proceed to Lecturer View
                Scene scene = EntryRoomDisplay.getCurrentScene();
                Stage stage = EntryRoomDisplay.getCurrentStage();

                EntryRoomDisplay.setCurrentScene("/LecturerViewUI.fxml");
            } else {
                // TODO: Implement scheduled lecture UI
            }
        }
    }
}
