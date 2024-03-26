package thresholding;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Image {
    int numRows, numCols, minVal, maxVal, thrValue;
    public Image(int numRows, int numCols, int minVal, int maxVal, int thrValue) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.thrValue = thrValue;
    }
    public void processing(Scanner input, FileWriter binaryOut, FileWriter nonBinaryOut) throws IOException {
        int counter = 0;
        while (input.hasNextInt()) {
            int pixel = input.nextInt();
            if (pixel >= thrValue) {
                binaryOut.write("1 ");
                nonBinaryOut.write(pixel + " ");
            } else {
                binaryOut.write("0 ");
                nonBinaryOut.write("0 ");
            }
            counter++;
            if (counter == numCols) {
                binaryOut.write("\n");
                nonBinaryOut.write("\n");
                counter = 0;
            }
        }
    }
}

