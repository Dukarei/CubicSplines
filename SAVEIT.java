/*
* Dukarei Abbott
* 2/19/25
* CS 479
* drawCurves
*
* Launches a javaFX application with options to draw several pre-defined curves.
*/
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import static java.lang.Math.*;
public class drawCurves extends Application
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
		
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        // List of sample Functions
        ObservableList<FunctionDisplay> functions = FXCollections.observableArrayList();
        functions.addAll(
                new FunctionDisplay("Daisy", "x=rcos(6t)cos(t)", "y=rcos(6t)sin(t)", 
                					a->Math.cos(6*a)*Math.cos(a),a->Math.cos(6*a)*Math.sin(a)),
                new FunctionDisplay("Clover", "x=rcos(2t)cos(t)", "y=rcos(2t)sin(t)",
                					a->Math.cos(2*a)*Math.cos(a), a->Math.cos(2*a)*Math.sin(a)),
                new FunctionDisplay("Cardioid", "x=r(1-sin(t)) cos(t)", "y=r(1-sin(t)) sin(t)",
                					a->(1-Math.sin(a))*Math.cos(a),a->(1-Math.sin(a))*Math.sin(a)),
                new FunctionDisplay("Lissajous", "x=4rsin(3t+1)", "y = 3rsin(t)", 
                					a->4*Math.sin(3*a + 1),a->3*Math.sin(a)),
                new FunctionDisplay( "Tricuspoid", "x=r(2cos(t)+cos(2t))", "y=r(2sin(t)-sin(2t))", 
                					a->(2*Math.cos(a)+Math.cos(2*a)),a->(2*Math.sin(a)+Math.sin(2*a)))
        );

        //make combo box out of input functions
        ComboBox<FunctionDisplay> combo = new ComboBox<>();
        combo.setItems(functions);


        //cell factory controls item display
        combo.setCellFactory(cell -> new ListCell<FunctionDisplay>() {

            //basic listcell layout
            GridPane gridPane = new GridPane();
            Label lblName = new Label();
            Label lblxFunc = new Label();
            Label lblyFunc = new Label();

            //static block to config layout
            {
                // Ensure all our column widths are constant
                gridPane.getColumnConstraints().addAll(
                        new ColumnConstraints(100, 100, 100),
                        new ColumnConstraints(100, 100, 100),
                        new ColumnConstraints(100, 100, 100)
                );

                gridPane.add(lblName, 0, 1);
                gridPane.add(lblxFunc, 1, 1);
                gridPane.add(lblyFunc, 2, 1);

            }

            // We override the updateItem() method in order to provide our own layout for this Cell's graphicProperty
            @Override
            protected void updateItem(FunctionDisplay function, boolean empty) {
                super.updateItem(function, empty);

                if (!empty && function != null) {

                    // Update our Labels
                    lblName.setText(function.getName());
                    lblxFunc.setText(function.getXfunc());
                    lblyFunc.setText(function.getyFunc());

                    // Set this ListCell's graphicProperty to display our GridPane
                    setGraphic(gridPane);
                } else {
                    // Nothing to display here
                    setGraphic(null);
                }
            }
        });
        
     Button drawButton = new Button("Draw");
     //add event handler to the button
     drawButton.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
             //get the selected function
             FunctionDisplay selectedFunction = combo.getSelectionModel().getSelectedItem();
             final Stage newStage = new Stage();
             if (selectedFunction != null) {
 
                primaryStage.setTitle(selectedFunction.getName());
                Canvas canvas = new Canvas(400, 400);
         		GraphicsContext graphic = canvas.getGraphicsContext2D();
         		graphic.setStroke(Color.RED);//change color to red
         		//stroke a line from (0, 200) to (400, 200) - "our" x-axis
         		graphic.strokeLine(0, 200, 400, 200);
         		//graphic.setLineWidth(4);
         		graphic.setStroke(Color.GREEN);//color change
         		//graphic.strokeOval(0, 0, 200, 400);//this will draw an oval
         		
         		//switch would have  been more efficient, compiler should optimize it out anyway
         		//idea is to set different scale factors and point numbers unique to each function, but in a very general and reproducible way
         		if(selectedFunction.getName() == "Daisy") {
 
         		drawCurve(graphic, selectedFunction.getxFunction() , selectedFunction.getyFunction(), 0, 2*Math.PI, 200, 300);
         		}
         		if(selectedFunction.getName() == "Clover") {
         			drawCurve(graphic, selectedFunction.getxFunction() , selectedFunction.getyFunction(), 0, 2*Math.PI, 200, 300);
         			
         		}
         		if(selectedFunction.getName() == "Cardioid") {
         			drawCurve(graphic, selectedFunction.getxFunction() , selectedFunction.getyFunction(), 0, 2*Math.PI, 300, 100);
         		}
         		if(selectedFunction.getName() == "Lissajous") {
         			drawCurve(graphic, selectedFunction.getxFunction() , selectedFunction.getyFunction(), 0, 2*Math.PI, 50, 100);
         		}
         		else {
         			drawCurve(graphic, selectedFunction.getxFunction() , selectedFunction.getyFunction(), 0, 2*Math.PI, 50, 100);
         		}
         		GridPane pane = new GridPane();
         		
         		pane.add(canvas,  0, 0);
         		
         		
         		Button returnButton = new Button("Return");

         	// Add event handler to the return button
         		returnButton.setOnAction(new EventHandler<ActionEvent>() {
         			@Override
         			public void handle(ActionEvent event) {
         				// Return to the original pane
         				newStage.close();
         	    }
         	});

         	// Add the return button to the grid pane
         	pane.add(returnButton, 0, 1);
         	GridPane.setHalignment(returnButton, HPos.CENTER);
         				
         		Scene scene = new Scene(pane, 400, 500);
         		newStage.setScene(scene);
         		newStage.show();
             }
         }
     });
        //add combobox and draw button to the scene
        root.getChildren().addAll(
                new Label("Select a function to draw:"),
                combo,
                drawButton
        );

        //show the stage
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Draw Curves");
        primaryStage.show();
		
		
		
		
		
		
		/*
		Canvas canvas = new Canvas(400, 400);
		GraphicsContext graphic = canvas.getGraphicsContext2D();
		graphic.setStroke(Color.RED);//change color to red
		//stroke a line from (0, 200) to (400, 200) - "our" x-axis
		graphic.strokeLine(0, 200, 400, 200);
		//graphic.setLineWidth(4);
		graphic.setStroke(Color.GREEN);//color change
		//graphic.strokeOval(0, 0, 200, 400);//this will draw an oval
		
		//call method to "draw" on graphic
		drawCurve(graphic, a->a , a->Math.cos(a), 0, 2*Math.PI, 50, 100);
		
		GridPane pane = new GridPane();
		
		pane.add(canvas,  0, 0);
				
		Scene scene = new Scene(pane, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX Test1");
		primaryStage.show();
		*/
	}
	
	private void drawCurve(GraphicsContext graphic, DoubleUnaryOperator xFunc,DoubleUnaryOperator yFunc, double tBegin, double tEnd, double scaleFactor, int numLines)
	{
		//int n = numLines;//Number of points
		double dt = (tEnd-tBegin)/numLines;//length of one sub division
		//double sf = 50;//scale factor
		double x0 = xFunc.applyAsDouble(tBegin), y0 = yFunc.applyAsDouble(tBegin);//Compute (x0, y0)
		//System.out.println("x1: "+x0);
		//System.out.println("y1: "+y0);
		double t0 = tBegin;
		//a failed implementation of r, another idea would be to add it implicitly to the Function class 
		//as r0, issue is it wouldn't be implicitly passed to the function. 
		//shapes don't draw entirely perfect for the more complex ones due to this
		
		//double r0 = Math.sqrt(Math.pow(y0, 2)+Math.pow(x0, 2));
		
		//System.out.println("r1: "+r0);
		//x0 = x0*r0;
		//y0 = y0*r0;
		//System.out.println("x2: "+x0);
		//System.out.println("y2: "+y0);
		//r0 = Math.sqrt(Math.pow(y0, 2)+Math.pow(x0, 2));
		//System.out.println("r2: "+r0);
		//x0 = x0*r0;
		//y0 = y0*r0;
		//System.out.println("x3: "+x0);
		//System.out.println("y3: "+y0);
		//r0 = Math.sqrt(Math.pow(y0, 2)+Math.pow(x0, 2));
		//System.out.println("r3: "+r0);
		
		double x1, y1, t1, r1;
		for(int k = 0; k < numLines; k++)
		{
			//calculate (x1, y1)
			t1 = t0 + dt;
			x1 = xFunc.applyAsDouble(t1);
			y1 = yFunc.applyAsDouble(t1);
			//r1 = Math.sqrt(Math.pow(y1, 2)+Math.pow(x1, 2));
			//x1 = x1*r1;
			//y1 = y1*r1;
			//scale, invert, shift and draw a line from (x0, y0) to (x1, y1)
			graphic.strokeLine(scaleFactor*x0+200, -scaleFactor*y0 + 200, scaleFactor*x1+200, -scaleFactor*y1 + 200);
			x0 = x1; y0 = y1;t0=t1;//r0=r1;
		}
	}
}
//function class
class FunctionDisplay {

