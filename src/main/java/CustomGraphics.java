import javafx.application.Application;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by Meyttt on 13.06.2017.
 */
public class CustomGraphics extends Application {
    public void start(Stage primaryStage) throws Exception {
        TextArea list = new TextArea("list");
        list.setPrefColumnCount(2);
//        list.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        Label label = new Label();
        label.setText("Date...");
//        label.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        Button sendNow = new Button("send");
        Button cancel = new Button("cancel");
        TextArea period = new TextArea("period");
        Button change = new Button("change");
        Button exit = new Button("exit");
//        exit.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        GridPane gridPane = new GridPane();
        gridPane.addColumn(0,list,label);
        gridPane.addColumn(1,sendNow,cancel, period,exit);
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
//        CustomGraphics customGraphics = new CustomGraphics();
        launch(CustomGraphics.class);
    }
}
