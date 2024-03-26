package project2;
import java.util.*;
import java.io.*;

public class MesaE_Project2_Main {
    public static void main(String[] args) {
        if (args.length < 6) {
            return;
        }
        String inFile = args[0];
        int opChoice = Integer.parseInt(args[1]);
        String outFile1 = args[2];
        String outFile2 = args[3];
        String outFile3 = args[4];
        String deBugFile = args[5];

        int[][] image = Enhancement.loadImage(inFile);
        Enhancement.mirrorFraming(image);
        int[][][] masks = Enhancement.loadMasks();

        int[][] filteredImage;
        if (opChoice == 1) {
            filteredImage = Enhancement.computeAvg5x5(image, new int[image.length][image[0].length]);
        } else if (opChoice == 2) {
            filteredImage = Enhancement.cornerPreserveAvg(image, masks);
        } else {
            return;
        }
        Enhancement.combinedOutput(image, filteredImage,
                Enhancement.computeHist(filteredImage), outFile1);

        Enhancement.imgReformat(filteredImage, outFile2);

        Enhancement.saveNormalHistogram(Enhancement.computeHist(filteredImage), outFile3);
    }
}