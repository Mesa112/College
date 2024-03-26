
#include <iostream>
#include <fstream>
#include <sstream>

using namespace std;

class Image {
public:
    int numRows, numCols, minVal, maxVal, thrValue;

    Image(int numRows, int numCols, int minVal, int maxVal, int thrValue) :
        numRows(numRows), numCols(numCols), minVal(minVal), maxVal(maxVal), thrValue(thrValue) {}

    void processing(ifstream& input, ofstream& binaryOut, ofstream& nonBinaryOut) {
        int counter = 0;
        int pixel;
        while (input >> pixel) {
            if (pixel >= thrValue) {
                binaryOut << "1 ";
                nonBinaryOut << pixel << " ";
            } else {
                binaryOut << "0 ";
                nonBinaryOut << "0 ";
            }
            counter++;
            if (counter == numCols) {
                binaryOut << "\n";
                nonBinaryOut << "\n";
                counter = 0;
            }
        }
    }
};
int main(int argc, char* argv[]) {
    if (argc < 5) {
        return 1;
    }

    string inFile = argv[1];
    int thrValue = stoi(argv[2]);
    string outFile1 = argv[3];
    string outFile2 = argv[4];

    ifstream input(inFile);
    ofstream binaryOut(outFile1);
    ofstream nonBinaryOut(outFile2);

    if (!input) {
        cerr << "File not found: " << inFile << endl;
        return 1;
    }

    int numRows, numCols, minVal, maxVal;
    input >> numRows >> numCols >> minVal >> maxVal;

    binaryOut << numRows << " " << numCols << " 0 1\n";
    nonBinaryOut << numRows << " " << numCols << " 0 " << maxVal << "\n";

    Image img(numRows, numCols, minVal, maxVal, thrValue);
    img.processing(input, binaryOut, nonBinaryOut);

    cout << "Done!" << endl;

    return 0;
}
