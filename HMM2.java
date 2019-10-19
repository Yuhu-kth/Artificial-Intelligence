package com.company;
import java.io.File;
import java.util.*;
public class HMM2 {

    public static void main(String[] args) throws Exception {

        File file = new File("/Users/yuhu/IdeaProjects/HMM/src/com/company/hmm2.in");
        Scanner sc = new Scanner(file);
//        Scanner sc = new Scanner(System.in);
        String transition = sc.nextLine();
        String emission = sc.nextLine();
        String initial = sc.nextLine();
        String sequence = sc.nextLine();
        List<Double> tran = new ArrayList<Double>();
        List<Double> emi = new ArrayList<Double>();
        List<Double> ini = new ArrayList<Double>();
        List<Double> seq = new ArrayList<Double>();
        Scanner scT = new Scanner(transition);
        Scanner scE = new Scanner(emission);
        Scanner scI = new Scanner(initial);
        Scanner scS = new Scanner(sequence);
        while (scT.hasNext()) {
            double ValueOfTransition = Double.valueOf(scT.next());
            tran.add(ValueOfTransition);
        }
        while (scE.hasNext()) {
            double ValueOfEmission = Double.valueOf(scE.next());
            emi.add(ValueOfEmission);
        }
        while (scI.hasNext()) {
            double ValueOfInitial = Double.valueOf(scI.next());
            ini.add(ValueOfInitial);
        }
        while (scS.hasNext()) {
            double ValueOfSequence = Double.valueOf(scS.next());
            seq.add(ValueOfSequence);
        }
        double rowT = tran.get(0), colT = tran.get(1);
        double rowE = emi.get(0), colE = emi.get(1);
        double rowI = ini.get(0), colI = ini.get(1);
        //int SeqNumber = (seq.get(0)).intValue();
        double[][] MatrixTransition = new double[(int) rowT][(int) colT];
        double[][] MatrixEmission = new double[(int) rowE][(int) colE];
        double[] MatrixInitial = new double[(int) colI];
        int[] observation = new int[seq.size()-1];
        for (int i = 0; i < seq.size() - 1; i++) {
            observation[i] = (seq.get(i + 1)).intValue();
        }
        int i = 2;
        for (int r = 0; r < rowT; r++) {
            for (int c = 0; c < colT; c++) {
                MatrixTransition[r][c] = tran.get(i++);
            }
        }
        int j = 2;
        for (int r1 = 0; r1 < rowE; r1++) {
            for (int c1 = 0; c1 < colE; c1++) {
                MatrixEmission[r1][c1] = emi.get(j++);
            }
        }
        int k = 2;
        for (int r2 = 0; r2 < colI; r2++) {
            MatrixInitial[r2] = ini.get(k++);
        }
        //Backward algorithm
        Delta(observation.length,observation, MatrixTransition, MatrixEmission,MatrixInitial);
    }
    static double[][] Delta(int n,int[] observation, double[][] MatrixTransition, double[][] MatrixEmission, double[]Initial) {
        int[] Observation = observation;
        double[][] matrixEmission = MatrixEmission;
        double[][] matrixTransition = MatrixTransition;
        double[] matrixInitial = Initial;
        double[][] backwardProbability = new double[matrixTransition.length][n];
        double[][] Index = new double[matrixTransition.length][n];
        //Initial
        for (int i = 0; i < matrixTransition.length; i++) {
            backwardProbability[i][0] = matrixEmission[i][Observation[0]] * matrixInitial[i];
            Index[i][0]=0;
        }
        for (int x = 1; x < n; x++) {
            for (int i = 0; i < matrixTransition.length; i++) {
                double MaxProba = 0;
                double proba = 0;
                int index = 0;
                for (int j = 0; j < matrixTransition.length; j++) {
                    double A = backwardProbability[j][x - 1];
                    double B = matrixTransition[j][i];
                    double C = matrixEmission[i][Observation[x]];
                    proba = A * B * C;
                    if (MaxProba < proba) {
                        MaxProba = proba;
                        index = j;
                    }
                }
                backwardProbability[i][x] = MaxProba;
                Index[i][x] = index;
            }
        }
        caculatePath(n, backwardProbability, Index);
        return backwardProbability;
    }
   static void caculatePath(int n,double[][]backwardProbability,double[][]Index){
       int[][]path=new int[1][n];
       double maxProba=0;
       for(int i=0;i<backwardProbability.length;i++){
           if(backwardProbability[i][n-1]>maxProba) {
               maxProba = backwardProbability[i][n - 1];
               path[0][n-1]= i;
           }
       }
       for(int t=n-2;t>=0;t--){
           path[0][t]= (int)Index[path[0][t+1]][t+1];
       }
       for(int i=0;i<path[0].length;i++){
           System.out.print(path[0][i]+" ");
       }
   }
}
