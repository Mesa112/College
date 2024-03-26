#include <iostream>
#include <fstream>
#include <string>
#include <sstream>

using namespace std;

class morphology{
public:
    int numImgRows, numImgCols, imgMin, imgMax, numStructRows, numStructCols, structMin, structMax, rowOrigin, colOrigin, rowFrameSize, colFrameSize, extraRows, extraCols, rowSize, colSize;

    morphology(int numImgRows, int numImgCols, int imgMin, int imgMax):
        numImgRows(numImgRows), numImgCols(numImgCols), imgMin(imgMin), imgMax(imgMax) {}

    int** zeroFrameAry;
    int** morphAry;
    int** tempAry;
    int** structAry;

    void initialize(){
        zeroFrameAry = new int*[rowSize];
        for(int i = 0; i < rowSize; i++) {
            zeroFrameAry[i] = new int[colSize];
        }
        
        structAry = new int*[numStructRows];
        for (int i = 0; i < numStructRows; i++) {
            structAry[i] = new int[numStructCols];
        }
        
        morphAry = new int*[rowSize];
        for (int i = 0; i < rowSize; i++) {
            morphAry[i] = new int[colSize];
        }
        
        tempAry = new int*[rowSize];
        for (int i = 0; i < rowSize; i++) {
            tempAry[i] = new int[colSize];
        }
    } // end-initialize

    void setStructuringElement(int numRows, int numCols, int minVal, int maxVal, int rowOrigin, int colOrigin) {
        numStructRows = numRows;
        numStructCols = numCols;
        structMin = minVal;
        structMax = maxVal;
        this->rowOrigin = rowOrigin;
        this->colOrigin = colOrigin;
        rowFrameSize = numStructRows / 2;
        colFrameSize = numStructCols / 2;
        extraRows = rowFrameSize * 2;
        extraCols = colFrameSize * 2;
        rowSize = numImgRows + extraRows;
        colSize = numImgCols + extraCols;
    }

    void zero2DAry(int** Ary,int nRows, int nCols){
        for(int i = 0; i < nRows; i++){
            for(int j = 0; j < nCols; j++){
                Ary[i][j] = 0;
            }
        }
    }

    void loadImg(ifstream& file, int** inAry) {
        for (int i = rowOrigin; i < rowSize - rowFrameSize; i++) {
            for (int j = colOrigin; j < colSize - colFrameSize; j++) {
                file >> inAry[i][j];
            }
        }
    }
    
    void loadStruct(ifstream& file, int** inAry){
        for(int i = 0; i < numStructRows; i++){
            for(int j = 0; j < numStructCols; j++){
                file >> inAry[i][j];
            }
        }
    }

    void ComputeDialation(int** inAry, int** outAry){
        int i = rowFrameSize;
        while(i < rowSize - rowFrameSize){
            int j = colFrameSize;
            while(j < colSize - colFrameSize){
                if(inAry[i][j] > 0){
                    onePixelDialation(i, j, inAry, outAry);
                }
                j++;
            }
            i++;
        }
    }

    void ComputeErosion(int** inAry, int** outAry){
        int i = rowFrameSize;
        while(i < rowSize - rowFrameSize){
            int j = colFrameSize;
            while (j < colSize - colFrameSize) {
                if(inAry[i][j] > 0){
                    onePixelErosion(i, j, inAry, outAry);
                }
                j++;
            }
            i++;
        }
        
    }


    void onePixelDialation(int i, int j,int** inAry, int** outAry) {
        int iOffset = i - rowOrigin;
        int jOffset = j - colOrigin;

        int rIndex = 0;
        while (rIndex < numStructRows) {
            int cIndex = 0;
            while (cIndex < numStructCols) {
                    if (structAry[rIndex][cIndex] > 0) {
                        outAry[iOffset + rIndex][jOffset + cIndex] = 1;
                    }
                cIndex++;;
                }
            rIndex++;
            }
        }

    void onePixelErosion(int i, int j, int** inAry, int** outAry) {
        int iOffset = i - rowOrigin;
        int jOffset = j - colOrigin;

        bool matchFlag = true;
        int rIndex = 0;
        while (matchFlag == true && rIndex < numStructRows) {
            int cIndex = 0;
            while (matchFlag == true && cIndex < numStructCols) {
                if (structAry[rIndex][cIndex] > 0 && inAry[iOffset + rIndex][jOffset + cIndex] <= 0) {
                    matchFlag = false;
                }
                cIndex++;
            }
            rIndex++;
        }
        if (matchFlag == true) {
            outAry[i][j] = 1;
        } else {
            outAry[i][j] = 0;
        }
    }
    void ComputeOpening(int** inAry, int** outAry, int** tempAry){
        ComputeErosion(inAry, tempAry);
        ComputeDialation(tempAry, outAry);
    }

