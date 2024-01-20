package test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class XGrid extends Application {



    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage stage){
        int length = 6;
        GridPane root = new GridPane();
        root.setPadding(new Insets(20));

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                Pane pane = new Pane();
                pane.setPrefSize(50.0,50.0);
                Text txt = new Text(""+ Integer.valueOf(length * i + j));
//                Text txt = new Text(""+i+","+j);
                pane.getChildren().add(txt);
                root.add(pane,j,i);
            }
        }



        Scene scene = new Scene(root,300,300);
        stage.setScene(scene);
        stage.show();
    }
}
