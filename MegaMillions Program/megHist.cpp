#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include <iomanip>
#include <cstring>



using namespace std;

class Histogram {
public:
    int numRows;
    int numCols;
    int* histReg;
    int* histSp;
    int* histWinnerReg; // New histogram for regular numbers when index p[I][7] contains 'winner'
    int* histWinnerSp;  // New histogram for Mega Ball numbers when index p[I][7] contains 'winner'
    string** file;      // Changed to string to handle 'winner' flag

    Histogram(int numRows, int numCols) :
    numRows(numRows), numCols(numCols) {
        initialize();
    }
    void initialize() {
        file = new string*[numRows];
        for (int i = 0; i < numRows; i++) {
            file[i] = new string[numCols];
        }

        histReg = new int[71](); // For numbers 1-70
        histSp = new int[26]();  // For Mega Ball numbers 1-25
        histWinnerReg = new int[71](); // For numbers 1-70 when index p[I][7] contains 'winner'
        histWinnerSp = new int[26]();  // For Mega Ball numbers 1-25 when index p[I][7] contains 'winner'
    }

    void zero2d() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                file[i][j] = "0";
            }
        }
    }
    
    void readFile(ifstream& inFile) {
        string line;
        int row = 0;

        while (getline(inFile, line) && row < numRows) {
            stringstream ss(line);
            string num;
            int col = 0;
            while (ss >> num) {
                file[row][col++] = num;
            }
            row++;
        }
    }

    bool isNumber(const string& s) {
        return !s.empty() && find_if(s.begin(), s.end(), [](unsigned char c) { return !isdigit(c); }) == s.end();
    }

    void storeHistForRegNums() {
        for (int i = 0; i < numRows; i++) {
            bool isWinner = (file[i][numCols - 1] != "Roll");
            for (int j = 0; j < 5; j++) { // Explicitly handle regular numbers in columns 0 to 4
                if (isNumber(file[i][j])) {
                    int number = stoi(file[i][j]);
                    if (number > 0 && number <= 70) {
                        histReg[number]++;
                        if (isWinner) {
                            histWinnerReg[number]++;
                        }
                    }
                }
            }
        }
    }

    void storeHistForSpNums() {
        for (int i = 0; i < numRows; i++) {
            bool isWinner = (file[i][numCols - 1] != "Roll");
            if (isNumber(file[i][5])) { // Mega Ball number is in column 5
                int number = stoi(file[i][5]);
                if (number > 0 && number <= 25) {
                    histSp[number]++;
                    if (isWinner) {
                        histWinnerSp[number]++;
                    }
                }
            }
        }
    }

    void writeHistogram(ofstream& outFile, int* hist, int size) {
        for (int i = 1; i < size; i++) { // Start from 1 because lottery numbers start from 1
            outFile << setw(2) << i << ": ";
            for (int j = 0; j < hist[i]; j++) {
                outFile << '*';
            }
            outFile << endl;
        }
    }
    
    /*void writeNumericalHist(ofstream& outFile, int* hist, int size){
        for (int i = 1; i < size; i++) { // Start from 1 because lottery numbers start from 1
            outFile << setw(2) << i << ": ";
            int val = 0;
            for (int j = 0; j < hist[i]; j++) {
                val++;
            }
            outFile << val;
            outFile << endl;
        }
    }*/
};

int main() {
    ifstream inFile("/Users/estebanm/Desktop/RandomNum/mega_millions_winning_numbers.txt");
    ofstream regHist("/Users/estebanm/Desktop/RandomNum/regHist.txt");
    ofstream spHist("/Users/estebanm/Desktop/RandomNum/spHist.txt");
    ofstream winnerRegHist("/Users/estebanm/Desktop/RandomNum/winnerRegHist.txt");
    ofstream winnerSpHist("/Users/estebanm/Desktop/RandomNum/winnerSpHist.txt");
    /*ofstream regNumericalHist("/Users/estebanm/Desktop/RandomNum/winnerNumericalHist.txt");
    ofstream NumericalSpHist("/Users/estebanm/Desktop/RandomNum/winnerNumericalSpHist.txt");*/

    if (!inFile.is_open()) {
        cerr << "Error opening file" << endl;
        return 1;
    }

    // Dynamically count the number of rows
    int numRows = 694; // Update this based on your actual number of rows

    int numCols = 7; // 5 regular numbers + 1 Mega Ball number + 1 extra column for the winner flag

    Histogram hist(numRows, numCols);
    hist.initialize();

    hist.zero2d();
    hist.readFile(inFile);

    hist.storeHistForRegNums();
    hist.storeHistForSpNums();

    hist.writeHistogram(regHist, hist.histReg, 71);
    hist.writeHistogram(spHist, hist.histSp, 26);
    hist.writeHistogram(winnerRegHist, hist.histWinnerReg, 71);
    hist.writeHistogram(winnerSpHist, hist.histWinnerSp, 26);
    /*hist.writeNumericalHist(regNumericalHist, hist.histReg, 71);
    hist.writeNumericalHist(NumericalSpHist, hist.histSp, 26);*/
    

    cout << "Histograms created in regHist.txt, spHist.txt, winnerRegHist.txt, and winnerSpHist.txt" << endl;

    return 0;
}