    void ComputeClosing(int** inAry, int** outAry, int** tempAry){
        ComputeDialation(inAry, tempAry);
        ComputeErosion(tempAry, outAry);
    }

    void AryToFile(int** Ary, ofstream& outFile) {
            outFile << numImgRows << " " << numImgCols << " " << imgMin << " " << imgMax << endl;
        for (int i = rowFrameSize; i < rowSize - rowFrameSize; i++) {
            for (int j = colFrameSize; j < colSize - colFrameSize; j++) {
                outFile << Ary[i][j] << " ";
            }
            outFile << endl;
            }
        }
    
    void prettyPrint(int** Ary, ofstream& outFile, int numRows, int numCols,string printing,  bool isStructElem = false){
        outFile << printing << endl;
        outFile << numRows << " " << numCols << " " << imgMin << " " << imgMax << endl;
        if (isStructElem) {
            outFile << rowOrigin << " " << colOrigin << endl;
        }
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (Ary[i][j] == 0) {
                    outFile << ". ";
                } else {
                    outFile << "1 ";
                }
            }
            outFile << endl;
        }
        outFile << endl;
    }
};

int main(int argc, const char * argv[]) {
    
    if(argc < 8){
        return 0;
    }
    
    string input1 = argv[1];
    string input2 = argv[2];
    string dilateOutFile = argv[3];
    string erodeOutFile = argv[4];
    string closingOutFile = argv[5];
    string openingOutFile = argv[6];
    string prettyPrintFile = argv[7];
    
    ifstream in1(input1);
    ifstream in2(input2);
    
    ofstream dilateOutFiles(dilateOutFile);
    ofstream erodeOutFiles(erodeOutFile);
    ofstream closingOutFiles(closingOutFile);
    ofstream openingOutFiles(openingOutFile);
    ofstream prettyPrintFiles(prettyPrintFile);
    
    if(!in1){
        cout << "no file!" << endl;
        return 1;
    }
    
    if(!in2){
        cout << "no structure file" << endl;
        return 1;
    }
    
    int numImgRows, numImgCols, imgMin, imgMax;
    in1 >> numImgRows >> numImgCols >> imgMin >> imgMax;
    
    int numStructRows, numStructCols, structMin, structMax, rowOrigin, colOrigin;
    in2 >> numStructRows >> numStructCols >> structMin >> structMax;
    in2 >> rowOrigin >> colOrigin;
    
    morphology img(numImgRows, numImgCols, imgMin, imgMax);
    img.setStructuringElement(numStructRows, numStructCols, structMin, structMax, rowOrigin, colOrigin);
    img.initialize();
    img.zero2DAry(img.zeroFrameAry, img.rowSize, img.colSize);
    img.loadImg(in1, img.zeroFrameAry);
    img.prettyPrint(img.zeroFrameAry, prettyPrintFiles, img.rowSize, img.colSize, "Data 1: ");
    
     img.zero2DAry(img.structAry, img.numStructRows, img.numStructCols);
     img.loadStruct(in2, img.structAry);
     img.prettyPrint(img.structAry, prettyPrintFiles, img.numStructRows, img.numStructCols, "Structuring element: ", true);

    img.zero2DAry(img.morphAry, img.rowSize, img.colSize);
     img.ComputeDialation(img.zeroFrameAry, img.morphAry);
     img.AryToFile(img.morphAry, dilateOutFiles);
     img.prettyPrint(img.morphAry, prettyPrintFiles, img.rowSize, img.colSize, "Dialated Image: ");
    
 
    img.zero2DAry(img.morphAry, img.rowSize, img.colSize);
     img.ComputeErosion(img.zeroFrameAry, img.morphAry);
     img.AryToFile(img.morphAry, erodeOutFiles);
    img.prettyPrint(img.morphAry, prettyPrintFiles, img.rowSize, img.colSize, "Eroded Image: ");

    img.zero2DAry(img.morphAry, img.rowSize, img.colSize);
     img.ComputeOpening(img.zeroFrameAry, img.morphAry, img.tempAry);
     img.AryToFile(img.morphAry, openingOutFiles);
     img.prettyPrint(img.morphAry, prettyPrintFiles, img.rowSize, img.colSize, "Opening Image: ");

    img.zero2DAry(img.morphAry, img.rowSize, img.colSize);
     img.ComputeClosing(img.zeroFrameAry, img.morphAry, img.tempAry);
     img.AryToFile(img.morphAry, closingOutFiles);
     img.prettyPrint(img.morphAry, prettyPrintFiles, img.rowSize, img.colSize, "Closed Image: ");
    
    //now we close all the files...
    in1.close();
    in2.close();
    dilateOutFiles.close();
    erodeOutFiles.close();
    closingOutFiles.close();
    openingOutFiles.close();
    prettyPrintFiles.close();
    cout << "Done!" << endl;
    return 0;
}
