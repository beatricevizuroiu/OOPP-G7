package nl.tudelft.oopp.g7.client.logic;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.g7.common.Room;
import nl.tudelft.oopp.g7.common.SortingOrder;

public class CreateRoomLogic {
    /**
     * Create a pop-up for confirming creation of a room.
     * @param lecturerName TextField in which lecturer's name is written
     * @param roomName TextField in which room's name is written
     * @return a boolean confirming whether the user pressed OK or cancel
     */
    public static boolean createRoomConfirmation(TextField lecturerName, TextField roomName) {
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

        // return whether the user pressed OK or not
        return alert.getResult() == okButton;
    }

    /**
     * Stores the room/user information into a local static class.
     * @param lecturerName TextField in which lecturer's name is written
     * @param room object containing information about the newly created room
     */
    public static void createRoomStoreLocalData(TextField lecturerName, Room room) {
        LocalData.setNickname(lecturerName.getText());
        LocalData.setRoomID(room.getId());
        LocalData.setStudentPassword(room.getStudentPassword());
        LocalData.setModeratorPassword(room.getModeratorPassword());
        LocalData.setSortingOrder(SortingOrder.UPVOTES);
        LocalData.setLecturer(true);
    }
}
