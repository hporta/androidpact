package mainAudio;

import java.io.IOException;




import dtw.DTWRealSignal;
import WavFile.*;
import mfcc.*;

public class RealCarte {
	
	private final int size = 6;
	private final int numberOfTemplates = 3;
	
	private Signal[][] carte;

	public RealCarte() throws IOException, WavFileException {
		carte = new Signal[size][numberOfTemplates];
		this.fill();
	}

	public void fill() throws IOException, WavFileException {

		carte[0][0] = new Signal("mnt/sdcard/AudioRecorder/data/c_bierebrune.wav");
 
		carte[0][1] = new Signal("mnt/sdcard/AudioRecorder/data/c_bierebrune2.wav");
		
		carte[0][2] = new Signal("mnt/sdcard/AudioRecorder/data/c_bierebrune3.wav");

		carte[1][0] = new Signal("mnt/sdcard/AudioRecorder/data/c_cafeaulait.wav");

		carte[1][1] = new Signal("mnt/sdcard/AudioRecorder/data/c_cafeaulait2.wav");
		
		carte[1][2] = new Signal("mnt/sdcard/AudioRecorder/data/c_cafeaulait3.wav");
		
		carte[2][0] = new Signal("mnt/sdcard/AudioRecorder/data/c_chocolatchaud.wav");
		
		carte[2][1] = new Signal("mnt/sdcard/AudioRecorder/data/c_chocolatchaud2.wav");
		
		carte[2][2] = new Signal("mnt/sdcard/AudioRecorder/data/c_chocolatchaud3.wav");
		
		carte[3][0] = new Signal("mnt/sdcard/AudioRecorder/data/c_jusdorange.wav");
		
		carte[3][1] = new Signal("mnt/sdcard/AudioRecorder/data/c_jusdorange2.wav");
		
		carte[3][2] = new Signal("mnt/sdcard/AudioRecorder/data/c_jusdorange3.wav");
	
		carte[4][0] = new Signal("mnt/sdcard/AudioRecorder/data/c_thealamenthe.wav");

		carte[4][1] = new Signal("mnt/sdcard/AudioRecorder/data/c_thealamenthe2.wav");
		
		carte[4][2] = new Signal("mnt/sdcard/AudioRecorder/data/c_thealamenthe3.wav");
		
		carte[5][0] = new Signal("mnt/sdcard/AudioRecorder/data/c_eaugazeuse.wav");
		
		carte[5][1] = new Signal("mnt/sdcard/AudioRecorder/data/c_eaugazeuse2.wav");
		
		carte[5][2] = new Signal("mnt/sdcard/AudioRecorder/data/c_eaugazeuse3.wav");
		
	}
	
public Signal[][] getCarte(){
	return carte;
}

public String recognize(String recordName) {
	
	Signal sample = new Signal(recordName);
	double[][] warpingDistances = new double[size][numberOfTemplates];
	for (int i=0; i<size; i++) {
		for (int j=0; j<numberOfTemplates; j++) {
			DTWRealSignal dtw = new DTWRealSignal(sample, carte[i][j]);
			warpingDistances[i][j] = dtw.getWP();
		}
	}
	int min = minMatrice(warpingDistances);
	
	switch (min) {
	case 0 :
		return "Bière brune";
	case 1 :
		return "Café expresso";
	case 2 :
		return "Coca-Cola";
	case 3 :
		return "Jus d'orange";
	case 4 :
		return "Thé à la menthe";
	case 5 :
		return "Eau gazeuse";
	default :
		return "mô ichido kudasai";
		}

	}

public int minMatrice(double [][] m){
	int nbLignes = m.length;
	int nbColonnes = m[0].length;
	double temp = Double.POSITIVE_INFINITY; //sert à mémoriser la plus petite wp
	int elt = 0; //sert à mémoriser le produit correspondant
	double[] moyennes = new double[size];

	for (int i = 0; i < nbLignes; i++){
		double moyenne = 0.;
		for (int j = 0; j < nbColonnes; j++) {
			moyenne += m[i][j];
		}
		moyennes[i] = moyenne;
	}
		for (int i = 0; i < nbLignes; i++){
			if (moyennes[i] < temp){
			temp = moyennes[i];
			elt =i;
			}
		}
		
return elt;
}
}

