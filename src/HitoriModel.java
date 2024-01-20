import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//Singleton class: Orchestrates the calculation/data end of the Hitori program.
public class HitoriModel {
    private static HitoriModel hitoriModel = new HitoriModel();
    private static int[] hitoriMatrix;
    private static int sideLength;
    private static int seed;
    private static Random rnd;
    private static List<int[]> blackedOutIndexList;
    private static CellGrid cellGrid;

    private HitoriModel() {
    }

    public static HitoriModel getInstance(){
        return hitoriModel;
    }

    public static int getSideLength() {
        return sideLength;
    }

    public static int[] getHitoriMatrix() {
        return hitoriMatrix;
    }

      /*
        Initial:
            view sends sideLength, seed, & empty hitoriMatrix to model
            view asks model for full hitoriMatrix

            model sends sideLength, seed, & empty hitoriMatrix to MatrixGenerator
            model asks MatrixGenerator for full hitoriMatrix
            MatrixGenerator sends back full hitoriMatrix
            MatrixGenerator sends back the new seed used (in case old one didn't work)

            model sends hitoriMatrix to Concealer
            model asks Concealer to change some cells in hitoriMatrix
            Concealer sends back changed hitoriMatrix
            Concealer sends back an "indexList" of cells that were changed in hitoriMatrix

            model sends hitoriMatrix back to view
         */
    int[] startPuzzle(int sideLength, int seed) {
        this.sideLength = sideLength;
        this.seed = seed;
        rnd = new Random(seed);

        MistakeChecker.restart();
        CellGrid.restart();

        hitoriMatrix = MatrixGenerator.restart(sideLength, rnd, seed);
        hitoriMatrix = Concealer.restart(sideLength, rnd, seed, hitoriMatrix);
        blackedOutIndexList = Concealer.getBlackedOutIndexList();

        MistakeChecker.restart();
        CellGrid.restart();

        return hitoriMatrix;
    }

    static void startFilePuzzle(int[] hitoriMatrix, int sideLength){
        HitoriModel.hitoriMatrix = hitoriMatrix;
        HitoriModel.sideLength = sideLength;

        MistakeChecker.restart();
        CellGrid.restart();

        HitoriView.restartGrid(hitoriMatrix,sideLength);
    }
}

















































