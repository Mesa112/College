#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <climits>


using namespace std;

struct property{
    int label;
    int numPixels;
    int minR;
    int minC;
    int maxR;
    int maxC;
    
};
class ccLabel{
public:
    int numRows = 0, numCols = 0, minVal = 0, maxVal = 0;
    int newLabel = 0, trueNumCC = 0, newMin = 0, newMax  = trueNumCC;
    
    ccLabel(int numRows, int numCols, int minVal, int maxVal, string deBugFileName):
    numRows(numRows), numCols(numCols), minVal(minVal), maxVal(maxVal) {
        debugFile.open(deBugFileName);
        if (!debugFile.is_open()) {
            cerr << "Failed to open debug" << endl;
        }else {
            debugFile << "starting to debug" << endl;
        }
        
    }
    
    int** zeroFrameAry;
    property* CCproperty;
    ofstream debugFile;
    
    
    void initialize(){
        zeroFrameAry = new int*[numRows+2];
        for(int i = 0; i < numRows+2; i++) {
            zeroFrameAry[i] = new int[numCols + 2];
        }
    }
    
    void zero2DAry(int** Ary,int nRows, int nCols){
        for(int i = 0; i < nRows; i++){
            for(int j = 0; j < nCols; j++){
                Ary[i][j] = 0;
            }
        }
    }
    
    void negative1D(int* Ary, int size){
        for(int i = 0; i < size; i++){
            Ary[i] = -1;
        }
    }
    
