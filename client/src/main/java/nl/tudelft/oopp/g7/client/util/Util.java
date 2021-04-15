package nl.tudelft.oopp.g7.client.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Util {

    public static Alert createAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert;
    }

    public static boolean getConfirmation(String title, String headerText, String contentText) {
        // show confirmation type pop-up with what you entered as text
        Alert alert = Util.createAlert(Alert.AlertType.CONFIRMATION, title, headerText, contentText);

        // set types of buttons for the pop-up
        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(okButton, cancelButton);

        // wait for the alert to appear
        alert.showAndWait();

        // return whether the user pressed OK or not
        return alert.getResult() == okButton;
    }
}
