/**
 * Created by Meyttt on 13.06.2017.
 */

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class NewGraphics extends Application {
    @FXML
    Label devList;
    @FXML
    ListView list;
    @FXML
    Button send;
    @FXML
    Button cancel;
    @FXML
    Button apply;
    @FXML
    TextField period;
    @FXML
    Button exit;
    @FXML
    Label update;
    NewGraphics newGraphics;
    AnchorPane NewProject;
    static ObservableList<String> results;
    public static void main(String[] args) {
        launch(args);
    }

    public ListView getList() {
        return list;
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NewGraphics.class.getResource("NewGraphics.fxml"));
        NewProject= loader.load();
        newGraphics=loader.getController();
        newGraphics.list.setItems(this.results);
        newGraphics.update.setText("Last update: \r\n"+new Date());

        Scene scene = new Scene(NewProject);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ObserverClient");
        primaryStage.show();


    }
    public void launch(ObservableList<String> results){
        this.results = results;
        launch(NewGraphics.class);
    }
}
