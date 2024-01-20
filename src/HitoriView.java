import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

/*
A class that draws the GUI components regarding design,elimination,
reactivation,highlighting, and win-detection.
 */
public class HitoriView extends Application{
    private static int[] hitoriMatrix;
    private static int sideLength;
    private static int seed;
    private static int wMax = 500;
    private static int hMax = 500;
    protected static Stage stage;
    private static GridPane grid;
    private static VBox root;
    private static Label infoLabel;

    public static void main(String[] args) {
        /*
        Initial:
            view asks model for Matrix sending sideLength & seed
            model sends back modified hitoriMatrix
            view makes initial gui
         */

        /*
        User clicks cell:
            Send Cell object to Controller
            Controller checks if cell is eliminated
                if so, cell.isEliminated=false and reactivate back cell
                if not, cell.isEliminated=true and eliminate cell
            Call MistakeChecker to "checkForMistake()"
            Cell(s) should get highlighted if mistake or playWinAnimation() if all 3 constraints are met.
         */

        launch(args);
    }

    public void start(Stage stage){
        this.stage = stage;
        seed = 1206;
        sideLength = 6;
        HitoriView.hitoriMatrix = HitoriModel.getInstance().startPuzzle(sideLength,seed);

        stage.setTitle("Hitori Puzzle");
        VBox root = draw();
        Scene scene = new Scene(root,wMax,hMax);
        restartGrid(hitoriMatrix,sideLength);
        scene.getStylesheets().add("graphics.css");
        stage.setMinWidth(400);
        stage.setMinHeight(400);
        stage.setMaxWidth(1000);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    private VBox draw(){
        root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        HBox settingsPane = new HBox();
        settingsPane.setSpacing(15);
        settingsPane.setAlignment(Pos.CENTER);
        Button fileButton = new Button("Click to Load File");
        Button resetButton = new Button("Click to Reset Game");
        resetButton.setOnAction(new Controller.ClickHandler("reset"));
        fileButton.setOnAction(new Controller.ClickHandler("load"));
        settingsPane.getChildren().addAll(fileButton, resetButton);

        grid = new GridPane();

        StackPane textLayoutPane = new StackPane();
        textLayoutPane.setAlignment(Pos.CENTER);
        infoLabel = new Label("Welcome to Hitori. Use Number Keys to edit the grid.\n"
                + "Left Click or Right Click to eliminate/reactivate."
        );
        infoLabel.setMinHeight(50);     //giving space to the label
        textLayoutPane.getChildren().add(infoLabel);

        root.getChildren().addAll(settingsPane,grid,textLayoutPane);
        return root;
    }

    static void restartGrid(int[] hitoriMatrix, int sideLength){
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        int index;
        for (int row = 0; row < sideLength; row++) {
            for (int col = 0; col < sideLength; col++) {
                index = sideLength*row+col;
                StackPane pane = new StackPane();
                pane.setPrefSize(300,300);
                pane.getStyleClass().add("pane");

                Label digit = new Label(""+hitoriMatrix[index]);
                digit.getStyleClass().add("digit");

                pane.getChildren().add(digit);
                pane.setAlignment(digit, Pos.CENTER);

                Cell cell = new Cell(pane,new int[]{row,col});
                pane.addEventHandler(MouseEvent.MOUSE_PRESSED,
                        new Controller.GridSelectHandler(cell));
                grid.add(pane, col, row);
                CellGrid.getCellArray()[index] = cell;
            }
        }
        root.getChildren().remove(1);
        root.getChildren().add(1, grid);
    }

    static void continueLabel(){
        infoLabel.setText("There is a duplicate number in a row/column.\n"
                + "Left Click or Right Click to eliminate/reactivate.\n"
                + "Keep Playing."
        );
    }

    static void eliminate(Pane pane){
        pane.setStyle("-fx-background-color: #000000"); //black
    }

    static void reactivate(Pane pane){
        pane.setStyle("-fx-background-color: #ffffff"); //white
    }

    /*
    Sends a confirmation Alert to reset the Hitori Puzzle.
    */
    static boolean canResetPuzzle(){
        Alert alertToClear = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to clear this Hitori Puzzle?");
        Optional<ButtonType> result = alertToClear.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            return true;
        }
        return false;
    }

    /*
    Changes 'infoLabel' at the bottom of the Hitori Puzzle.
    Also highlights the blacked-out cells & disjoint cells not fitting
    constraints 2 and 3
     */
    static void highlightMistake(Pane pane){
        pane.setStyle("-fx-background-color: #ff0000"); //red
        infoLabel.setText("There is a mistake at one or more cells.");
    }

    static void unhighlightMistake(Cell cell){
        cell.getPane().setStyle("-fx-background-color: #ffffff"); //white
        if(cell.isEliminated){
            HitoriView.eliminate(cell.getPane());
        }
        else {
            HitoriView.reactivate(cell.getPane());
        }
    }

    static void playWinAnimation(){
        for(Cell cell: CellGrid.getCellArray()){
            //disable all cell panes
            cell.getPane().setDisable(true);
        }

        for(Cell cell: CellGrid.getCellArray()){
            if(!cell.isEliminated)
                cell.getPane().setStyle("-fx-background-color: #00ff00"); //green
        }

        infoLabel.setText("You've won the puzzle!");
    }
}
