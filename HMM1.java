package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class HMM1 {

    public static void main(String[] args) throws Exception {
        //Read file
        //final long startTime = System.nanoTime();
//        File file= new File("/Users/yuhu/IdeaProjects/HMM/src/com/company/hmm1.in");
//        Scanner sc = new Scanner(file);
        Scanner sc = new Scanner(System.in);
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
        int SeqNumber = (seq.get(0)).intValue();
        double[][] MatrixTransition = new double[(int) rowT][(int) colT];
        double[][] MatrixEmission = new double[(int) rowE][(int) colE];
        double[][] MatrixInitial = new double[(int) rowI][(int) colI];
        double[][] observation = new double[(int)rowE][1];
        double[] temp = new double[(int) colI];
       // System.out.println("序列数组："+seq);
        //--------------------------output one col for testing
        int i = 2;
        for (int r = 0; r < rowT; r++) {
            for (int c = 0; c < colT; c++) {
                MatrixTransition[r][c] = tran.get(i++);
            }
        }
        int j = 2;
        for (int r1 = 0; r1  < rowE; r1++) {
            for (int c1 = 0; c1 < colE; c1++) {
                MatrixEmission[r1][c1] = emi.get(j++);
            }
        }
        int k = 2;
        for (int r2 = 0; r2 < rowI; r2++) {
            for (int c2 = 0; c2 < colI; c2++) {
                MatrixInitial[r2][c2] = ini.get(k++);
            }
        }

        //Forward algorithm
        for (int row = 0; row < rowE; row++) {
            //Seq.get(t+1)
            observation[row][0] = MatrixEmission[row][(seq.get(1)).intValue()];
        }
        double[][] prod1 = new double[1][(int) colI];
        for (int p = 0; p < colI; p++) {
            prod1[0][p] = MatrixInitial[0][p] * observation[p][0];
        }
        for(int t=1;t<SeqNumber;t++) {
            for (int row = 0; row < rowE; row++) {
                //Seq.get(t+1)
                observation[row][0] = MatrixEmission[row][(seq.get(t + 1)).intValue()];
            }
            alphaSum(prod1,MatrixTransition);
            for (int p = 0; p < alphaSum(prod1,MatrixTransition)[0].length; p++) {
                temp[p]=( alphaSum(prod1,MatrixTransition)[0][p] )* observation[p][0];
               // System.out.print(temp[p]+ " "+"\n");
            }
            for (int p = 0; p < temp.length; p++) {
                prod1[0][p]=temp[p];
            }

        }
        double sum=0.0;
        for (int p = 0; p < colI; p++) {

            sum+=prod1[0][p];

        }
        System.out.print(sum);

    }
       static double[][] alphaSum(double[][] alphaMinusOne, double[][] A){
           double[][] result=new double[alphaMinusOne.length][alphaMinusOne[0].length];
           //alphaMinusOne=transpose(alphaMinusOne);
        for (int col=0;col<A[0].length;col++){
            double rowSum=0.0;
            for(int row=0;row<A.length;row++){
                rowSum+=A[row][col]*alphaMinusOne[0][row];
            }
            result[0][col]=rowSum;
        }
         return result;
    }
}
