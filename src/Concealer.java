import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//Singleton class: generates blacked-out cells from MatrixGenerator.hitoriMatrix
//                  and conceals each cell as one of their row-column neighbours.
public class Concealer {
    private static Concealer concealer = new Concealer();
    private static int sideLength;
    private static int seed;
    private static int[] hitoriMatrix;
    private static Random rnd;
    private static List<int[]> blackedOutIndexList;

    private Concealer(){
    }

    public static Concealer getInstance(){
        return concealer;
    }

    static int[] restart(int sideLength, Random rnd, int seed, int[] hitoriMatrix){
        Concealer.sideLength = sideLength;
        Concealer.rnd = rnd;
        Concealer.seed = seed;
        Concealer.hitoriMatrix = hitoriMatrix;
        blackedOutIndexList = new ArrayList<>();
        generateBlackedOutCells();
        concealBlackedOutCells();
        return hitoriMatrix;
    }

    public static List<int[]> getBlackedOutIndexList() {
        return blackedOutIndexList;
    }

    /*
    Notice that each column has no more than 3 black dots.
    Use the BlackDots method rather than the Tree method; the grid isn't
    tree-like.
    Ok, so if sideLength = 8, 8/2 -1 = 3 blacked-out cells
    Ok, so if sideLength = 6, 6/2 -1 = 2
    Ok, so if sideLength = 7, 7/2 -1 = 2.5 = 3 (so the ceiling value).

    For each cell, the chance of it being blacked-out is 60%
    even if no blacked-out cells are near it.
     */
    private static void generateBlackedOutCells(){
        int maxBlackedOutCells = (int) Math.ceil( ((double) sideLength/2) - 1 );

        //setup the CellGrid
        for (int index = 0; index < hitoriMatrix.length; index++) {
            CellGrid.getCellArray()[index] =
                    new Cell(null, indexToID(index));
        }

        //loop over hitoriMatrix (check each column, then each row),
        for(int col = 0; col < sideLength; col++){
            int actualBlackedOutCells = rnd.nextInt(maxBlackedOutCells+1);

            for (int row = 0; row < sideLength; row++) {
                boolean checkLeft = false;
                boolean checkUp = false;

                //checkLeft
                if(col == 0){
                    checkLeft = true;
                }
                else if (hitoriMatrix[sideLength * row + (col-1)] != 0){
                    checkLeft = true;
                }

                //checkUp
                if (row == 0){
                    checkUp = true;
                }
                //to prevent the true-blacked-out cells being adjacent to each other
                else if (hitoriMatrix[sideLength * (row-1) + col] != 0){
                    checkUp = true;
                }

                //checking for constraint2, constraint3, and other factors
                if (checkLeft && checkUp && actualBlackedOutCells != 0){
                    //testing if the cell fulfills constraint3 via assumption
                    CellGrid.getCellArray()[sideLength * row + col].isEliminated = true;

                    if(MistakeChecker.keepsConstraintThree() && checkChance(80,100)){
                        hitoriMatrix[sideLength * row + col] = 0;
                        actualBlackedOutCells--;
                        blackedOutIndexList.add(new int[]{row,col}); //record it's position
                    }
                    else{
                        CellGrid.getCellArray()[sideLength * row + col].isEliminated = false;

                    }
                }

                //unvisit all the cells to reset checking constraint3
                for(Cell cell: CellGrid.getCellArray()){
                    cell.isVisited = false;
                }
            }
        }
        System.out.println("Hint is: "+Arrays.toString(hitoriMatrix)+"\n" +
                "The value 0 is a blacked-out cell.");

        //Part 2: now in reverse to check if it's alright (ensure cells connected in a single component)
    }

    private static int[] indexToID(Integer index) {
        int[] id = new int[2];
        id[0] = (int) Math.floor((double) index / sideLength);
        id[1] = (index % sideLength);
        return id;
    }

    // For a certain chance, returns whether that chance is successful.
    private static Boolean checkChance(int numerator, int denominator){
        int pointer = rnd.nextInt(denominator)+1;
        //pointer: random number from 1 - 100 (if denominator=100).
        //return true if pointer <= 20 (for 20% chance)
        if (pointer <= numerator) {
            return true;
        }
        return false;
    }

    /*
    Part 3: Converts the values in hitoriMatrix that have "0" into a candidate
     */
    static void concealBlackedOutCells(){
        for(int[] id: blackedOutIndexList) {
            //get all the numbers in the same (row and column) as such cell (except the number zero)
            List<Integer> candidates = new ArrayList<>();
            for (int col = 0; col < sideLength; col++) {
                candidates.add(hitoriMatrix[sideLength * id[0] + col]);
            }
            for (int row = 0; row < sideLength; row++) {
                candidates.add(hitoriMatrix[sideLength * row + id[1]]);
            }

            //removing duplicates in candidates
            List<Integer> conciseCandidates = new ArrayList<>();
            for(Integer candidate: candidates){
                if((!conciseCandidates.contains(candidate)) && (candidate != 0)){ //"0" isn't a candidate
                    conciseCandidates.add(candidate);
                }
            }

            //Redundant catch block.
            try {
                hitoriMatrix[sideLength * id[0] + id[1]] = MatrixGenerator.getRandom(conciseCandidates);
            }
            catch (Exception e) {
                System.out.println("No candidates to mask blacked out cell. ID:"+id[0]+","+id[1]);
            }
        }
//        System.out.println(Arrays.toString(hitoriMatrix));
    }
}





















