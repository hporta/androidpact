package com.hugo.dome;
import android.graphics.Color;
import android.graphics.//import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Test {

	public static void main(String[] args) {
			
	//Selectionner les 3 points
		Point A=new Point(-10,0,0);
		Point B=new Point(10,12,1);
		Point C=new Point(351,1,1);
			
	//Tracer un cercle a partir de 3 points
		Circle cercle=new Circle(A,B,C);
		
	//Creer le detecteur
		SobelEdgeDetector detector = new SobelEdgeDetector();
			
		
	//Choix des parametres
		//Distance au cercle pour Ãªtre inliner
		cercle.setDistanceInliners(25);
				
		//Nombre d'inliners suffisant pour arrÃªter
		cercle.setNombreInliners(3000);
		cercle.setNombreDIterations(10000);
		
		//Niveau de gradient
		detector.setGradientLevel(500);
		
		//Charge l'image
				BufferedImage frame = null;
				try {
					frame = ImageIO.read(new File("C://Users//Patrick//workspace//test.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	//S'applique a  l'image
		detector.setSourceImage(frame);
		detector.process();
		BufferedImage edges = detector.getEdgesImage();
			
	//Enregistre de l'image des contours
		File outputfile1 = new File("C://Users//Patrick//workspace//Contours.png");
		try {
			ImageIO.write(edges, "png", outputfile1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			 
			
		
	//Application de RANSAC
		ArrayList<Point> listeDePoints = new ArrayList<Point>();
		listeDePoints=SobelEdgeDetector.getListPoints();
		cercle.ransac(listeDePoints);
		
	//Definir le meilleur cercle
		Circle bestCircle = cercle.getBestCircle();
		int x = bestCircle.circleCenter().getX();
		int y = bestCircle.circleCenter().getY();
		int r = (int) bestCircle.radius();
			
	//Dessine le cercle obtenu
			
		Graphics2D g2d = frame.createGraphics();
		g2d.setColor(Color.RED);
		g2d.fillOval(x-r,y-r,2*r,2*r);
		g2d.drawImage(frame, 0, 0, null);			
		g2d.dispose();
			
	//Enregistre l'image avec tracé du cercle
		File outputfile2 = new File("C://Users//Patrick//workspace//traceDuCercle.png");
		try {
			ImageIO.write(frame, "png", outputfile2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}

