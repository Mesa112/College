package thresholding; 
    import java.io.*;
    import java.util.Scanner;
    
    public class MesaE_Thresholding_0B_Main {
        public static void main(String[] args) {
            if (args.length < 4) {
                return;
            }
            String inFile = args[0];
            int thrValue = Integer.parseInt(args[1]);
            String outFile1 = args[2];
            String outFile2 = args[3];
    
            try {
                Scanner input = new Scanner(new File(inFile));
                FileWriter binaryOut = new FileWriter(outFile1);
                FileWriter nonBinaryOut = new FileWriter(outFile2);
                int numRows = input.nextInt();
                int numCols = input.nextInt();
                int minVal = input.nextInt();
                int maxVal = input.nextInt();
    
                Image img = new Image(numRows, numCols, minVal, maxVal, thrValue);
    
                binaryOut.write(numRows + " " + numCols + " 0 1\n");
                nonBinaryOut.write(numRows + " " + numCols + " 0 " + maxVal + "\n");
    
                img.processing(input, binaryOut, nonBinaryOut);
    
                input.close();
                binaryOut.close();
                nonBinaryOut.close();
    
                System.out.println("Done!");
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + inFile);
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error processing the file.");
                e.printStackTrace();
            }
        }
    }