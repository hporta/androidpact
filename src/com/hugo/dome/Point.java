package com.hugo.dome;
import java.util.ArrayList;


public class Point {
private int x;
private int y;
//z=1 si Inliner, z=0 si Outliner
private int z;
//private double rayon;
//private double angle;

//Coordonn�es cart�siennes
public Point(int x, int y, int z) {
	this.x=x;
	//x=(int) (rayon*Math.cos(angle));
	this.y=y;
	//y=(int) (rayon*Math.sin(angle));
	this.z=z;
}

/*//Coordonn�es 
public Point(double rayon, double angle, int z){
	this.setRayon(rayon);
	rayon=Math.sqrt(x*x+y*y);
	this.setAngle(angle);
	angle=Math.atan(y/x);
	this.z=z;
}*/

//Getters et Setters
public int getX() {
	return x;
}
public void setX(int x) {
	this.x = x;
}
public int getY() {
	return y;
}
public void setY(int y) {
	this.y = y;
}
public int getInliner() {
	return z;
}
public void setInliner(int z) {

	this.z = z;
}
/*public double getRayon() {
	return rayon;
}
public void setRayon(double rayon) {
	this.rayon = rayon;
}
public double getAngle() {
	return angle;
}
public void setAngle(double angle) {
	this.angle = angle;
}*/

//Calculer le nombre d'Inliner
public int nombreInliners(ArrayList<Point> listePoints) {
	int nombreInliners=0;
	for(int i=0;i<listePoints.size();i++){
		nombreInliners=nombreInliners+listePoints.get(i).getInliner();
	}	
	//System.out.println(nombreInliners);
	return nombreInliners;
}


}
