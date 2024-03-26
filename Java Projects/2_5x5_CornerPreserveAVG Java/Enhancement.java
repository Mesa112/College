package project2;

import java.io.*;
import java.util.Scanner;

public class Enhancement {
    int numRows, numCols, maxVal, minVal;
    int[][] mirrorFramedAry;
    int[][] avgAry;
    int[][] CPavgAry;
    static int[][][] masks = new int[8][5][5];
    int[] Avg_histAry;
    int[] CPavg_histAry;

    public Enhancement(int numRows, int numCols, int minVal, int maxVal) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.mirrorFramedAry = new int[numRows + 4][numCols + 4];
        this.avgAry = new int[numRows + 4][numCols + 4];
        this.CPavgAry = new int[numRows + 4][numCols + 4];
        this.Avg_histAry = new int[maxVal + 1];
        this.CPavg_histAry = new int[maxVal + 1];
        initializeArrays();
    }

    public void initializeArrays() {
        for (int i = 0; i < numRows + 4; i++) {
            for (int j = 0; j < numCols + 4; j++) {
                mirrorFramedAry[i][j] = 0;
                avgAry[i][j] = 0;
                CPavgAry[i][j] = 0;
            }
        }
        for (int i = 0; i < maxVal + 1; i++) {
            Avg_histAry[i] = 0;
            CPavg_histAry[i] = 0;
        }
    }

    public static int[][] loadImage(String inFile) {
        try {
            Scanner input = new Scanner(new File(inFile));
            String header = input.nextLine();
            String[] headerParts = header.split(" ");
            int rows = Integer.parseInt(headerParts[0]);
            int cols = Integer.parseInt(headerParts[1]);
            int[][] image = new int[rows][cols];

            int row = 0;
            while (input.hasNextLine() && row < rows) {
                String[] values = input.nextLine().trim().split("\\s+");
                for (int col = 0; col < Math.min(values.length, cols); col++) {
                    image[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }
            input.close();
            return image;
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void mirrorFraming(int[][] mirrorFramedAry) {
        int numRows = mirrorFramedAry.length;
        int numCols = mirrorFramedAry[0].length;
        for (int col = 2; col < numCols - 2; col++) {
            mirrorFramedAry[1][col] = mirrorFramedAry[2][col];
            mirrorFramedAry[0][col] = mirrorFramedAry[2][col];
            mirrorFramedAry[numRows - 2][col] = mirrorFramedAry[numRows - 3][col];
            mirrorFramedAry[numRows - 1][col] = mirrorFramedAry[numRows - 3][col];
        }
        for (int row = 2; row < numRows - 2; row++) {
            mirrorFramedAry[row][1] = mirrorFramedAry[row][2];
            mirrorFramedAry[row][0] = mirrorFramedAry[row][2];
            mirrorFramedAry[row][numCols - 2] = mirrorFramedAry[row][numCols - 3];
            mirrorFramedAry[row][numCols - 1] = mirrorFramedAry[row][numCols - 3];
        }
        mirrorFramedAry[0][0] = mirrorFramedAry[2][2];
        mirrorFramedAry[1][1] = mirrorFramedAry[2][2];
        mirrorFramedAry[0][numCols - 1] = mirrorFramedAry[2][numCols - 3];
        mirrorFramedAry[1][numCols - 2] = mirrorFramedAry[2][numCols - 3];
        mirrorFramedAry[numRows - 1][0] = mirrorFramedAry[numRows - 3][2];
        mirrorFramedAry[numRows - 2][1] = mirrorFramedAry[numRows - 3][2];
        mirrorFramedAry[numRows - 1][numCols - 1] = mirrorFramedAry[numRows - 3][numCols - 3];
        mirrorFramedAry[numRows - 2][numCols - 2] = mirrorFramedAry[numRows - 3][numCols - 3];
    }
    public static int[][] computeAvg5x5(int[][] mirrorFramedAry, int[][] avgAry) {
        int sum, count;
        for (int i = 2; i < mirrorFramedAry.length - 2; i++) {
            for (int j = 2; j < mirrorFramedAry[i].length - 2; j++) {
                sum = 0;
                count = 0;
                for (int k = i - 2; k <= i + 2; k++) {
                    for (int l = j - 2; l <= j + 2; l++) {
                        sum += mirrorFramedAry[k][l];
                        count++;
                    }
                }
                avgAry[i - 2][j - 2] = sum / count;
            }
        }
        return avgAry;
    }
    public static void combinedOutput(int[][] originalImage, int[][] smoothedImage, int[] histogram, String outFile) {
        try {
            int combinedWidth = originalImage[0].length + smoothedImage[0].length + 10;
            int combinedHeight = Math.max(originalImage.length, smoothedImage.length);

            PrintWriter fileOut = new PrintWriter(new FileWriter(outFile));
            fileOut.println(combinedHeight + " " + combinedWidth  + " " + 0 + " " + 63);

            for (int row = 0; row < combinedHeight; row++) {
                for (int col = 0; col < originalImage[0].length; col++) {
                    if (row < originalImage.length) {
                        fileOut.print(originalImage[row][col] + " ");
                    } else {
                        fileOut.print("  ");
                    }
                }
                fileOut.print("     ");
                for (int col = 0; col < smoothedImage[0].length; col++) {
                    if (row < smoothedImage.length) {
                        fileOut.print(smoothedImage[row][col] + " ");
                    } else {
                        fileOut.print("  ");
                    }
                }
                fileOut.print("     ");
                if (row < histogram.length) {
                    fileOut.print(histogram[row]);
                }

                fileOut.println();
            }
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int[][] cornerPreserveAvg(int[][] mirrorFramedAry, int[][][] masks) {
        int numRows = mirrorFramedAry.length;
        int numCols = mirrorFramedAry[0].length;
        int[][] result = new int[numRows - 4][numCols - 4];

        for (int i = 2; i < numRows - 2; i++) {
            for (int j = 2; j < numCols - 2; j++) {
                int sum = 0;
                int count = 0;
                for (int k = i - 2; k <= i + 2; k++) {
                    for (int l = j - 2; l <= j + 2; l++) {
                        sum += mirrorFramedAry[k][l];
                        count++;
                    }
                }
                result[i - 2][j - 2] = sum / count;
            }
        }
        return result;
    }

    public static int[] computeHist(int[][] ary) {
        int[] histogram = new int[63];
        for (int i = 0; i < ary.length; i++) {
            for (int j = 0; j < ary[i].length; j++) {
                histogram[ary[i][j]]++;
            }
        }
        return histogram;
    }
    public static void printHist(int[] histAry, String outFile) {
        try {
            FileWriter writer = new FileWriter(outFile);
            for (int i = 0; i < histAry.length; i++) {
                writer.write(i + " ");
                for (int j = 0; j < histAry[i]; j++) {
                    writer.write("+");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void imgReformat(int[][] inAry, String outFile) {
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter(outFile));
            fileOut.println(inAry.length + " " + inAry[0].length + " " + 0 + " " + 63);
            String str = Integer.toString(63);
            int width = str.length();
            for (int i = 2; i < (inAry.length - 2); i++) {
                for (int j = 2; j < (inAry[i].length - 2); j++) {
                    str = Integer.toString(inAry[i][j]);
                    fileOut.print(str);
                    int ww = str.length();
                    while (ww <= width) {
                        fileOut.print(" ");
                        ww++;
                    }
                }
                fileOut.println();
            }
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int convolution5x5(int[][] mirrorFramedAry, int i, int j, int[][] mask, String deBugFile) {
        int result = 0;
        try {
            FileWriter debugWriter = new FileWriter(deBugFile, true);
            for (int r = -2; r <= 2; r++) {
                for (int c = -2; c <= 2; c++) {
                    result += mask[r + 2][c + 2] * mirrorFramedAry[i + r][j + c];
                }
            }
            debugWriter.write(result + "\n");
            debugWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static int[][][] loadMasks() {
        String[] maskFiles = {
                "/Users/estebanm/Documents/381projects/M1.txt",
                "/Users/estebanm/Documents/381projects/M2.txt",
                "/Users/estebanm/Documents/381projects/M3.txt",
                "/Users/estebanm/Documents/381projects/M4.txt",
                "/Users/estebanm/Documents/381projects/M5.txt",
                "/Users/estebanm/Documents/381projects/M6.txt",
                "/Users/estebanm/Documents/381projects/M7.txt",
                "/Users/estebanm/Documents/381projects/M8.txt"
        };
        for (int i = 0; i < maskFiles.length; i++) {
            try {
                Scanner input = new Scanner(new File(maskFiles[i]));
                String[] header = input.nextLine().trim().split("\\s+");
                int rows = Integer.parseInt(header[0]);
                int cols = Integer.parseInt(header[1]);

                masks[i] = new int[rows][cols];

                for (int row = 0; row < rows; row++) {
                    if (!input.hasNextLine()) {
                        break;
                    }
                    String[] values = input.nextLine().trim().split("\\s+");
                    for (int col = 0; col < Math.min(values.length, cols); col++) {
                        masks[i][row][col] = Integer.parseInt(values[col]);
                    }
                }
                input.close();
            } catch (FileNotFoundException e) {
                masks[i] = null;
            } catch (Exception e) {
                masks[i] = null;
            }
        }

        return masks;
    }
    public static void saveNormalHistogram(int[] histogram, String outFile) {
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter(outFile));
            int maxFrequency = 0;
            for (int count : histogram) {
                maxFrequency = Math.max(maxFrequency, count);
            }
            int maxBarLength = 60;

            for (int i = 0; i < histogram.length; i++) {
                fileOut.print(i + ": ");
                int barLength = Math.round((float) histogram[i] / maxFrequency * maxBarLength);
                for (int j = 0; j < barLength; j++) {
                    fileOut.print("+");
                }
                fileOut.println();
            }

            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
