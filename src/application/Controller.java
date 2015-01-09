package application;

import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class Controller extends Application {
	
	@FXML
	private TextField input;
	@FXML
	private Canvas canvas;

	private String formel;
	private GraphicsContext gc;
	private double width;
	private double height;
	

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void initialize() {
		gc = canvas.getGraphicsContext2D();
		width = gc.getCanvas().getWidth();
		height = gc.getCanvas().getHeight();
		drawCoord();
	}
	
	// Draw Coordinate System
	public void drawCoord(){
		
		
		gc.setStroke(Color.BLACK);		
		gc.strokeLine(0, height/2, width, height/2);
		gc.strokeLine(width/2, 0, width/2, height);
		for (int x=10; x <= width;x+=20){
			gc.strokeLine(x,(height/2)-10,x, (height/2)+10);
		}
		
		/* 
		 * Transformation Experiment
		 */
		
		/*
		gc.translate(-75,-75);
		Affine trans = gc.getTransform();		
		trans.appendRotation(45, new Point2D(175,175));
		gc.setTransform(trans);
		
		//gc.translate(50,50);
		gc.fillRect(150, 150, 50, 50);
		*/
		
	}

	@FXML
	public void plot(ActionEvent event) {
		if (input.getText().isEmpty()) {
			System.out.println("Textfeld leer");
		} else {
			formel = input.getText();
			drawFunction(5,formel);
		}

	}

	@FXML
	public void clear(ActionEvent event) {
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight()); 
		drawCoord();
	}

	public void drawFunction(int zoom, String formula) {
		int scale = 2;
		Jep jep = new Jep();
		try {
			// add variable to allow parsing
			jep.addVariable("x", 0);
			jep.parse(formula);
			
			
			for (double i = -width; i <= width; i++) {
				
				gc.fillRect(scale * ((width/2) + i), scale * (height-jep.evaluateD() - (height/2)) , 1, 1);				
				// update the value of x
				jep.addVariable("x", i);
				// print the result
				System.out.println("Value at x = " + i + ": " + jep.evaluate());
			}

			
		} catch (JepException e) {
			System.out
					.println("Ein Fehler ist aufgetreten : " + e.getMessage());
		}
	}
}
