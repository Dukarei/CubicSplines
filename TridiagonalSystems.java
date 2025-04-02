/*
* Dukarei Abbott 
* 3/24/25 
* CS 479
* Tridiagonal Systems  
*
* Solves an input tridiagonal system of 4 vectors: sub/super/main diag and the constants. 
*/
import java.util.*;
public class TridiagonalSystems{

    public static double[][] gaussianElimination(double[] consts,double[] sub,double[] main,double[] sup, int n) throws Exception{
	//idea is this function will return the double array with arr[0] = cPrime vector and arr[1] = dPrime vector as given by ThomasAlgo
	//basic error checking to avoid stupid mistakes
	if(main.length != n) throw new Exception("Main diag does not have n elements");
	if(consts.length != n)throw new Exception("Consts does not have n elements");
	if(sub.length != n-1)throw new Exception("Sub diag does not have n-1 elements");
	if(sup.length != n-1)throw new Exception("Super diag does not have n-1 elements");
	double[][] arr = new double[2][n];
	//preliminary step initializing cPrime and dPrime for our loop
	arr[0][0] = sup[0]/main[0];
	arr[1][0] = consts[0]/main[0];
	int i;
	for( i = 1; i < n-1; i++){
	    arr[0][i] = sup[i]/(main[i] - sub[i-1]*arr[0][i-1]);
	    arr[1][i] = (consts[i] - sub[i-1]*arr[1][i-1])/(main[i] - sub[i- 1]*arr[0][i-1]);
	}
	arr[1][n-1] = consts[n-1]; 
	for(int k = 0; k < main.length-1; k++){
	    main[k] = 1;
	    sup[k] = arr[0][k];
	    consts[k] = arr[1][k];
	    if(equals(main[k],0)||equals(consts[k],0)||equals(sup[k],0)) throw new Exception("singular system");
	}
	main[n-1] = main[n-1] - sub[n-2]*arr[0][n-2];
	consts[n-1] = consts[n-1] - sub[n-2]*arr[1][n-2];
	if(equals(main[n-1],0))throw new Exception("singular system");
	return arr;
    }

    public static double[] backSubstitution(double[] consts,double[] main, double[] sup, int n){
	double[] solution = new double[n];
	solution[n-1] = consts[n-1]/main[n-1];
	for(int i = n-2;i>=0;i--){
	    solution[i] = consts[i] - sup[i]*solution[i+1];
	}
	return solution;
    }
    static boolean equals(double a, double b){
	if(Math.abs(a-b) <= 0.000001) return true;
	else return false;
    }
    public static void main(String[] args){
	double[] sub = {1,2,3.5,-1,2,1.2,1.5,0.8,1.2};
	double[] sup = {1,2,3.5,-1,2,1.2,1.5,0.8,1.2};
	double[] main = {3,-2.2,1.5,-1,5,4.2,-1.5,2.8,0.2,3.9};
	double[] consts = {5,1,-4.5,1,2.5,6.2,5.5,-0.4,1.2,5.2};
	int n = 10;
	/*
	double[] sub = {1,1,1,1,1};
	double[] sup = {1,1,1,1,1};
	double[] main = {4,4,4,4,4,4};
	double[] consts = {5,6,6,6,6,5};
	int n = 6;
	*/
	/*
	double [] consts= {1,1,5};
	double [] sup= {-1,3};
	double [] main={1,2,2};
	double [] sub= {1,2};
	int n = 3;
	*/
	try{
	double[][] arr = gaussianElimination(consts, sub, main, sup, n); 
	System.out.println("cPrime: " + Arrays.toString(arr[0]));
	System.out.println("dPrime: " + Arrays.toString(arr[1]));
	System.out.println("main: " + Arrays.toString(main));
	System.out.println("consts: " + Arrays.toString(consts));
	System.out.println("super: " + Arrays.toString(sup));
	System.out.println("solution: " + Arrays.toString(backSubstitution(consts, main, sup, n)));
	
	}
	catch(Exception e){
	    System.out.println(e);
	}
    }
}
