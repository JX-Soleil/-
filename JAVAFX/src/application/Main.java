package application;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
        	
        	System.out.println("enter");
            // Read file fxml and draw interface.
            Parent root = FXMLLoader.load(getClass()
                    .getResource("MyScene.fxml"));
            Scene scene = new Scene(root);
            //¼ÓÔØcssÎÄ¼þ
            scene.getStylesheets().add("application/application.css");
            primaryStage.setTitle("My Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}