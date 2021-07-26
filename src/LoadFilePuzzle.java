import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
Parses the file received from Controller.ClickHandler.
Sends back a 1-dimensional grid for HitoriModel.hitoriMatrix.
 */
class LoadFilePuzzle{
    private BufferedReader reader;
    private String filename;
    private FileReader fileReader;
    private static int sideLength;
    private static int[] hitoriMatrix;

    //load the file. make sure it's .txt...
    public void restart(String filename){
        this.filename = filename;

        //catching error of missing file.
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.out.println("Error message....");
        }
    }

    /*
    Returns next line of the .txt file.
    */
    public String getLine() {
        try {
            return reader.readLine(); //reads nextline from the file.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
    Returns true if the stream is ready to be read.
     */
    public Boolean fileIsReady() {
        //checking if nothing on the reader. if so, return false.
        if (reader == null){
            return false;
        }

        //catching error of file with no text inside.
        try {
            return reader.ready();
        } catch (IOException e) {
            System.err.println("Error message. File has no text inside.");
        }

        return false;
    }

    /*
    Gets the details of each Cage and puts them in an ArrayList
     */
    public int[] parseFile() {

        //while file is still ready,...
        boolean hasLength = false;
        int row = 0;
        while (fileIsReady()) {
            String line = this.getLine(); //takes a line
            String[] detailArray = line.split(" "); //splits the line to "x" words...

            if (!hasLength){
                sideLength = detailArray.length;
                hitoriMatrix = new int[sideLength*sideLength];
                hasLength = true;
            }

            //Using Math.min() for now. Later on, an alert will be sent to invalid puzzles.
            for (int column = 0; column < Math.min(detailArray.length,sideLength); column++) {
                hitoriMatrix[sideLength*row+column] = Integer.valueOf(detailArray[column]);
            }

            row++; //increment row
        }
        return hitoriMatrix;
    }

    public static int getSideLength() {
        return sideLength;
    }
}

