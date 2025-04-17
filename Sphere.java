/*
   Dukarei Abbott
* 4/17/25
* CS 479
* Sphere
*
  computes x,y,z points on the sphere and then draws said sphere on jfx canvas
*/
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import static java.lang.Math.*;


public class Sphere extends Application
{
	/*
	 * start is an abstract method of the Application (abstract class)
	 * When the class is run (no main method necessary), start method is
	 * invoked with a Stage object. 
	 */
	private int count = 0;
	@Override
	public void start(Stage primaryStage) throws Exception
	{		
		Canvas canvas = new Canvas(400, 400);
		GraphicsContext graphic = canvas.getGraphicsContext2D();
		graphic.setStroke(Color.RED);//change color to red
		graphic.strokeLine(0, 200, 400, 200); //x axis
		graphic.setStroke(Color.GREEN);//color change
		
		drawSinCurve(graphic);
		
		GridPane pane = new GridPane();
		
		pane.add(canvas,  0, 0);
				
		Scene scene = new Scene(pane, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Compute Sphere");
		primaryStage.show();
	}
	void setSphere(double[][] x, double[][] y, double[][] z){
	    int n = 100; //number of points to sketch
	    
	}
	private void drawSinCurve(GraphicsContext graphic)
	{
		int n = 100;//Number of points
		double dx = 2*Math.PI/n;//length of one sub division
		double sf = 50;//scale factor
		double x0 = 0, y0 = Math.sin(x0);//Compute (x0, y0)
		double x1, y1;
		for(int k = 0; k < n; k++)
		{
			//calculate (x1, y1)
			x1 = x0 + dx;
			y1 = Math.sin(x1);
			//scale, invert, shift and draw a line from (x0, y0) to (x1, y1)
			graphic.strokeLine(sf*x0, -sf*y0 + 200, sf*x1, -sf*y1 + 200);
			x0 = x1; y0 = y1;
		}
	}
	
	
	/*
	public static void main(String[] arg)
	{
		Application.launch(arg);
	}
	*/
}

