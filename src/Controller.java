import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/*
Contains 2 Inner classes. This class responds to user input sent in from the Node objects in HitoriView.java
and responds depending on CellGrid.java or the type of String sent.
 */
public class Controller {

    static class GridSelectHandler implements EventHandler<MouseEvent> {
        private Cell cell;

        public GridSelectHandler(Cell cell) {
            this.cell = cell;
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                Integer index = HitoriModel.getSideLength()*cell.getId()[0]+cell.getId()[1];
                if (!cell.isEliminated) {   //if its not eliminated yet, eliminate it
                    HitoriView.eliminate(cell.getPane());
                    cell.isEliminated = true;
                } else {    //if it is already eliminated, reactivate back the cell
                    HitoriView.reactivate(cell.getPane());
                    cell.isEliminated = false;
                }
            }
            MistakeChecker.checkForMistake();
        }
    }

    static class ClickHandler implements EventHandler<ActionEvent>{
        String instruction;
        protected static Stage stage;

        public ClickHandler(String instruction) {
            this.instruction = instruction;
            stage = HitoriView.stage;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            if (instruction.equals("reset") && HitoriView.canResetPuzzle()){

                for(Cell cell: CellGrid.getCellArray()) {
                    //reactivate all eliminated cells
                    if (cell.isEliminated) {
                        HitoriView.reactivate(cell.getPane());
                        cell.isEliminated = false;
                    }

                    //unhighlight all cells
                    HitoriView.unhighlightMistake(cell);
                    HitoriView.continueLabel();

                    //re-enable all the cell panes
                    cell.getPane().setDisable(false);
                }
            }
            else if (instruction.equals("load")) {
                    /*
                    Launches a FileChooser window in order to select a Filename,
                    send such to 'LoadFilePuzzle', and return an array from LoadFilePuzzle
                    as the new 'hitoriMatrix'.
                     */
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Pre-Defined Hitori Puzzle");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt")
                );
                File selectedFile = fileChooser.showOpenDialog(stage);
                LoadFilePuzzle loadFilePuzzle = new LoadFilePuzzle();
                loadFilePuzzle.restart(
                        selectedFile.getAbsolutePath()
                );

                //Static method: Two objects of same class cannot use such method
                HitoriModel.startFilePuzzle(
                        loadFilePuzzle.parseFile(), loadFilePuzzle.getSideLength()
                );
            }
        }
    }
}
