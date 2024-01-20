import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/*
Singleton class: stores all the Cell objects used in the program. Also, can output
the adjacent neighbours of a specific cell.
 */
public class CellGrid {
    private static CellGrid cellGrid = new CellGrid();
    private static Cell[] cellArray;
    private static int sideLength;

    private CellGrid(){
    }

    public static CellGrid getInstance(){
        return cellGrid;
    }

    static void restart(){
        sideLength = HitoriModel.getSideLength();
        cellArray = new Cell[sideLength*sideLength];
    }

    public static Cell[] getCellArray() {
        return cellArray;
    }

    static Cell[] getNeighbourList(int id[]) {
        boolean canShiftUp = false;
        boolean canShiftLeft = false;
        boolean canShiftRight = false;
        boolean canShiftDown = false;

        //local variable
        Cell[] neighbourList = new Cell[4];
            //referenced as up(0). left(1). right(2). down(3).

        //turn id to index
        int index = sideLength * id[0] + id[1];

        if ((id[0] - 1) >= 0) {
            canShiftUp = true;
            neighbourList[0] = cellArray[index - sideLength];
        }
        if ((id[1] - 1) >= 0) {
            canShiftLeft = true;
            neighbourList[1] = cellArray[index - 1];
        }
        if ((id[1] + 1) < sideLength) {
            canShiftRight = true;
            neighbourList[2] = cellArray[index + 1];
        }
        if ((id[0] + 1) < sideLength) {
            canShiftDown = true;
            neighbourList[3] = cellArray[index + sideLength];

        }

        return neighbourList;
    }
}
