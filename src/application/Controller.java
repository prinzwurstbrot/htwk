package application;

import java.util.ArrayList;

import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import com.singularsys.jep.functions.List;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class Controller extends Application {

	@FXML
	private TextField input;
	@FXML
	private TextField xMin;
	@FXML
	private TextField xMax;
	@FXML
	private Label response;
	@FXML
	private Canvas canvas;

	private String formel;
	private GraphicsContext gc;

	private int width;
	private int height;
	private int min;
	private int max;
	double interval;
	
	// Regulärer Ausdruck um nur Zahlen zuzulassen
	String digits = "[-]?[0-9]+"; // alle Zahlen von 0-9

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(
					"MainView.fxml"));
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
		width = (int) gc.getCanvas().getWidth();
		height = (int) gc.getCanvas().getHeight();
		drawCoord(0.1);
	}

	// Koordinatensystem erstellen
	public void drawCoord(double dx) {
		gc.setStroke(Color.BLACK);
		// X und Y Achse
		gc.strokeLine(0, height / 2, width, height / 2);
		gc.strokeLine(width / 2, 0, width / 2, height);
		
		 // Skala auf X-Achse / Initialisierung in 0.1er Schritten
		for (int x = 0; x <= width; x += dx * width) 
		{ gc.strokeLine(x, (height / 2) - 5, x, (height / 2) +  5); }
		  
		  // Skala auf Y-Achse
		for (int y = 0; y <= height; y += dx * height)
			{ gc.strokeLine((width / 2) - 5, y, (width / 2) + 5, y);
		  }
	}

	@FXML
	public void plot(ActionEvent event) {
		if (input.getText().isEmpty()) {
			System.out.println("Textfeld leer");
			input.setPromptText("Textfeld leer !");
		} else if (!xMax.getText().matches(digits)
				|| !xMin.getText().matches(digits)) {
			System.out.println("Interval muss Zahl sein !");
			response.setText("Interval muss eine Zahl sein !");
		} else {
			formel = input.getText();
			min = Integer.parseInt(xMin.getText());
			max = Integer.parseInt(xMax.getText());
			drawFunction(formel);
		}
	}

	public void clearCanvas(){
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		drawCoord(1/interval);
	}
	
	@FXML
	public void clear(ActionEvent event) {		
		clearCanvas();
	}

	public void drawFunction(String formula) {
		// Versatz zu Koordinatenursprung
		double shiftX = width / 2;
		double shiftY = height / 2;
		interval = max-min;
		double dx = interval/(double)width;
		// X und Y Koordinate zum Zeichnen der Funktion 
		double x,y;
		
		clearCanvas();
		drawCoord(1/interval);
		
		Jep jep = new Jep();
	
		try { // Variable X mit Wert 0 hinzufügen
			jep.addVariable("x", 0);
			jep.parse(formula);

			for (double i = -interval; i <= interval; i += dx) {

				// Abbilden der Werte w von -Interval < w < Interval auf -widht < w < width			
				x = (i * (width/interval)) + shiftX;
				y = (height - (jep.evaluateD()*(height/interval)))- shiftY;

				// Zeichene Punkt 
				gc.fillRect(x,y, 1, 1);

				// update des Wertes von X
				jep.addVariable("x", i);

				// Ausgabe X und Y
				System.out.println("x = " + x + " y : " + y);
			}

		} catch (JepException e) {
			System.out
					.println("Ein Fehler ist aufgetreten : " + e.getMessage());
		}
	}

}
