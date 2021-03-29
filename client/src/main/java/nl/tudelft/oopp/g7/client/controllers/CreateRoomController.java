package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.communication.RoomServerCommunication;
import nl.tudelft.oopp.g7.client.logic.CreateRoomLogic;
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

        // pop-up an alert to confirm creating the room
        boolean isConfirmed = CreateRoomLogic.createRoomConfirmation(lecturerName, roomName);

        // if the user presses OK, the go to Lecturer View
        if (isConfirmed){

            // Put all information for a new room in a NewRoom object and send it to the server to have it create the room
            NewRoom newRoom = new NewRoom(roomName.getText(), studentPassword.getText(), moderatorPassword.getText(), new Date());
            Room room = RoomServerCommunication.createRoom(newRoom);

            // Store all relevant room information for future reference
            CreateRoomLogic.createRoomStoreLocalData(lecturerName, room);

            // Proceed to Lecturer View
            Scene scene = EntryRoomDisplay.getCurrentScene();
            Stage stage = EntryRoomDisplay.getCurrentStage();

            EntryRoomDisplay.setCurrentScene("/LecturerViewUI.fxml");
        }
    }

    /**
     * Handles clicking the button Create from Dark.
     */
    public void buttonClicked2() {
        // pop-up an alert to confirm creating the room
        boolean isConfirmed = CreateRoomLogic.createRoomConfirmation(lecturerName, roomName);

        // if the user presses OK, the go to Lecturer View
        if (isConfirmed){

            // Put all information for a new room in a NewRoom object and send it to the server to have it create the room
            NewRoom newRoom = new NewRoom(roomName.getText(), studentPassword.getText(), moderatorPassword.getText(), new Date());
            Room room = RoomServerCommunication.createRoom(newRoom);

            // Store all relevant room information for future reference
            CreateRoomLogic.createRoomStoreLocalData(lecturerName, room);

            // Proceed to Lecturer View
            Scene scene = EntryRoomDisplay.getCurrentScene();
            Stage stage = EntryRoomDisplay.getCurrentStage();

            EntryRoomDisplay.setCurrentScene("/LecturerViewUI(DARKMODE).fxml");
        }
    }
}
