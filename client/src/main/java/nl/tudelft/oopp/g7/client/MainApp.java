package nl.tudelft.oopp.g7.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.logic.LocalData;

import java.io.IOException;
import java.util.List;

public class MainApp extends Application {
    private static Stage currentStage;
    private static Scene currentScene;

    /**
     * Entry point.
     * @param args Program arguments.
     */
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        currentStage = primaryStage;

        setCurrentScene("/views/entryPage.fxml");
    }

    public static void updateStyleSheets() {
        List<String> stylesheets = currentScene.getStylesheets();

        stylesheets.clear();

        stylesheets.add(MainApp.class.getResource("/fonts/fonts.css").toString());
        stylesheets.add(MainApp.class.getResource(LocalData.getColorScheme().getStylesheet()).toString());
    }

    /**
     * Set current scene.
     * @param newSceneName the new scene name
     */
    public static void setCurrentScene(String newSceneName) {
        try {
            Parent parent = FXMLLoader.load(MainApp.class.getResource(newSceneName));
            currentScene = new Scene(parent);

            currentStage.setScene(currentScene);

            updateStyleSheets();

            if (LocalData.getRoomName() != null && !"".equals(LocalData.getRoomName())) {
                currentStage.setTitle("RaisedHand: " + LocalData.getRoomName());
            } else {
                currentStage.setTitle("RaisedHand");
            }

            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
