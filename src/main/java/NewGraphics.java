/**
 * Created by Meyttt on 13.06.2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NewGraphics extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NewGraphics.class.getResource("NewGraphics.fxml"));
        AnchorPane NewProject = loader.load();
        Scene scene = new Scene(NewProject);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ObserverClient");
        primaryStage.show();
    }
}
