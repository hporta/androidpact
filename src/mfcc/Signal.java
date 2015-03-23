package mfcc;

import java.io.File;


//import javax.swing.JFrame;

import WavFile.WavFile;

//import org.math.plot.*;

public class Signal {

	private long size; // nombre d'échantillons du signal
	private long sampleRate; // fréquence d'échantillonnage
	private double[] samples; // échantillons contenus dans le fichier
	private double[] energyPerRaster; // énergie de chaque trame
	private double[] energyPlot; // énergie non nulles pour les courbes
	private double[] firstCepstralCoefficients; // c(0) de chaque trame
	private Raster[] rasters; // vecteur des trames
	private Raster[] trimmedRasters; // vecteur des trames d'énergie non nulle
	private double[][] cepstres; // vecteur des coefficients cepstraux de chaque trame
	private double[][] realCepstres;
	private int numberOfRasters;
	private int paddedRasterLength;
	
	public Signal(String fileName) {
		
		try {
			
			WavFile file = WavFile.openWavFile(new File(fileName));
			
			size = file.getNumFrames();
			
			sampleRate = file.getSampleRate();
			
			numberOfRasters = (int)(size*100/sampleRate);
			//System.out.println(fileName + ": " +numberOfRasters);
			samples = new double[(int)(2*size)];
			file.readFrames(samples, (int)size);
			
			paddedRasterLength = getPaddedRasterLength();
			
			rasters = new Raster[numberOfRasters];
			for (int i=0; i<numberOfRasters; i++) rasters[i] = new Raster(i, this);
			
			energyPerRaster = new double[numberOfRasters];
			for (int i=0; i<numberOfRasters; i++) 
				energyPerRaster[i] = rasters[i].energy();
			
			//System.out.println("premier print" + energyPerRaster[0]);		
			/*for (int i=0; i<1; i++) 
				System.out.println(energyPerRaster[i]);*/
			
			//System.out.println("deuxieme print" + energyPerRaster[0]);			
			int p = 0;
			while (p < numberOfRasters && energyPerRaster[p] != 0.) 
			{
				p++;
			}
			//System.out.println(energyPerRaster[0]);
			trimmedRasters = new Raster[p];
			for (int i=0; i<p; i++) trimmedRasters[i] = rasters[i];
			
			energyPlot = new double[p];
			for (int k=0; k<p; k++) energyPlot[k] = energyPerRaster[k];
			
			cepstres = new double[p][paddedRasterLength];
			for (int i=0; i<p; i++) cepstres[i] = rasters[i].getCepstre();
			
			firstCepstralCoefficients = new double[p];
			for (int i=0; i<p; i++) firstCepstralCoefficients[i] = cepstres[i][0];
			
			//System.out.println(energyPerRaster[0]);			
			int[] realSignal = vocalActivityDetection();
			int realLength = realSignal[1]-realSignal[0];
			realCepstres = new double[realLength][paddedRasterLength];
			for (int i=0; i<realLength; i++) realCepstres[i] = rasters[realSignal[0]+i].getCepstre();
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public long getSampleRate() {
		
		return sampleRate;
		
	}
	
	public double[] getSamples() {
		
		return samples;
		
	}
	
	public double[] getEnergyPerRaster() {
		
		return energyPerRaster;
		
	}
	
	public double[] getFirstCepstralCoefficients() {
		
		return firstCepstralCoefficients;
		
	}
	
	public int getNumberOfRasters() {
		
		return numberOfRasters;
		
	}
	
	public int getPaddedRasterLength() {
		
		//Obtenir une longueur qui est une puissance de 2 
		//afin de pouvoir utiliser l'algorithme de TFD rapide
		long length = sampleRate*2/100;
		long p = 1;
		while (p < length) {
			p=2*p;
		}
		length = p;
		return (int)length;
		
	}
	
	public Raster[] getRasters() {
		
		return rasters;
		
	}
	
	public Raster[] getTrimmedRasters() {
		
		return trimmedRasters;
		
	}
	
	public double[][] getCepstres() {
		
		return cepstres;
		
	}
	
	
	public double noiseLevel() {
		
		double bruit = 0;
		for (int i=1;i<5;i++) {
			bruit += energyPerRaster[i];
		}
		
		return bruit/5;
	}
	public int[] vocalActivityDetection() {
		//System.out.println(energyPerRaster[0]);
		int[] result = new int[2];
		boolean startFound = false;
		boolean endFound = false;
		int i = 5;
		int j = numberOfRasters-1;
		double bruit = noiseLevel();
		//double bruit = (energyPerRaster[0]+energyPerRaster[1]+energyPerRaster[2]+energyPerRaster[3]+energyPerRaster[4])/5;
		while (i<j && !startFound) {
			if (energyPerRaster[i]>2*bruit) {
				startFound = true;
			} else i++;
		}
		if (startFound) result[0] = i; else result[0] = 0;
		while (j>i && !endFound) {
			if (2*energyPerRaster[j]<bruit) {
				endFound = true;
			} else j--;
		}
		if (endFound) result[1] = j; else result[1]=numberOfRasters-1;
		return result;
	}
	
	public double[][] getRealCepstres() {
		
		return realCepstres;
	}
	
	public void printEnergyPerRaster() {
		
		for (double truc : energyPerRaster) System.out.println(truc);
	}
	
	/*public void verif2() {
		
		//Vérifier que la courbe de l'énergie du signal sur chaque trame varie de
		//manière similaire à la courbe de la séquence du coefficient c(0)
		//calculé sur chaque trame

		Plot2DPanel plot = new Plot2DPanel();
		Plot2DPanel plot2 = new Plot2DPanel();

		
		//Tracer les deux courbes dans des fenêtres différentes
		//pour des questions d'échelle
		plot.addLinePlot("énergie", energyPlot);
		plot2.addLinePlot("c0", firstCepstralCoefficients);
				 
		JFrame frame = new JFrame("test2 : énergie");
		frame.setContentPane(plot);
		frame.setVisible(true);
		
		JFrame frame2 = new JFrame("test2 : c(0)");
		frame2.setContentPane(plot2);
		frame2.setVisible(true);
		
	}*/
	
}
