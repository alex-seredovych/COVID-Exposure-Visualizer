package finalproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FinalProject extends Application {
    @Override
    public void start(Stage primaryStage) {
        
        /* Create a new Visual Graph, and
         *  fill it with data from "tinyfreinds.txt"
         */
        VisualGraph<String> g = new VisualGraph<>();
        g.createFromFile("src\\finalproject\\tinyfriends.txt");
        
        
        /* Create a new scene, add the Visual Graph display, 
         *   and show the window
         */
        StackPane root = new StackPane();
        root.getChildren().add(g.display);
        Scene scene = new Scene(root, 1200, 900);
        
        primaryStage.setTitle("Final Project");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        /* Launch the JavaFX Application */
        launch(args);
    }
    
}
