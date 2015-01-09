package application;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class Zeichne extends Applet {

public void init ( ) {
    Funktionsplott z =  new Funktionsplott( );
    add(z);   
}

public void paint( Graphics k ){
    setBackground( Color.yellow );		
}
}	