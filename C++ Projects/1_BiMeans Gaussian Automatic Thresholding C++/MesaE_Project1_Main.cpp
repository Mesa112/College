#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <limits>

using namespace std;

class thresholdSelection {
public:
    int numRows, numCols, minVal, maxVal;
    int BiGaussThrVal;
    int histHeight;
    int maxHeight;
    int *histAry;
    int *GaussAry;
    int *bestFitGaussAry;
    char **Graph;

     thresholdSelection(ifstream &input1)
    {
        histHeight = loadHist(input1);
        GaussAry = new int[maxVal + 1]();
        bestFitGaussAry = new int[maxVal + 1]();
        initGraph();
    }

    void initGraph()
    {
        int x = maxVal + 1;
        int y = histHeight + 1;

        Graph = new char *[x];
        for (int i = 0; i < x; i++)
        {
            Graph[i] = new char[y];
        }

        // Initialize each element of the array
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                Graph[i][j] = ' ';
            }
        }
    }

    void printGraph(ofstream &outFile)
    {
        int x = maxVal + 1;
        int y = histHeight + 1;
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                outFile << Graph[i][j];
            }
            outFile << endl;
        }
    }

    int loadHist(ifstream &input1)
    {
        int index;
        int value;
        int max = 0;
        input1 >> numRows >> numCols >> minVal >> maxVal;
        histAry = new int[maxVal + 1]();
        for (int i = 0; i < maxVal + 1; i++)
        {
            input1 >> index;
            input1 >> value;
            histAry[index] = value;
            if (value >= max)
                max = value;
        }
        return max;
    }

    void dispHist(ofstream &outFile)
    {
        // print histogram header
        outFile << numRows << " " << numCols << " " << minVal << " " << maxVal << " " << endl;

        for (int i = 0; i < maxVal + 1; i++)
        {
            outFile << i << " (" << histAry[i] << "):";
            for (int j = 0; j < histAry[i]; j++)
            {
                outFile << "+";
            }
            outFile << endl;
        }
    }

    void plotHist()
    {
        // plot Hist into Graph
        for (int i = 0; i < maxVal + 1; i++)
        {
            for (size_t j = 0; j < histAry[i]; j++)
            {
                Graph[i][j] = '+';
            }
        }
    }

    double computeMean(int leftIndex, int rightIndex, ofstream &deBugFile)
    {
        // debug info
        deBugFile << "Entering computeMean method" << endl;
        // stars
        int index = leftIndex;
        double sum = 0.0;
        int numPixels = 0;
        double result = 0.0;
        maxHeight = 0;

        while (index < rightIndex)
        {
            sum += histAry[index] * index;
            numPixels += histAry[index];
            if (histAry[index] >= maxHeight)
            {
                maxHeight = histAry[index];
                cout << "maxHeight= " << maxHeight << endl;
            }
            cout << "inedx=" << index << " sum=" << sum << " numPixels=" << numPixels << " histAry[index]=" << histAry[index] << endl;
            index++;
        }
        result = sum / numPixels;
        deBugFile << "Leaving computeMean method maxHight=" << maxHeight << " and result=" << result << endl;
        return result;
    }

    double computeVar(int leftIndex, int rightIndex, double mean, ofstream &deBugFile)
    {
        // debug info
        deBugFile << "Entering computeVar method" << endl;
        // stars
        int index = leftIndex;
        double sum = 0.0;
        int numPixels = 0;
        double result = 0.0;

        while (index < rightIndex)
        {
            sum += (double)histAry[index] * pow(((double)index - mean), 2);
            numPixels += histAry[index];
            cout << "inedx="<<index<<" sum=" << sum<< " numPixels=" << numPixels << " histAry[index]=" << histAry[index] << endl;
            index++;
        }

        result = sum / (double)numPixels;
        deBugFile << "Leaving computeVar method returning result=" << result << endl;
        return result;
    }

    double modifiedGauss(int x, double mean, double var)
    {
        return (double)(maxHeight * exp(-(pow((double)x - mean, 2) / (2 * var))));
    }

    void plotGaussCurves()
    {
    }

    void setZero()
    {
        // for GaussAry
        for (int i = 0; i < maxVal + 1; i++)
        {
            GaussAry[i] = 0;
        }
    }

    void copyArys(int *Ary1, int *Ary2)
    {
        for (int i = 0; i < maxVal + 1; i++)
        {
           Ary2[i] = Ary1[i];
        }
    }

    double fitGauss(int leftIndex, int rightIndex, ofstream &deBugFile)
    {
        deBugFile << "Entering fitGauss method" << endl;
        double mean;
        double var;
        double sum = 0.0;
        double Gval;
        mean = computeMean(leftIndex, rightIndex, deBugFile);
        var = computeVar(leftIndex, rightIndex, mean, deBugFile);
        int index = leftIndex;

        while (index <= rightIndex)
        {
            Gval = modifiedGauss(index, mean, var);
            sum += abs(Gval - (double)histAry[index]);
            GaussAry[index] = Gval;
            index++;
        }
        deBugFile << "“leaving fitGauss method, sum is=" << sum << endl;
        return sum;
    }

    int biGaussian(ofstream &deBugFile)
    {
        deBugFile << "Entering biGaussian method" << endl;
        double sum1;
        double sum2;
        double total;
        double minSumDiff;
        int offSet = (int)(maxVal - minVal) / 10;
        int dividePt = offSet;
        int bestThr = dividePt;
        minSumDiff = 99999;

        while (dividePt < (maxVal - offSet))
        {
            setZero();
            sum1 = fitGauss(0, dividePt, deBugFile);
            sum2 = fitGauss(dividePt, maxVal, deBugFile);
            total = sum1 + sum2;
            if (total < minSumDiff)
            {
                minSumDiff = total;
                bestThr = dividePt;
                copyArys(GaussAry, bestFitGaussAry);
            }
            deBugFile << "in biGaussian method, ";
            deBugFile << "dividePt = " << dividePt
                      << ", sum1 = " << sum1
                      << ", sum2 = " << sum2
                      << ", total = " << total
                      << ", minSumDiff = " << minSumDiff
                      << ", bestThr = " << bestThr
                      << endl;
            dividePt++;
        }
        deBugFile << "leaving biGaussian method, minSumDiff=" << minSumDiff
                  << "bestThr= " << bestThr
                  << endl;
        return bestThr;
    }

    void plotGaussCurves(ofstream &deBugFile)
    {
        deBugFile << "Entering plotGaussCurves method" << endl;
        int index = 0;
        int end1;
        int end2;

        while (index <= maxVal)
        {
            if (bestFitGaussAry[index] <= histAry[index])
            {
                end1 = bestFitGaussAry[index];
                end2 = histAry[index];
            }
            else
            {
                end1 = histAry[index];
                end2 = bestFitGaussAry[index];
            }
            int i = end1;
            while (i <= end2)
            {
                Graph[index][i] = '#';
                i++;
            }
            Graph[index][bestFitGaussAry[index]] = '*';
            index++;
        }
        deBugFile << "“leaving plotGaussCurves()" << endl;
    }
};

int main(int argc, char const *argv[])
{
    ifstream inFile(argv[1]);
    ofstream outFile1(argv[2]);
    ofstream outFile2(argv[3]);
    ofstream deBugFile(argv[4]);


    thresholdSelection a(inFile);
    outFile1 << "In main (), below is the input histogram" << endl;
    a.dispHist(outFile1);
    a.plotHist();
    deBugFile << "In main (), below is the Graph after plotting the histogram onto Graph" << endl;
    a.printGraph(deBugFile);
    int BiGaussThrVal = a.biGaussian(deBugFile);
    outFile2 << "The BiGaussThrVal is " << BiGaussThrVal << endl;
    a.plotGaussCurves(deBugFile);
    outFile2 << "In main(). Below is the graph showing the histogram, the best fitted Gaussian curves and the gap" << endl;
    a.printGraph(outFile2);
    return 0;
}
