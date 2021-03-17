package nl.tudelft.oopp.g7.client.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class EntryRoomDisplay extends Application {

    private static Stage currentStage;
    private static Scene currentScene;

    @Override
    public void start(Stage primaryStage) throws IOException{
        currentStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();

        URL xmlUrl = getClass().getResource("/entryPage.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        currentScene = new Scene(root);

        currentStage.setScene(currentScene);
        currentStage.show();
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
            Scene newScene = new Scene(parent);

            currentStage.setScene((newScene));
            EntryRoomDisplay.currentScene = newScene;

            currentStage.show();
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
