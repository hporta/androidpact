package com.hugo.dome;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;



public class Test {

	private void main(String[] args){
			
	//Selectionner les 3 points
		Point A=new Point(-10,0,0);
		Point B=new Point(10,12,1);
		Point C=new Point(351,1,1);
			
	//Tracer un cercle a partir de 3 points
		Circle cercle=new Circle(A,B,C);
		
	//Creer le detecteur
		SobelEdgeDetector detector = new SobelEdgeDetector();
			
		
	//Choix des parametres
		//Distance au cercle pour être inliner
		cercle.setDistanceInliners(25);///cercle.setDistanceInliners(25);
				
		//Nombre d'inliners suffisant pour arrêter
		cercle.setNombreInliners(3000);
		cercle.setNombreDIterations(10000);
		
		//Niveau de gradient
		detector.setGradientLevel(500);
		
		//Charge l'image
				Bitmap frame = null;
					frame = BitmapFactory.decodeFile("C://Users//Patrick//workspace//test.png");
	
	//S'applique a� l'image
		detector.setSourceImage(frame);
		detector.process();
		Bitmap edges = detector.getEdgesImage();
			
	//Enregistre de l'image des contours
		FileOutputStream out = null;
		String filepath = Environment.getExternalStorageDirectory().getPath();
		String filename = "contour.jpeg";
		File file = new File(filepath, filename);
		try {
			 out = new FileOutputStream(file);
		    edges.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
		    // PNG is a lossless format, the compression factor (100) is ignored
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (out != null) {
		            out.close();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
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
			
	
			
	}
		
}

