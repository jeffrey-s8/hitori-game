import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
Singleton class: with each user input on the grid, the class checks whether
                  the 3 rules are held or not and reacts appropriately.
 */
public class MistakeChecker {
    private static MistakeChecker mistakeChecker = new MistakeChecker();
    private static int sideLength;
    private static Set<Cell> redRecord;

        /*
        redRecord: list of eliminated cells not following constraints 2 or 3
            a Set to prevent duplicates of the same neighbour in keepsConstraintTwo()
         */

    private MistakeChecker() {
    }

    public static MistakeChecker getInstance(){
        return mistakeChecker;
    }

    public static void restart(){
        sideLength = HitoriModel.getSideLength();
        redRecord = new HashSet<>();
    }


    //When user violates constraints 2 or 3,
    //immediately highlight the cells causing such & inform to the user there's a mistake.
    static void checkForMistake() {
        calibrateCells();
        if (keepsConstraintTwo()) {
            if (keepsConstraintThree()){
                if (keepsConstraintOne()) {
                    HitoriView.playWinAnimation();
                }
                else{
                    //No mistakes. No need for redRecord
                    HitoriView.continueLabel();
                }
            }
            else {
                for (Cell cell : redRecord) {
                    HitoriView.highlightMistake(cell.getPane());
                }
            }
        } else {
            for (Cell cell : redRecord) {
                HitoriView.highlightMistake(cell.getPane());
            }

        }
    }

    protected static void calibrateCells() {
        //unhighlight and unvisit all cells at cellArray. clear redRecord
        for (Cell cell : CellGrid.getCellArray()) {
            cell.isVisited = false;
        }

        //if we unhighlight a cell, we must put it back to it's proper place
        for(Cell cell: redRecord){
            HitoriView.unhighlightMistake(cell);
        }

        redRecord.clear();
    }

    // 2) no adjacent eliminated cells
    private static boolean keepsConstraintTwo() {
        boolean keepsConstraintTwo = true;

        //find eliminated cells and check if they have adjacent eliminated cells
        for(Cell cell: CellGrid.getCellArray()){
            if(cell.isEliminated){
                Cell[] neighbourList = CellGrid.getNeighbourList(cell.getId());

                //some neighbours may be beyond the grid
                //highlight cell and adjacent cells (if eliminated)
                for(Cell neighbour: neighbourList) {
                    if(neighbour != null && neighbour.isEliminated) {
                        redRecord.add(neighbour);
                        keepsConstraintTwo = false;
                    }
                }
            }
        }

        return keepsConstraintTwo;
    }

    // 3) all white cells are connected to each other in a single component.
    protected static boolean keepsConstraintThree(){
        boolean keepsConstraintThree = true;

        //Checking Constraint 3: One White Cluster

        //finding an un-eliminated cell to make snakes to spread
        for(Cell cell: CellGrid.getCellArray()){
            if(!cell.isEliminated){
                spreadFrom(cell);
                break;
            }
        }

        //find unvisited white cells
        for(Cell cell: CellGrid.getCellArray()){
            if(!cell.isEliminated && !cell.isVisited){
                redRecord.add(cell);
                keepsConstraintThree = false;
            }
        }

        return keepsConstraintThree;
    }

    private static void spreadFrom(Cell cell){
        cell.isVisited = true;
        //redundant block: if snake went beyond the grid, go back.
        if (cell == null){
            return;
        }

        Cell[] neighbourList = CellGrid.getNeighbourList(cell.getId());
        boolean[] checkDirections = new boolean[4];
        for (boolean dir: checkDirections) {dir = false;}
            //checkUp,Left,Right,Down

        for (int i = 0; i < 4; i++) {
            if (neighbourList[i] != null && !neighbourList[i].isEliminated && !neighbourList[i].isVisited) {
                //visit cell
                checkDirections[i] = true;
                spreadFrom(neighbourList[i]);
            }
        }

        //if snake has reached dead end, go back.
        if(!checkDirections[0] && !checkDirections[1] &&
                !checkDirections[2] && !checkDirections[3]){
            return;
        }
    }

    // 1) no duplicate numbers in each row and column.
    protected static boolean keepsConstraintOne() {
        boolean keepsConstraintOne = true;

        //for each row on cellArray
        for (int row = 0; row < sideLength; row++) {
            //checking for duplicates
            List<Integer> rowNumbers = new ArrayList<>();
            for (int col = 0; col < sideLength; col++) {
                int index = sideLength*row+col;
                int cellValue = HitoriModel.getHitoriMatrix()[index];
                Cell cell = CellGrid.getCellArray()[index];
                if(!cell.isEliminated && rowNumbers.contains(cellValue)){
                    //can't add cell to redRecord.
                    keepsConstraintOne = false;
                }
                else if (!cell.isEliminated && !rowNumbers.contains(cellValue)){
                    rowNumbers.add(cellValue);
                }
            }
        }

        //for each column on cellArray
        for (int col = 0; col < sideLength; col++) {
            //checking for duplicates
            List<Integer> columnNumbers = new ArrayList<>();
            for (int row = 0; row < sideLength; row++) {
                int index = sideLength*row+col;
                int cellValue = HitoriModel.getHitoriMatrix()[index];
                Cell cell = CellGrid.getCellArray()[index];
                if(!cell.isEliminated && columnNumbers.contains(cellValue)){
                    //can't add cell to redRecord.
                    keepsConstraintOne = false;
                }
                else if (!cell.isEliminated && !columnNumbers.contains(cellValue)){
                    columnNumbers.add(cellValue);
                }
            }
        }

//        System.out.println(keepsConstraintOne);
        return keepsConstraintOne;
    }
}
