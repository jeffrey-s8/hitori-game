import javafx.scene.layout.StackPane;

//This class contains information about each cell in the GridPane.
class Cell {
    boolean isVisited;
    boolean isEliminated;

    private StackPane pane;
    private int[] id;
        //id = [row,col]

    Cell(StackPane pane, int[] id) {
        isVisited = false;
        isEliminated = false;
        this.pane = pane;
        this.id = id;
    }

    public StackPane getPane() {
        return pane;
    }

    public int[] getId() {
        return id;
    }
}
