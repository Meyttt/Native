import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by Meyttt on 27.04.2017.
 */
public class HandlerTest extends Application {
    public void start(){
        launch(HandlerTest.class);
    }
    public void start(Stage primaryStage) throws Exception {
        Button button = new Button("Далее");
//        button.
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.addColumn(0,button,textArea);
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Handler test...");
        primaryStage.show();
    }

    public static void main(String[] args) {
        HandlerTest handlerTest = new HandlerTest();
        handlerTest.start();
    }
}
