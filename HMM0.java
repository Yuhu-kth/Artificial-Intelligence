package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class HMM0 {

    public static void main(String[] args) throws Exception{
        //Read file
        //final long startTime = System.nanoTime();
//        File file= new File("/Users/yuhu/IdeaProjects/HMM/src/com/company/test.in");
//        Scanner sc = new Scanner(file);
        Scanner sc = new Scanner(System.in);
        String transition = sc.nextLine();
        String emission = sc.nextLine();
        String initial = sc.nextLine();
        List<Double> tran = new ArrayList<Double>();
        List<Double> emi = new ArrayList<Double>();
        List<Double> ini = new ArrayList<Double>();
        Scanner scT = new Scanner(transition);
        Scanner scE = new Scanner(emission);
        Scanner scI = new Scanner(initial);
        while(scT.hasNext()){
            double ValueOfTransition= Double.valueOf(scT.next());
            tran.add(ValueOfTransition);
        }
        while(scE.hasNext()){
            double ValueOfEmission= Double.valueOf(scE.next());
            emi.add(ValueOfEmission);
        }
        while(scI.hasNext()){
            double ValueOfInitial= Double.valueOf(scI.next());
            ini.add(ValueOfInitial);
        }
        double rowT = tran.get(0),colT = tran.get(1);
        double rowE = emi.get(0),colE = emi.get(1);
        double rowI = ini.get(0),colI = ini.get(1);
        double[][] MatrixTransition = new double[(int)rowT][(int)colT];
        double[][] MatrixEmission = new double[(int)rowE][(int)colE];
        double[][] MatrixInitial = new double[(int)rowI][(int)colI];
        //System.out.println("\n The transition Matrix");

        int i=2;
        for (int r = 0; r < rowT; r++) {
                for (int c = 0; c < colT; c++) {
                    MatrixTransition[r][c]=tran.get(i++);
                }
            }
        int j=2;
        for (int r1 = 0; r1 < rowE; r1++) {
            for (int c1 = 0; c1 < colE; c1++) {
                MatrixEmission[r1][c1]=emi.get(j++);
            }
        }
        int k=2;
        for (int r2 = 0; r2 < rowI; r2++) {
            for (int c2 = 0; c2 < colI; c2++) {
                MatrixInitial[r2][c2]=ini.get(k++);
            }
        }
        double prod1[][]=new double[(int)rowI][(int)colT];
        for(int p=0;p<rowI;p++){
            for(int q=0;q<colT;q++){
                for(int s=0;s<rowT;s++){
                    prod1[p][q]=prod1[p][q]+MatrixInitial[p][s]*MatrixTransition[s][q];
                }
            }
        }
        double prod2[][]=new double[(int)rowI][(int)colE];
        for(int a=0;a<rowI;a++){
            for(int b=0;b<colE;b++){
                for(int c=0;c<rowE;c++){
                    prod2[a][b]=prod2[a][b]+prod1[a][c]*MatrixEmission[c][b];
                }
            }
        }
        System.out.print(rowI+" "+colE+" ");
        for(int a=0;a<rowI;a++){
        for(int b=0;b<colE;b++){
        System.out.print(prod2[a][b] + " ");
        }
        System.out.println();
        }
//        final long duration=System.nanoTime()-startTime;
//        System.out.println(duration);
        System.exit(0);
    }
//       public static double matrix(double row, double col,double arrayList){
//        double[][] Matrix;
//        int i=2;
//        for (int r = 0; r < row; r++) {
//            for (int c = 0; c < col; c++) {
//                Matrix[r][c]=arrayList.get(i++);
//            }
//        }
//       return Matrix[][];
//    }
}







