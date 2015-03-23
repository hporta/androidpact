package dtw;

import java.lang.Math;

import mfcc.*;

//reste à se démerder pour renvoyer display

public class DTWRealSignal {
	private Signal sig1;
	private Signal sig2;
	private double[][] seq1;
	private double[][] seq2;
	private double[][] display; // affiche le chemin le plus court
	private double GAMMA=2.0;
	private double coefNorm = 0.0;

	private int n; // taille sample
	private int m; // taille template
	
	private double warpingDistance;

	public DTWRealSignal(Signal sample, Signal template) {
		sig1 = sample;
		sig2 = template;
		seq1 = sig1.getRealCepstres();
		seq2 = sig2.getRealCepstres();

		n = seq1.length;
		m = seq2.length;

		warpingDistance = 0.0;
		this.fill();

	}

	public void fill() { // remplit dl, DA, ksi et display
		double accumulatedDistance = 0.0;
		int[][] ksi = new int[n][m]; // sert à ranger les codes de paternité
		double[][] dl = new double[n][m]; // distances locales
		double[][] DA = new double[n][m]; // distances accumulées
		//double[][] localDisplay = new double[n][m];

		// initialisation dl
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				dl[i][j] = distanceVect(seq1[i], seq2[j]);
			}
		}

		// initialisation DA
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				DA[i][j] = Double.POSITIVE_INFINITY;
			}
		}

		// initialisation localDisplay
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				//localDisplay[i][j] = -1.0;
			}
		}

		DA[0][0] = dl[0][0]; // initialisation de la première case de DA

		for (int i = 1; i < n; i++) { // remplissage première colonne
			DA[i][0] = dl[i][0] + DA[i - 1][0];
			ksi[i][0] = 3; // le père est la case du dessus
		}

		for (int j = 1; j < m; j++) { // remplissage première ligne
			DA[0][j] = dl[0][j] + DA[0][j - 1];
			ksi[0][j] = 1; // le père est la case à gauche
		}

		for (int i = 1; i < n; i++) {
			for (int j = 1; j < m; j++) {
				double dloc = dl[i][j];
				double a = (DA[i][j - 1]) + dloc;
				double b = (DA[i - 1][j - 1]) + GAMMA * dloc;
				double c = (DA[i - 1][j]) + dloc;
				double v = tripleMin(a, b, c);

				DA[i][j] = v;
				accumulatedDistance = v;
				
				 //déterminer le père (i, j) et le mémoriser dans ksi
				if (v == a) {
					ksi[i][j] = 1;
					
				} else if (v == b) {
					ksi[i][j] = 2;
					
				} else if (v == c) {
 					ksi[i][j] = 3;
 				 					
				}
			}

		}
		
		accumulatedDistance = DA[n-1][m-1];
		int i = n - 1;
		int j = m - 1;

		while (i != 0 || j != 0) {
			//localDisplay[i][j] = DA[i][j]/coefNorm ;
			if (ksi[i][j] == 1) {
				j--;
				coefNorm += 1;
			} else if (ksi[i][j] == 2) {
				i--;
				j--;
				coefNorm += GAMMA;
			} else if (ksi[i][j] == 3){
				i--;
				coefNorm += 1;
			}
		}
		
		//display = localDisplay;
		
		warpingDistance = accumulatedDistance / coefNorm;
	}

	public double getWP() {
		return warpingDistance;
	}

	public double[][] getDisplay() {
		return display;
	}

	public double tripleMin(double a, double b, double c) {
		return Math.min(Math.min(a, b), c);
	}

	public int getN() { // longueur du premier signal, sample
		return n;
	}

	public int getM() { // longueur du deuxième signal, template
		return m;
	}


	public double distanceVect(double[] sig1, double[] sig2) {
		int l = sig1.length;
		double u0 = sig1[0] - sig2[0];
		double s = u0 * u0;

		for (int i = 1; i < l; i++) {
			double u = sig1[i] - sig2[i];
			s += u*u;
		}
		return Math.sqrt(s);
	}
	
	public double getCN(){
		return coefNorm;
	}

}
