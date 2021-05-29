package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Working Directory = C:\Users\nunou\IdeaProjects\geocaching
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("geocache.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);

        primaryStage.setTitle("Graph Creator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