    void loadImg(ifstream& file, int** inAry) {
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                file >> inAry[i][j];
            }
        }
    }
    
    void conversion(int** inAry) {
        for (int i = 1; i < numRows; i++) {
            for (int j = 1; j < numCols; j++) {
                if (inAry[i][j] == 0) {
                    inAry[i][j] = 1;
                } else {
                    inAry[i][j] = 0;
                }
            }
        }
    }
    
    void imageReformat(int** Ary, ofstream& outFile) {
        cout << endl << endl;
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                if (Ary[i][j] == 0) {
                    outFile << ". ";
                } else {
                    outFile << Ary[i][j] << " ";
                }
            }
            outFile << endl;
        }
        outFile << endl;
    }
    void connect8Pass1(int** inAry, int* EQAry, int& newLabel){
        debugFile << "Entering connected8Pass1   method" << endl;
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                if (inAry[i][j] > 0) {
                    int a = inAry[i - 1][j - 1];
                    int b = inAry[i - 1][j];
                    int c = inAry[i - 1][j + 1];
                    int d = inAry[i][j - 1];
                    
                    int minLabel = INT_MAX;
                    for (int neighbor : {a, b, c, d}) {
                        if (neighbor > 0 && EQAry[neighbor] < minLabel) {
                            minLabel = EQAry[neighbor];
                        }
                    }
                    
                    if (minLabel == INT_MAX) { // so this would be case 1
                        newLabel++;
                        inAry[i][j] = newLabel;
                        EQAry[newLabel] = newLabel;
                    } else { // for case 2 and 3 ._.
                        inAry[i][j] = minLabel;
                        for (int neighbor : {a, b, c, d}) {
                            if (neighbor > 0 && EQAry[neighbor] != minLabel) {
                                EQAry[neighbor] = minLabel;
                            }
                        }
                    }
                }
            }
        }
        debugFile << "After connected8 pass1, newLabel = " << newLabel << endl;
    }
    
    void connect8Pass2(int** inAry, int* EQAry) {
        debugFile << "Entering connect8Pass2 method." << endl;
        for (int i = numRows; i >= 0; i--) {
            for (int j = numCols; j >= 0; j--) {
                if (inAry[i][j] > 0) {
                    int e = inAry[i + 1][j + 1];
                    int f = inAry[i + 1][j];
                    int g = inAry[i + 1][j - 1];
                    int h = inAry[i][j + 1];
                    int currentLabel = EQAry[inAry[i][j]];
                    vector<int> neighbors = {EQAry[e], EQAry[f], EQAry[g], EQAry[h], currentLabel};
                    int minLabel = INT_MAX;
                    for (int neighborLabel : neighbors) {
                        if (neighborLabel < minLabel) {
                            minLabel = neighborLabel;
                        }
                    }
                    if (currentLabel != minLabel) {
                        inAry[i][j] = minLabel;
                        EQAry[currentLabel] = minLabel;
                    }
                }
            }
        }
        debugFile << "After connect8Pass2." << endl;
    }
    void connect4Pass1(int** inAry, int* EQAry, int& newLabel){
        debugFile << "Entering connect4Pass1 method." << endl;
        
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                if (inAry[i][j] > 0) {
                    int b = inAry[i-1][j];
                    int d = inAry[i][j-1];
                    
                    if (b == 0 && d == 0) {
                        newLabel++;
                        inAry[i][j] = newLabel;
                        EQAry[newLabel] = newLabel;
                    } else {
                        int minLabel = INT_MAX;
                        if (b > 0) minLabel = min(minLabel, EQAry[b]);
                        if (d > 0) minLabel = min(minLabel, EQAry[d]);
                        
                        inAry[i][j] = minLabel;
                        
                        if (b > 0 && EQAry[b] != minLabel) {
                            EQAry[EQAry[b]] = minLabel;
                        }
                        if (d > 0 && EQAry[d] != minLabel) {
                            EQAry[EQAry[d]] = minLabel;
                        }
                    }
                }
            }
        }
        
        debugFile << "After connect4Pass1, newLabel = " << newLabel << endl;
    }
    
    void connect4Pass2(int** inAry, int* EQAry) {
        debugFile << "Entering connect4Pass2 method." << endl;
        for (int i = numRows; i >= 1; i--) {
            for (int j = numCols; j >= 1; j--) {
                if (inAry[i][j] > 0) {
                    int currentLabel = inAry[i][j];
                    int rootLabel = currentLabel;
                    while (EQAry[rootLabel] != rootLabel) {
                        rootLabel = EQAry[rootLabel];
                    }
                    inAry[i][j] = rootLabel;
                    if (currentLabel != rootLabel) {
                        EQAry[currentLabel] = rootLabel;
                    }
                }
            }
        }
        debugFile << "After connect4Pass2." << endl;
    }
    
    void connectPass3(int** inAry, int* EQAry) {
        debugFile << "Entering connectPass3 method." << endl;
        CCproperty = new property[trueNumCC + 1];
        for (int i = 1; i <= trueNumCC; i++) {
            CCproperty[i].label = i;
            CCproperty[i].numPixels = 0;
            CCproperty[i].minR = INT_MAX;
            CCproperty[i].minC = INT_MAX;
            CCproperty[i].maxR = 0;
            CCproperty[i].maxC = 0;
        }
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int currentLabel = inAry[i][j];
                int trueLabel = EQAry[currentLabel];
                inAry[i][j] = trueLabel;
                CCproperty[trueLabel].numPixels += 1;
                CCproperty[trueLabel].minR = min(CCproperty[trueLabel].minR, i);
                CCproperty[trueLabel].minC = min(CCproperty[trueLabel].minC, j);
                CCproperty[trueLabel].maxR = max(CCproperty[trueLabel].maxR, i);
                CCproperty[trueLabel].maxC = max(CCproperty[trueLabel].maxC, j);
            }
        }
        debugFile << "Leaving connectPass3 method." << endl;
    }
    
    void updateEQ(int* EQAry, vector<int>& nonZeroNeighborAry, int minLabel) {
        for (int label : nonZeroNeighborAry) {
            if (label > 0) {
                EQAry[label] = minLabel;
            }
        }
    }
    
    int manageEQAry(int* EQAry, int size) {
        debugFile << "Entering manageEQAry method." << endl;
        
        // First, compress paths for each label to its root label.
        for (int i = 1; i <= size; ++i) {
            int current = i;
            while (EQAry[current] != current) {
                EQAry[current] = EQAry[EQAry[current]]; // Path compression
                current = EQAry[current];
            }
        }
        
        int labelCount = 0;
        for (int i = 1; i <= size; ++i) {
            if (EQAry[i] == i) {
                ++labelCount;
                EQAry[i] = labelCount;
            } else {
                EQAry[i] = EQAry[EQAry[i]];
            }
        }
        
        debugFile << "After consolidation, trueNumCC = " << labelCount << endl;
        debugFile << "Leaving manageEQAry method." << endl;
        
        return labelCount;
    }
    
    
    void printCCproperty(const property* CCproperty, int trueNumCC, ofstream& propertyFile) {
        for (int i = 1; i <= trueNumCC; i++) {
            propertyFile << "Label: " << CCproperty[i].label << ", "
            << "NumPixels: " << CCproperty[i].numPixels << ", "
            << "MinR: " << CCproperty[i].minR << ", "
            << "MinC: " << CCproperty[i].minC << ", "
            << "MaxR: " << CCproperty[i].maxR << ", "
            << "MaxC: " << CCproperty[i].maxC << endl;
        }
    }
    
    void printEQAry(int* EQAry, int newLabel, ofstream& outFile) {
        outFile << "Equivalency Table after:" << endl;
        for (int i = 1; i <= newLabel; i++) {
            outFile << i << " ";
        }
        outFile << endl;
        for (int i = 1; i <= newLabel; i++) {
            outFile << EQAry[i] << " ";
        }
        outFile << endl << endl;
    }
    
    
    void drawBoxes(int** zeroFrameAry, const property* CCproperty, int trueNumCC) {
        for (int i = 1; i <= trueNumCC; i++) {
            for (int r = CCproperty[i].minR; r <= CCproperty[i].maxR; r++) {
                zeroFrameAry[r][CCproperty[i].minC] = CCproperty[i].label; // Left vertical
                zeroFrameAry[r][CCproperty[i].maxC] = CCproperty[i].label; // Right vertical
            }
            for (int c = CCproperty[i].minC; c <= CCproperty[i].maxC; c++) {
                zeroFrameAry[CCproperty[i].minR][c] = CCproperty[i].label; // Top horizontal
                zeroFrameAry[CCproperty[i].maxR][c] = CCproperty[i].label; // Bottom horizontal
            }
        }
    }
    
    void printImg(int** zeroFrameAry, int numRows, int numCols, ofstream& labelFile) {
        debugFile << "Printing the final labeled image to the labelFile." << endl;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                labelFile << zeroFrameAry[i][j] << " ";
            }
            labelFile << endl;
        }
        debugFile << "Finished printing the labeled image." << endl;
    }
    ~ccLabel() {
        if (debugFile.is_open()) {
            debugFile << "Debugging ended." << endl;
            debugFile.close();
        }
    }
    
    
};
int main(int argc, char* argv[]) {
    if(argc < 8){
        return -1;
    }
    string inFile =  "/Users/estebanm/Documents/PROJECT 4/data1.txt";
    int conectnes = stoi(argv[2]);
    char option = argv[3][0];
    string outputDir = "/Users/estebanm/Documents/PROJECT 4/";
    string RFprettyPrintFile = outputDir + argv[4];
    string labelFile = outputDir + argv[5];
    string properyFile = outputDir + argv[6];
    string deBugFile = outputDir + argv[7];
    
    ifstream input(inFile);
    
    ofstream RFprettyPrintFiles(RFprettyPrintFile);
    ofstream labelFiles(labelFile);
    ofstream properyFiles(properyFile);
    ofstream deBugFiles(deBugFile);
    
    
    int numRows, numCols, minVal, maxVal;
    input >> numRows >> numCols >> minVal >> maxVal;
    
    ccLabel connect(numRows, numCols, minVal, maxVal, deBugFile);
    
    connect.initialize();
    connect.loadImg(input, connect.zeroFrameAry);
    if(tolower(option) == 'y'){
        connect.conversion(connect.zeroFrameAry);
    }
    int size = (connect.numRows * connect.numCols) / 4;
    int* EQAry = new int[size];
    connect.negative1D(EQAry, size);
    if(conectnes == 4){
        connect.debugFile << "Entering connect4 method." << endl;
        connect.connect4Pass1(connect.zeroFrameAry, EQAry, connect.newLabel);
        connect.imageReformat(connect.zeroFrameAry, RFprettyPrintFiles);
        connect.printEQAry(EQAry, connect.newLabel, RFprettyPrintFiles);
        connect.connect4Pass2(connect.zeroFrameAry, EQAry);
        connect.imageReformat(connect.zeroFrameAry, RFprettyPrintFiles);
        connect.printEQAry(EQAry, connect.newLabel, RFprettyPrintFiles);
        connect.manageEQAry(EQAry, size);
        connect.trueNumCC = connect.manageEQAry(EQAry, connect.newLabel);
        connect.newMin = 0;
        connect.newMax = connect.trueNumCC;
        connect.CCproperty = new property[connect.trueNumCC + 1];
        connect.debugFile << "In connected4, after manage EQAry, trueNumCC = " << connect.trueNumCC << endl;
        connect.connectPass3(connect.zeroFrameAry, EQAry);
        connect.imageReformat(connect.zeroFrameAry, RFprettyPrintFiles);
        connect.printEQAry(EQAry, connect.newLabel, RFprettyPrintFiles);
        connect.printImg(connect.zeroFrameAry, connect.numRows, connect.numCols, labelFiles);
        connect.printCCproperty(connect.CCproperty, connect.trueNumCC, properyFiles);
        RFprettyPrintFiles << "Total number of connected components: " << connect.trueNumCC << endl;
        connect.debugFile << "Leaving connected4 method" << endl;
    }else if (conectnes  == 8){
        connect.connect8Pass1(connect.zeroFrameAry, EQAry, connect.newLabel);
        connect.imageReformat(connect.zeroFrameAry, RFprettyPrintFiles);
        connect.printEQAry(EQAry, connect.newLabel, RFprettyPrintFiles);
        connect.connect8Pass2(connect.zeroFrameAry, EQAry);
        connect.imageReformat(connect.zeroFrameAry, RFprettyPrintFiles);
        connect.printEQAry(EQAry, connect.newLabel, RFprettyPrintFiles);
        connect.manageEQAry(EQAry, size);
        connect.trueNumCC = connect.manageEQAry(EQAry, connect.newLabel);
        connect.newMin = 0;
        connect.newMax = connect.trueNumCC;
        connect.CCproperty = new property[connect.trueNumCC + 1];
        connect.debugFile << "In connected8, after manage EQAry, trueNumCC = " << connect.trueNumCC << endl;
        connect.connectPass3(connect.zeroFrameAry, EQAry);
        connect.imageReformat(connect.zeroFrameAry, RFprettyPrintFiles);
        connect.printEQAry(EQAry, connect.newLabel, RFprettyPrintFiles);
        connect.printImg(connect.zeroFrameAry, connect.numRows, connect.numCols, labelFiles);
        connect.printCCproperty(connect.CCproperty, connect.trueNumCC, properyFiles);
        RFprettyPrintFiles << "Total number of connected components: " << connect.trueNumCC << endl;
        connect.debugFile << "Leaving connected8 method" << endl;
    }
    cout << "Done" << endl;
    return 0;
}
