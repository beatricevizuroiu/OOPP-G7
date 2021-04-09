package nl.tudelft.oopp.g7.client.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import nl.tudelft.oopp.g7.client.logic.LocalData;

import java.io.IOException;

public class EntryRoomDisplay extends Application {

    private static Stage currentStage;
    private static Scene currentScene;
    @Getter
    private static boolean isDarkMode;

    @Override
    public void start(Stage primaryStage) throws IOException{
        currentStage = primaryStage;

        setCurrentScene("/entryPage.fxml");
    }

    /**
     * Get current scene scene.
     *
     * @return the scene
     */
    public static Scene getCurrentScene(){
        return currentScene;
    }

    /**
     * Set current scene.
     *
     * @param newSceneName the new scene name
     */
    public static void setCurrentScene(String newSceneName){
        try{
            Parent parent = FXMLLoader.load(EntryRoomDisplay.class.getResource(newSceneName));
            currentScene = new Scene(parent);

            currentStage.setScene(currentScene);

            currentScene.getStylesheets().add(EntryRoomDisplay.class.getResource("/fonts/fonts.css").toString());

            if (LocalData.getRoomName() != null && !"".equals(LocalData.getRoomName())) {
                currentStage.setTitle("RaisedHand: " + LocalData.getRoomName());
            } else {
                currentStage.setTitle("RaisedHand");
            }

            currentStage.show();

            isDarkMode = newSceneName.contains("DARKMODE");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get current stage stage.
     *
     * @return the stage
     */
    public static Stage getCurrentStage(){
        return currentStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
