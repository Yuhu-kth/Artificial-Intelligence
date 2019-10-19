package com.company;
import java.io.File;
import java.util.*;
import java.lang.Math;
public class HMM3 {
    private static double[][] MatrixTransition;
    private static double[][] MatrixEmission;
    private static double[] MatrixInitial;
    private static int[] observation;
    private static double oldLogProb=Double.NEGATIVE_INFINITY;
    private static double logProb=Double.NEGATIVE_INFINITY;
    private static int MaxIters=100;
    private static double[][] alpha;
    private static double[][] belt ;
    private static double[] scale;
    private static double[][][]digamma;
    private static double[][]gamma;

    private static void alphaPass(){
        alpha = new double[MatrixInitial.length][observation.length];
        scale=new double[observation.length];
        scale[0]=0.0;
        for (int i = 0; i <MatrixInitial.length; i++) {
            alpha[i][0] = MatrixInitial[i] * MatrixEmission[i][observation[0]];
            scale[0] += alpha[i][0];
        }
        //scale the alpha0
        scale[0]=1/scale[0];
        for (int i = 0; i < MatrixInitial.length; i++) {
            alpha[i][0] = scale[0] * alpha[i][0];
        }
        //compute alpha_t
        for (int t = 1; t < observation.length; t++) {
            scale[t] = 0;
            for (int i = 0; i <MatrixInitial.length; i++) {
                alpha[i][t] = 0;
                for (int j = 0; j <MatrixInitial.length; j++) {
                    alpha[i][t] += MatrixTransition[j][i] * alpha[j][t - 1];
                }
                alpha[i][t] = alpha[i][t] * MatrixEmission[i][observation[t]];
                scale[t] = scale[t] + alpha[i][t];
            }
            scale[t] = 1 / scale[t];
            for (int i = 0; i < MatrixInitial.length; i++) {
                alpha[i][t] = scale[t] * alpha[i][t];
            }
        }
    }
    private static void betaPass(){
        //belt-pass
       belt = new double[MatrixInitial.length][observation.length];
        //initial
        for (int i = 0; i < MatrixInitial.length; i++) {
            belt[i][observation.length- 1] = scale[observation.length- 1];
        }

        for (int T = observation.length- 2; T >=0; T--) {
            for (int i = 0; i < MatrixInitial.length; i++) {
                belt[i][T] = 0;
                for (int j = 0; j < MatrixInitial.length; j++) {
                    belt[i][T] = belt[i][T] + MatrixTransition[i][j] * MatrixEmission[j][observation[T + 1]] * belt[j][T + 1];
                }
                belt[i][T] = scale[T] * belt[i][T];
            }
        }
    }
    private static void ComputeGamma(){
        int t=observation.length;
        //compute di-gamma and gamma
        digamma=new double[MatrixInitial.length][MatrixInitial.length][observation.length];
        gamma=new double[MatrixInitial.length][observation.length];
        for(int T=0;T<t-1;T++){
            double denom=0.0;
            for(int i=0;i<MatrixInitial.length;i++){
                for(int j=0;j<MatrixInitial.length;j++){
                    denom=denom+alpha[i][T]*MatrixTransition[i][j]*MatrixEmission[j][observation[T+1]]*belt[j][T+1];
                }
            }
            for(int i=0;i<MatrixInitial.length;i++){
                gamma[i][T]=0;
                for(int j=0;j<MatrixInitial.length;j++){
                    digamma[i][j][T]=(alpha[i][T]*MatrixTransition[i][j]*MatrixEmission[j][observation[T + 1]]* belt[j][T + 1])/denom;
                    gamma[i][T]=gamma[i][T]+digamma[i][j][T];
                }
            }
        }
        double denom=0.0;
        for(int i=0;i<MatrixInitial.length;i++){
            denom=denom+alpha[i][t-1];
        }
        for(int i=0;i<MatrixInitial.length;i++){
            gamma[i][t-1]=alpha[i][t-1]/denom;
        }
    }
    private static void ComputeLog(){
        //compute logProb
        double Prob=0.0;
        for(int i=0;i<scale.length;i++){
            Prob=Prob+Math.log(scale[i]);
        }
        logProb=-Prob;
    }
    public static void EstimateT(){
        //re-estimate A
        for(int i=0;i<MatrixInitial.length;i++){
            for(int j=0;j<MatrixInitial.length;j++){
                double numer=0.0;
                double denom=0.0;
                for(int T=0;T<observation.length-1;T++){
                    numer=numer+digamma[i][j][T];
                    denom=denom+gamma[i][T];
                }
                MatrixTransition[i][j]=numer/denom;
            }
        }
    }
    public static void EstimateE(){
        //re-estimate B
        for(int i=0;i<MatrixInitial.length;i++){
            for(int j=0;j<MatrixEmission[0].length;j++){
                double numer=0.0;
                double denom=0.0;
                for(int T=0;T<observation.length;T++){
                    if(observation[T]==j){
                        numer =numer+gamma[i][T];
                    }
                    denom=denom+gamma[i][T];
                }
                MatrixEmission[i][j]=numer/denom;
            }
        }
    }
    public static void EstimateI(){
        //re-estimate pi
        for(int i=0;i<MatrixInitial.length;i++){
            MatrixInitial[i]=gamma[i][0];
        }
    }
    private static void Output(){
        int rowT=MatrixTransition.length;
        int colT=MatrixTransition[0].length;
        int rowE=MatrixEmission.length;
        int colE=MatrixEmission[0].length;
        System.out.print(rowT+" "+colT+" ");
        for(int i=0;i<rowT;i++){
            for (int j=0;j<colT;j++){
                System.out.print(String.format("%.6f",MatrixTransition[i][j])+" ");
            }
        }
        System.out.println();
        System.out.print(rowE+" "+colE+" ");
        for(int i=0;i<rowE;i++){
            for (int j=0;j<colE;j++){
                System.out.print(String.format("%.6f",MatrixEmission[i][j])+" ");
            }
        }
    }

    public static void main(String[] args) throws Exception {
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
        double colI = ini.get(1);
        MatrixTransition = new double[(int) rowT][(int) colT];
        MatrixEmission = new double[(int) rowE][(int) colE];
        MatrixInitial = new double[(int) colI];
        observation = new int[seq.size() - 1];
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
        double iters=0;
        while((iters<MaxIters)&&(logProb>=oldLogProb)){
            oldLogProb=logProb;
            iters++;
            alphaPass();
            betaPass();
            ComputeGamma();
            ComputeLog();
            EstimateT();
            EstimateE();
            EstimateI();
        }
        Output();
    }
}
