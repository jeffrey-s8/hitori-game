import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
A class that generates a random sideLength*sideLength grid
based on the seed and the sideLength such that no row or column have any duplicates.
*/
public class MatrixGenerator {
    private static MatrixGenerator matrixGenerator = new MatrixGenerator();
    static int sideLength;
    static Integer seed;
    static Random rnd;
    static int[] hitoriMatrix;
    static ArrayList<ArrayList<Integer>> allRowCandidates;
    static ArrayList<ArrayList<Integer>> allColumnCandidates;

    public MatrixGenerator() {
    }

    public static MatrixGenerator getInstance(){
        return matrixGenerator;
    }

    static int[] restart(int sideLength, Random rnd, int seed){
        MatrixGenerator.sideLength = sideLength;
        MatrixGenerator.rnd = rnd;
        MatrixGenerator.seed = seed;
        createCandidates();
        generateRandomNumbers();
        return hitoriMatrix;
    }

    static int getNewSeed(){
        return seed;
    }

    /*
    This exception is used in MatrixGenerator so far.
    It's used to identify when there are no more
    candidates/values for a cell to choose from.
    To combat such, this exception is thrown, the seed is incremented
    and the matrix generator restarts
    */
    private static class OutOfCandidatesException extends Exception {
    }

    static void createCandidates() {
        allRowCandidates = new ArrayList<ArrayList<Integer>>();
        allColumnCandidates = new ArrayList<>();

        /*
        If sideLength = 3,
        allRowCandidates = [[1,2,3],[1,2,3],[1,2,3]]
         */
        for (int row = 0; row < sideLength; row++) {
            allRowCandidates.add(new ArrayList<Integer>());
            for (int n = 0; n < sideLength; n++) {
                allRowCandidates.get(row).add(n + 1);
            }
        }

        /*
        If sideLength = 3,
        allColumnCandidates = [[1,2,3],[1,2,3],[1,2,3]]
         */
        for (int col = 0; col < sideLength; col++) {
            allColumnCandidates.add(new ArrayList<Integer>());
            for (int n = 0; n < sideLength; n++) {
                allColumnCandidates.get(col).add(n + 1);
            }
        }
    }

    static void generateRandomNumbers() {
        hitoriMatrix = new int[sideLength * sideLength]; //initialCapacity is supposed to be 3*3.

        //for each row,
        for (int row = 0; row < sideLength; row++) {

            //look at the 1st column, then..
            for (int col = 0; col < sideLength; col++) {

//                    Each element of allRowCandidates is an arrayList.
//                    Each element of allColumnCandidates is an arrayList.
//
//                    Thus, ArrayList<Integer> rowCandidates = allRowCandidates.get(row);
//                    And, ArrayList<Integer> columnCandidates = allColumnCandidates.get(col);

                ArrayList<Integer> cellCandidates = commonNumbers(
                        allRowCandidates.get(row), //rowCandidates
                        allColumnCandidates.get(col));  //columnCandidates

                //Get a random integer from cellCandidates
                Integer chosenCellCandidate;

                try {
                    chosenCellCandidate = getRandom(cellCandidates);
                }
                //Problem #1: When a rowCandidates = [2,4,3] and
                //a columnCandidates = [5,1] for cellID [3,2] in 5x5 grid.
                catch (OutOfCandidatesException ooce) {
                    seed = seed + 5; //create new seed
                    restart(sideLength,rnd,seed); //restart
                    return; //returns to avoid the leftovers to stack-up
                }


                hitoriMatrix[sideLength * row + col] = chosenCellCandidate;

                //removing the Object Integer from the arrayLists
                //"rowCandidates" & "columnCandidates".
                allRowCandidates.get(row).remove(chosenCellCandidate);
                allColumnCandidates.get(col).remove(chosenCellCandidate);
            }
        }

//            System.out.println(Arrays.toString(hitoriMatrix));
    }

    //If for example, a1 = [1,2] and a2 = [1,3]
    //then, newList = [1]
    public static ArrayList<Integer> commonNumbers(ArrayList<Integer> listA, ArrayList<Integer> listB) {

        ArrayList<Integer> newList = new ArrayList<Integer>();

        //for each integer in listA,
        //if "n" is in listB, add to newList
        for (Integer n : listA) {
            if (listB.contains(n)) {
                newList.add(n);
            }
        }

        return newList;
    }

    public static Object getRandom(Object[] array) {
        int rndInt = rnd.nextInt(array.length);
        return array[rndInt];
    }

    public static Integer getRandom(List<Integer> array) throws OutOfCandidatesException {
        if (array.size() != 0) {
            int rndInt = rnd.nextInt(array.size());
            return array.get(rndInt);
        } else {
            //return 0;
            throw new OutOfCandidatesException();
        }
    }
}
