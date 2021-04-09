package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.communication.RoomServerCommunication;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.CreateRoomLogic;
import nl.tudelft.oopp.g7.client.logic.ModeratorViewLogic;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.NewRoom;
import nl.tudelft.oopp.g7.common.Room;
import nl.tudelft.oopp.g7.common.RoomJoinInfo;

import java.time.LocalDate;
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
    @FXML
    DatePicker startDate;
    @FXML
    TextField startTime;
    @FXML
    CheckBox scheduleCheckbox;

    /**
     * Create the current date.
     */
    @FXML
    public void initialize() {
        Date currentDate = new Date();
        startDate.setValue(LocalDate.now());
        startTime.setText(currentDate.getHours() + ":" + currentDate.getMinutes());
    }

    /**
     * Handle button action for button Mode from Light.
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
     * @param event the event.
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
        // if the user presses OK, the go to Lecturer View
        if (CreateRoomLogic.createRoomConfirmation(lecturerName, roomName)) {

            Date startDateTime = CreateRoomLogic.getTimeAndDateFromUI(startDate, startTime);

            if (scheduleCheckbox.isSelected()
                    && startDateTime == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid date/time");
                alert.setContentText("The date and or time you have entered are invalid.");

                alert.showAndWait();

                return;
            }

            if (startDateTime == null)
                startDateTime = new Date();

            // Put all information for a new room in a NewRoom object and send it to the server to have it create the room
            NewRoom newRoom = new NewRoom(roomName.getText(), studentPassword.getText(), moderatorPassword.getText(), startDateTime);
            Room room = RoomServerCommunication.createRoom(newRoom);
            if (room == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not create room.");
                alert.setContentText("Could not create room.");

                alert.showAndWait();
                return;
            }

            // Store all relevant room information for future reference
            CreateRoomLogic.createRoomStoreLocalData(lecturerName, room);

            if (scheduleCheckbox.isSelected()) {
                ModeratorViewLogic.displayLinkAndPasswords();
            } else {
                RoomJoinInfo roomJoinInfo = RoomServerCommunication.joinRoom(room.getId(), room.getModeratorPassword(), lecturerName.getText());

                if (roomJoinInfo == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Could not join room.");
                    alert.setContentText("Could not join room.");

                    alert.showAndWait();
                    return;
                }

                LocalData.setUserID(roomJoinInfo.getUserId());
                LocalData.setToken(roomJoinInfo.getAuthorization());

                // Proceed to Lecturer View
                Scene scene = EntryRoomDisplay.getCurrentScene();
                Stage stage = EntryRoomDisplay.getCurrentStage();

                EntryRoomDisplay.setCurrentScene("/LecturerViewUI.fxml");
            }
        }
    }

    /**
     * Handles clicking the button Create from Dark.
     */
    public void buttonClicked2() {

        // if the user presses OK, the go to Lecturer View
        if (CreateRoomLogic.createRoomConfirmation(lecturerName, roomName)) {

            Date startDateTime = CreateRoomLogic.getTimeAndDateFromUI(startDate, startTime);

            if (scheduleCheckbox.isSelected()
                    && startDateTime == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid date/time");
                alert.setContentText("The date and or time you have entered are invalid.");

                alert.showAndWait();

                return;
            }

            if (!scheduleCheckbox.isSelected())
                startDateTime = new Date();


            // Put all information for a new room in a NewRoom object and send it to the server to have it create the room
            NewRoom newRoom = new NewRoom(roomName.getText(), studentPassword.getText(), moderatorPassword.getText(), startDateTime);
            Room room = RoomServerCommunication.createRoom(newRoom);
            if (room == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not create room.");
                alert.setContentText("Could not create room.");

                alert.showAndWait();
                return;
            }

            // Store all relevant room information for future reference
            CreateRoomLogic.createRoomStoreLocalData(lecturerName, room);

            if (scheduleCheckbox.isSelected()) {
                ModeratorViewLogic.displayLinkAndPasswords();
            } else {
                RoomJoinInfo roomJoinInfo = RoomServerCommunication.joinRoom(room.getId(), room.getModeratorPassword(), lecturerName.getText());

                if (roomJoinInfo == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Could not join room.");
                    alert.setContentText("Could not join room.");

                    alert.showAndWait();
                    return;
                }

                LocalData.setUserID(roomJoinInfo.getUserId());
                LocalData.setToken(roomJoinInfo.getAuthorization());

                // Proceed to Lecturer View
                Scene scene = EntryRoomDisplay.getCurrentScene();
                Stage stage = EntryRoomDisplay.getCurrentStage();

                EntryRoomDisplay.setCurrentScene("/LecturerViewUI.fxml");
            }
        }
    }

    /**
     * Handle button action for going back to Entry page from Light.
     * @param event the event
     */
    public void handleBackButton(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/entryPage.fxml");
    }

    /**
     * Handle button action for going back to Entry page from Dark.
     * @param event the event
     */
    public void handleBackButtonDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if goBack is clicked, change Scene to LecturerViewUI
        EntryRoomDisplay.setCurrentScene("/entryPage(DARKMODE).fxml");
    }
}
