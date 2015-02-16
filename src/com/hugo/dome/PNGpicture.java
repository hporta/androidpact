package com.hugo.dome;
import java.util.*;

public class PNGpicture {
	public final static int changementMax = 1000;
	public final static int Aire = 10000;
	private final int size;
	private ArrayList<ArrayList<Pixel>> image;
	
	public PNGpicture(PNGpicture picture0){
		image = new ArrayList<ArrayList<Pixel>>();
		size = picture0.getSize();		
	}
	public PNGpicture(int size){
		this.size = size;
		image = new ArrayList<ArrayList<Pixel>>();
	}
	
	public int getSize(){
		return size;
	}
	public ArrayList<ArrayList<Pixel>> getImage(){
		return image;
	}
	public boolean changement (PNGpicture picture0){
		int a =0;
		for(int i = (Aire/2); i < (size - (Aire/2)); i++){
			for(int j= (Aire/2); j< (size - (Aire/2)); j++){
				for(int k = (i-(Aire/2)); k < (i + (Aire/2)); k++){
					for(int h = (j-(Aire/2)); h < (j + (Aire/2)); h++){
						if(image.get(k).get(h).getCodecolor()!= picture0.getImage().get(k).get(h).getCodecolor()){
							a++;
						}
					}
				}
				if(a>changementMax){
					return true;
				}
			}
		}
		return false;
	}
	

}
