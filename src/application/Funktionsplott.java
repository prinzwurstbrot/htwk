package application;

import java.applet.Applet;     
import java.awt.*;
import java.awt.event.*;


public class Funktionsplott extends Canvas {
		    int breite = 400;         	// Breite der Zeichenfläche
		    int hoehe = 300;          	// Höhe der Zeichenfläche
		    int  mitte_x = 100;       	// Koordinatenursprung
		    int  mitte_y = 150;
		    double einheit =  20;        	// Anzahl der Pixel für die Einheit,   double wegen Rundungsfehler
		    int anfang_x; 
		    int anfang_y;
			
			public double f( double x ){
			                   return Math.sin(x);
			}
			
	    			
			 public boolean mouseDrag(Event e, int x, int y) {
				     Graphics g =  getGraphics( );          //   Koordinatenursprung kann verschoben werden,
				     if ( (Math.abs(x-mitte_x)<10) && (Math.abs(y-mitte_y)<10) ){
					    mitte_x = x; mitte_y = y;
				      }
							  				//  Einheit kann verändert werden,
				     if ( (Math.abs(x-mitte_x-einheit)<10) && (Math.abs(y-mitte_y)<10) ){
					    einheit =  Math.abs(x-mitte_x);
				     }
				        repaint( );
					return true;	  
			 }	 
		      			  
	          
		    Dimension Groesse;
	      
		    public Dimension getPreferredSize( )	{
			return Groesse;
		    }
		    
		    public Funktionsplott(){
			Groesse = new Dimension( breite,hoehe );		
		    }	
		
		    public void zeichne( Graphics g, int i, int j ){
			    if   (  (j > 0) && (j < hoehe) && (anfang_y > 0) && (anfang_y <hoehe)  ) {                         
				g.drawLine(anfang_x, anfang_y, i, j);
			    }
			     anfang_x = i;
			     anfang_y = j;
		    }
		    
		    
		    

	     public void Koordinatensystem(Graphics g){
			      g.drawLine(5,mitte_y, breite-5, mitte_y);              //    x_Achse
			      g.drawLine(mitte_x, 5, mitte_x, hoehe-5);             //    y_Achse
			      g.drawLine(mitte_x + (int) einheit, mitte_y-2, mitte_x + (int) einheit, mitte_y + 2);  
	//   Einheit auf der x_Achse
			      g.drawLine(mitte_x-2, mitte_y -  (int) einheit , mitte_x +2, mitte_y - (int) einheit );   
	                 }                                                                                                             //   Einheit auf der y_Achse                                                                                   
		
		    public void paint(Graphics g){
			setBackground(Color.blue);
			double x;
			double y;
			int j;
			       Koordinatensystem(g); 		    
			       x = ( 0 - mitte_x ) / einheit;         			//   1. Punkt für drawLine
			       y =  f(x);
			       j =  (int) Math.round(mitte_y - y*einheit);
			       anfang_x = 0;
			       anfang_y = j;
			       
			    for ( int i  =  1; i < breite; i++ ) {
			       	x = (i - mitte_x) / einheit;
			       	y =  f(x);
			       	j =  (int)  Math.round(mitte_y - y*einheit);
			       	zeichne( g, i, j ); 
			    }
		     }	
	    }
	   

					


	    

