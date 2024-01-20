package test;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.stage.Stage;

import javafx.application.Application;

public class TextTest extends Application{
    protected static Button b1;
    protected static Text txt;
    protected static Button b2;


    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage stage){
        HBox root = new HBox();

        Button b1 = new Button("yes");
        Button b2 = new Button("no");
        b1.addEventHandler(ActionEvent.ANY,new Control());
        b2.addEventHandler(ActionEvent.ANY,new Control());
        txt = new Text("null");

        root.getChildren().addAll(b1,b2,txt);


        Scene scene = new Scene(root,300,300);
        stage.setScene(scene);
        stage.show();
    }
}

class Control implements EventHandler<ActionEvent> {
    protected static Button b1 = TextTest.b1;
    protected static Button b2 = TextTest.b2;
    protected static Text txt = TextTest.txt;

    @Override
    public void handle(ActionEvent actionEvent) {
        if(actionEvent.getSource() == b1){
            txt.setText("yes");
        }
        else if(actionEvent.getSource() == b2){
            txt.setText("no");
        }
    }
}
