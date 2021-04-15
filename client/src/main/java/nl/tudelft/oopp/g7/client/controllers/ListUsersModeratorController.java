package nl.tudelft.oopp.g7.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.g7.client.Views;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.SharedLogic;


public class ListUsersModeratorController {
    @FXML
    private VBox listUsersContainer;
    @FXML
    private Text courseName;

    /**
     * Special FXML method that will run as soon as listUsersContainer is populated.
     */
    @FXML
    public void initialize() {
        SharedLogic.displayCourseName(courseName);

        String component = "ModeratorUserInfoContainer.fxml";
        String componentDark = "ModeratorUserInfoContainer(DARKMODE).fxml";
        SharedLogic.retrieveAllUsers(LocalData.getRoomID(), listUsersContainer, component, componentDark);
    }

    /**
     * Handle button action for going back to lecturer view (light).
     */
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
