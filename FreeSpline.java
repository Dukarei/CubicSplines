/*
 * Dukarei Abbott
 * CS 479
 * 4/4/25
 * FreeSpline
 *
 * program to compute a free spline that interpolates index-joined arrays x[0]->x[n] and y[0]->y[n] in the fashion (x[0],y[0])->(x[n],y[n]) where f(x[n]) = y[n] and f(x) is function to be interpolated
 * */
import java.util.Arrays;
public class FreeSpline{
   public static double[][] gaussianElimination(double[] consts,double[] sub,double[] main,double[] sup, int n) throws Exception{
	//idea is this function will return the double array with arr[0] = cPrime vector and arr[1] = dPrime vector as given by ThomasAlgo
	//basic error checking to avoid stupid mistakes
	//also modified main, consts, sub,sup like the methods outlined in the assingment 7 required
	/*
	if(main.length != n) throw new Exception("Main diag does not have n elements");
	if(consts.length != n)throw new Exception("Consts does not have n elements");
	if(sub.length != n-1)throw new Exception("Sub diag does not have n-1 elements");
	if(sup.length != n-1)throw new Exception("Super diag does not have n-1 elements");
	*/
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
	for(int k = 0; k < n-1; k++){
	    main[k] = 1;
	    sup[k] = arr[0][k];
	    consts[k] = arr[1][k];
	    //if(equals(main[k],0)||equals(consts[k],0)||equals(sup[k],0)) throw new Exception("singular system");
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
    static double[][] initMatrix(double[] x, double[] y, double[] h){
	//note that sub and sup have extra indices, but it is not relevant to tridiag calculations
	double[][] arr = new double[4][y.length];
	//init h array for step sizes, presume a array to be y array, init sub/main/super diagonal
	//double[] h = new double[y.length-1];
	arr[1][0] = 0;
	arr[2][0] = 1;
	arr[2][y.length-1] = 1;
	arr[3][y.length-1] = 0;
	/*for(int i = 0; i<h.length; i++){
	    h[i] = x[i+1]-x[i];
	    //System.out.printf("\nh[%d] : %f", i, h[i]);
	}*/
	for(int i = 0; i < y.length-1; i++){
	    if(i > 0) arr[1][i] = h[i];
	    if(i == y.length-2) arr[0][i] = 0;
	    else arr[0][i] = h[i];
	    //statement below makes it simple to evaluate that sub and super diagonals are consistent with the h values outlined in formula
	    //System.out.printf("\nh[%d] : %f, sub[%d] : %f, sup[%d]: %f",i, h[i], i, arr[0][i], i, sup[i]);
	}
	for(int i = 1; i < y.length-1; i++){
	    arr[2][i] = 2*(h[i-1]+h[i]);
	    arr[3][i] = (3/h[i])*(y[i+1]-y[i]) - (3/h[i-1])*(y[i]-y[i-1]); 
	}
//	System.out.println("main: " + Arrays.toString(arr[2]));
//	System.out.println("sub: " + Arrays.toString(arr[0]));
//	System.out.println("sup: " + Arrays.toString(arr[1]));
//	System.out.println("consts: " + Arrays.toString(arr[3]));
	return arr;
    }
    public static void main(String[] args){
//    	double[] x = {0,0.4,1,1.4,2,2.4,3,3.4,4};
//    	double[] y = {-2.4,0.2,2,-0.5,-2.6,0.3,3.5,-1.2,2.4};
	double[] x = {0,1,2,3,4,5};
	double[] y = {0, 0.05, 0.4, 1.35,3.2,6};
//	double[] x = {0,0.4,1,1.4,2,2.4,3,3.4,4};
//	double[] y = {-2.4,0.2,2,-0.5,-2.6,0.3,3.5,-1.2,2.4};
	double[] h = new double[y.length-1];
	for(int i = 0; i<h.length; i++){
		    h[i] = x[i+1]-x[i];
		    //System.out.printf("\nh[%d] : %f", i, h[i]);
	}

	double[][] cSystem = initMatrix(x, y, h);
	/*
	System.out.println("main: " + Arrays.toString(cSystem[2]));
	System.out.println("sub: " + Arrays.toString(cSystem[0]));
	System.out.println("sup: " + Arrays.toString(cSystem[1]));
	System.out.println("consts: " + Arrays.toString(cSystem[3]));
	*/
    try{
	gaussianElimination(cSystem[3], cSystem[0], cSystem[2], cSystem[1], cSystem[0].length);
	double[] c = backSubstitution(cSystem[3], cSystem[2], cSystem[1], cSystem[0].length-1);
	double[] b = new double[c.length];
	double[] d = new double[c.length];
	for(int i = 0; i < c.length - 1; i++){
	    b[i] = (y[i+1]-y[i])/((h[i]*(c[i+1]+c[i]))/3);
	    d[i] = (c[i+1]-c[i])/3*h[i];
	}
	System.out.println("cVec: " + Arrays.toString(c));
	System.out.println("dVec: " + Arrays.toString(d));
	System.out.println("bVec: " + Arrays.toString(b));
	System.out.println("h: " + Arrays.toString(h));
    }
    catch(Exception e){
	System.out.println(e);
    }
    }
}





















