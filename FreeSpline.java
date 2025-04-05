
import java.util.Random;
import java.util.function.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
public class FreeSpline extends Application{
	public void start(Stage primaryStage) throws Exception
	{	
		//only input required is x and y arrays, the x and y offsets(lines for axes, as well as scalefactor of drawCurve may need adjusted)
//    	double[] x = {0,0.4,1,1.4,2,2.4,3,3.4,4};
//    	double[] y = {-2.4,0.2,2,-0.5,-2.6,0.3,3.5,-1.2,2.4};
//		double[] x = {0,1,2,3,4,5};
//		double[] y = {0, 0.05, 0.4, 1.35,3.2,6};
		double[] x = {0,0.4,1,1.4,2,2.4,3,3.4,4};
		double[] y = {-2.4,0.2,2,-0.5,-2.6,0.3,3.5,-1.2,2.4};
		/*
		 * playing around with this has interesting results, although not very spline-ey
		double[][] arr = genInputs(20, 100);
		double[] x = arr[0];
		double[] y = arr[1];
		*/
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int cWidth = gd.getDisplayMode().getWidth();
		int cHeight = gd.getDisplayMode().getHeight();
		double height = cHeight * 0.8;
		double width = cWidth * 0.8;
		double xScaleFactor = (width*0.6)/distance(x);
		double yScaleFactor = (height*0.6)/distance(y);
		double yOffset = height/linTerp(y, 1.2, 2);
		double xOffset = width/linTerp(x, 20, 2); //linTerp takes array as input and interpolates based upon % of negative nums in array. closer to 0, closer to first num, closer to 50%, closer to second num
		Canvas canvas = new Canvas(width, height);
		GraphicsContext graphic = canvas.getGraphicsContext2D();
		graphic.setStroke(Color.BLACK);//change color to red
		graphic.strokeLine(0, yOffset, width, yOffset);
		graphic.strokeLine(xOffset, 0, xOffset, height);
		//graphic.setStroke(Color.GREEN);//color change
		Color[] colors = {Color.CORNFLOWERBLUE, Color.DARKORANGE, Color.CRIMSON,Color.DARKRED, Color.BLUEVIOLET, Color.BLUE, Color.AQUAMARINE, Color.LIGHTSEAGREEN};
		DoubleUnaryOperator[] spline = constructSpline(x,y);
		int k = 0;
		for(int i = 0; i < spline.length; i++) {
			graphic.setStroke(colors[k]);
			drawCurve(graphic, a->a, spline[i], x[i],x[i+1], xScaleFactor, yScaleFactor, 50, xOffset, yOffset);
			if(k==colors.length-1)k=0;
			else k++;
		}
		GridPane pane = new GridPane();
		pane.add(canvas,  0, 0);
		Scene scene = new Scene(pane, width, height);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Free Spline(s)");
		primaryStage.show();
	}
	private static double linTerp(double[] y, double p0, double p1) {
		//linearly interpolating between 1.2 and 2 such that when we have 50% of array as negative, value is at 2 which allows xAxis to be drawn halfway on screen
		int sum = 0;
		for(int i = 0; i < y.length; i++) {
			if(y[i] < 0)sum++;
		}
		double ratio = sum/(y.length/2);
		DoubleUnaryOperator interp = a->(1-a)*p0 + a*p1;
		return interp.applyAsDouble(ratio);
	}
	private static double[][] genInputs(int n, double bound){
		Random rand = new Random();
		double[][] arr = new double[2][n];
		for(int i = 0; i < arr[0].length; i++) {
			arr[0][i] = rand.nextDouble(bound);
			arr[1][i] = rand.nextDouble(bound);
		}
		return arr;
	}
	private static double distance(double[] a) {
		double max = a[0];
		double min = a[0];
		for(int i = 1; i < a.length; i++) {
			if(a[i] > max)max = a[i];
			if(a[i] < min)min = a[i];
		}
		return max-min;
	}
	private void drawCurve(GraphicsContext graphic, DoubleUnaryOperator xFunc,DoubleUnaryOperator yFunc, double tBegin, double tEnd, double xScaleFactor, double yScaleFactor,int numLines, double xOffset, double yOffset)
	{
		//int n = numLines;//Number of points
		double dt = (tEnd-tBegin)/numLines;//length of one sub division
		//double sf = 50;//scale factor
		double x0 = xFunc.applyAsDouble(tBegin), y0 = yFunc.applyAsDouble(tBegin);//Compute (x0, y0)
		//System.out.println("x1: "+x0);
		//System.out.println("y1: "+y0);
		double t0 = tBegin;
		double x1, y1, t1;
		for(int k = 0; k < numLines; k++)
		{
			//calculate (x1, y1)
			t1 = t0 + dt;
			x1 = xFunc.applyAsDouble(t1);
			y1 = yFunc.applyAsDouble(t1);
			//System.out.println("x1: " + x1);
			//System.out.println("y1: " + y1);
			//scale, invert, shift and draw a line from (x0, y0) to (x1, y1)
			graphic.strokeLine(xScaleFactor*x0+xOffset, -yScaleFactor*y0 + yOffset, xScaleFactor*x1+xOffset, -yScaleFactor*y1 + yOffset);
			x0 = x1; y0 = y1;t0=t1;//r0=r1;
		}
	}
	public static void main(String[] arg)
	{
		Application.launch(arg);
	}
   public static double[][] gaussianElimination(double[] consts,double[] sub,double[] main,double[] sup, int n) throws Exception{
	//idea is this function will return the double array with arr[0] = cPrime vector and arr[1] = dPrime vector as given by ThomasAlgo
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
	arr[1][0] = 0;
	arr[2][0] = 1;
	arr[2][y.length-1] = 1;
	arr[3][y.length-1] = 0;
	//sub/super arrays init
	for(int i = 0; i < y.length-1; i++){
	    if(i > 0) arr[1][i] = h[i];
	    if(i == y.length-2) arr[0][i] = 0;
	    else arr[0][i] = h[i];
	    //statement below makes it simple to evaluate that sub and super diagonals are consistent with the h values outlined in formula
	    //System.out.printf("\nh[%d] : %f, sub[%d] : %f, sup[%d]: %f",i, h[i], i, arr[0][i], i, sup[i]);
	}
	//main/consts init 
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
    public static DoubleUnaryOperator[] constructSpline(double[]x, double[]y){
	DoubleUnaryOperator[] s = new DoubleUnaryOperator[y.length-1];
	double[] h = new double[y.length-1];
	for(int i = 0; i<h.length; i++){
		    h[i] = x[i+1]-x[i];
		    //System.out.printf("\nh[%d] : %f", i, h[i]);
	}
	double[][] cSystem = initMatrix(x, y, h);
	try{
	    gaussianElimination(cSystem[3], cSystem[0], cSystem[2], cSystem[1], cSystem[0].length);
	    double[] c = backSubstitution(cSystem[3], cSystem[2], cSystem[1], cSystem[0].length-1);
	    double[] b = new double[c.length];
	    double[] d = new double[c.length];
	    for(int i = 0; i < c.length - 1; i++){
		b[i] = (y[i+1]-y[i])/h[i]-h[i]*(c[i+1]+2*c[i])/3;
		d[i] = (c[i+1]-c[i])/(3*h[i]);
	    }
	    //manually input final values due to loop errors and there is no c[n] = 0, so we manually handle that here 
	    b[c.length-1] = (y[c.length]-y[c.length-1])/h[c.length-1]-h[c.length-1]*(0+2*c[c.length-1])/3;
	    d[c.length-1] = (0-c[c.length-1])/(3*h[c.length-1]);
	    for(int i = 0; i < c.length; i++){
		final double xi = x[i];
		final double di = d[i];
		final double ci = c[i];
		final double bi = b[i];
		final double yi = y[i];
		System.out.printf("\nS%d(x) = %f + %f(x-%f) + %f(x-%f)^2 + %f(x-%f)^3", i, y[i], b[i], x[i], c[i], x[i], d[i], x[i]);
		s[i] = a->yi + bi*(a-xi)+ ci*Math.pow((a-xi),2)+ di*Math.pow((a-xi),3); 	
	    }
	}
	catch(Exception e){
	    System.out.println(e);
	}
	return s;
    }
}