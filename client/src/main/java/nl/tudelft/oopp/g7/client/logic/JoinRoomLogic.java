package nl.tudelft.oopp.g7.client.logic;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.g7.client.communication.LocalData;
import nl.tudelft.oopp.g7.common.Room;
import org.w3c.dom.Text;

public class JoinRoomLogic {
    /**
     * Create pop-up confirming joining a room.
     * @param nickname TextField in which user's name is written
     * @param roomId TextField in which id of the room user wants to join is written
     * @return a boolean confirming whether the user pressed OK or cancel
     */
    public static boolean joinRoomConfirmation(TextField nickname, TextField roomId) {
        // show confirmation type pop-up with what you entered as text
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // title of pop-up
        alert.setTitle("Join Room");
        alert.setHeaderText(null);

        // body of pop-up with what the user entered
        if(nickname.getText() == null || nickname.getText().equals("")){
            alert.setContentText("You are joining room: " + roomId.getText());
        }else {
            alert.setContentText("You are joining room: " + roomId.getText() + " as: " + nickname.getText());
        }

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
     * @param nickname TextField in which user's name is written
     * @param roomId TextField in which id of the room user wants to join is written
     * @param roomPassword TextField in which the appropriate password (student/moderator) is written
     */
    public static void joinRoomStoreLocalData(TextField nickname, TextField roomId, TextField roomPassword) {
        LocalData.setNickname(nickname.getText());
        LocalData.setRoomID(roomId.getText());
        LocalData.setPassword(roomPassword.getText());
    }
}
