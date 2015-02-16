package com.hugo.dome;

public class Pixel {
	Color color;
	int codecolor;
	

	public Pixel(Color color, int codecolor){
		this.color = color;
		this.codecolor = codecolor;
	}
	public int getCodecolor(){
		return codecolor;
	}
	
}