  private final StringProperty name = new SimpleStringProperty();
  private final StringProperty xFunc = new SimpleStringProperty();
  private final StringProperty yFunc = new SimpleStringProperty();
  private DoubleUnaryOperator xFunction;
  private DoubleUnaryOperator yFunction;

  FunctionDisplay(String name, String xFunc, String yFunc, DoubleUnaryOperator xFunction,DoubleUnaryOperator yFunction) {
      this.name.set(name);
      this.xFunc.set(xFunc);
      this.yFunc.set(yFunc);
      this.xFunction = xFunction;
      this.yFunction = yFunction;
  }

  public String getName() {
      return name.get();
  }

  public void setName(String name) {
      this.name.set(name);
  }

  public StringProperty nameProperty() {
      return name;
  }

  public String getXfunc() {
      return xFunc.get();
  }

  public void setxFunction(DoubleUnaryOperator func) {
      this.xFunction = func;
  }
  public DoubleUnaryOperator getxFunction() {
      return xFunction;
  }

  public void setXfunc(String func) {
      this.xFunc.set(func);
  }

  public StringProperty xFuncProperty() {
      return xFunc;
  }

  public String getyFunc() {
      return yFunc.get();
  }

  public void setyFunc(String yFunc) {
      this.yFunc.set(yFunc);
  }
  public void setyFunction(DoubleUnaryOperator func) {
      this.yFunction = func;
  }
  public DoubleUnaryOperator getyFunction() {
      return yFunction;
  }

  public StringProperty yFuncProperty() {
      return yFunc;
  }
}
