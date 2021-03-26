package nl.tudelft.oopp.g7.client.logic;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.g7.client.communication.LocalData;
import nl.tudelft.oopp.g7.common.Room;

public class JoinRoomLogic {
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
}
