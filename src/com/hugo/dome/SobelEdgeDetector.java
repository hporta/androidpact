//import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.Image;


public class SobelEdgeDetector {

	
	private int height;
	private int width;
	private int picsize;
	private int gradlvl;
	private int[] data;
	private int[] magnitude;
	private Bitmap sourceImage;
	private Image edgesImage;
	//private BufferedImage edgesImage;	
	private boolean contrastNormalized;
	
	private int[] xGradient;
	private int[] yGradient;
	private float[] gradientMag;
	
	
	private static ArrayList<Point> listeDePoints = new ArrayList<Point>();
	
										/**************************************/
										/********* Les getters/setters ********/
										/**************************************/
	
	public SobelEdgeDetector() {
		contrastNormalized = false;
	}
	
	
	public static ArrayList<Point> getListPoints() {
		return listeDePoints;
	}
	
	//public BufferedImage getSourceImage() {
	public Image getSourceImage(){
		return sourceImage;
	}
	

	public void setSourceImage(Image image) {
		sourceImage = image;
	}

	public Image getEdgesImage() {
		return edgesImage;
	}

	public void setEdgesImage(Image edgesImage) {
		this.edgesImage = edgesImage;
	}

	public boolean isContrastNormalized() {
		return contrastNormalized;
	}
	
	public void setContrastNormalized(boolean contrastNormalized) {
		this.contrastNormalized = contrastNormalized;
	}
	
	public void setGradientLevel(int gradient){
		gradlvl=gradient;
	}
	
											/********************************/
											/********* Les méthodes ********/
											/*******************************/
	
	public void process() {
		width = sourceImage.getWidth();
		height = sourceImage.getHeight();
		picsize = width * height;
		initArrays();		
		readLuminance();
		if (contrastNormalized) normalizeContrast();
		computeGradients();
		writeEdges(magnitude);
	}
 	
	private void initArrays() {
		if (data == null || picsize != data.length) {
			data = new int[picsize];
			magnitude = new int[picsize];
			
			xGradient = new int[picsize];
			yGradient = new int[picsize];
			gradientMag = new float[picsize];
		}
	}
	
	//Calcul des gradients
	private void computeGradients() {
		int     i, j, nrows, ncols, img[][], nbpoints=0;
	    double  Gx[][], Gy[][], G[][];
	    
	    nrows=sourceImage.getHeight();
	    ncols=sourceImage.getWidth();
	    img = new int[nrows][ncols];
	    
	    for (i=0; i<nrows; i++) {
		      for (j=0; j<ncols; j++) {
		    	  img[i][j]=data[i*ncols+j];
		      }
	    }
	    
	    Gx = new double[nrows][ncols];
	    Gy = new double[nrows][ncols];
	    G  = new double[nrows][ncols];
	    
		for (i=0; i<nrows; i++) {
		      for (j=0; j<ncols; j++) {
		        if (i==0 || i==nrows-1 || j==0 || j==ncols-1){
		          Gx[i][j] = Gy[i][j] = G[i][j] = 0; // Limites de l'image
		        }
		        else{
		          Gx[i][j] = img[i+1][j-1] + 2*img[i+1][j] + img[i+1][j+1] -
		                     img[i-1][j-1] - 2*img[i-1][j] - img[i-1][j+1];
		          Gy[i][j] = img[i-1][j+1] + 2*img[i][j+1] + img[i+1][j+1] -
		                     img[i-1][j-1] - 2*img[i][j-1] - img[i+1][j-1];
		          G[i][j]  = Math.hypot(Math.abs(Gx[i][j]),Math.abs(Gy[i][j]));
		          
		          xGradient[i*ncols+j] = (int) Gx[i][j];
		          yGradient[i*ncols+j] = (int) Gy[i][j];
		          gradientMag[i*ncols+j] = (int) G[i][j];
		          
		          //Choix de l'intensité des points à retenir
		          if(gradientMag[i*ncols+j]>gradlvl)
		        	{
		        	  magnitude[i*ncols+j]=(int) gradientMag[i*ncols+j];
		        	  listeDePoints.add(new Point(j,height-i,0));
		        	  nbpoints++;
		        		  }
		          else
		        	  magnitude[i*ncols+j]=0;
		        }
		      }
		    }
		System.out.println("Nombre de points issu de la d�tection de contours");
		System.out.println(nbpoints);
		System.out.println();
		System.out.println("Niveau de gradient");
		System.out.println(gradlvl);
		System.out.println();
	}
	
	//Calcul de la luminance
	private int luminance(float r, float g, float b) {
		return Math.round(0.299f * r + 0.587f * g + 0.114f * b);
	}
	
	//Lire la luminance de l'image
	private void readLuminance() {
		
		if (type == PixelFormat.RGBA_8888 || type == PixelFormat.RGB_888) {
			int[] pixels = (int[]) sourceImage.getData().getDataElements(0, 0, width, height, null);
			for (int i = 0; i < picsize; i++) {
				int p = pixels[i];
				int r = (p & 0xff0000) >> 16;
				int g = (p & 0xff00) >> 8;
				int b = p & 0xff;
				data[i] = luminance(r, g, b);
			}
		} else if (type == PixelFormat.TYPE_BYTE_GRAY) {
			byte[] pixels = (byte[]) sourceImage.getData().getDataElements(0, 0, width, height, null);
			for (int i = 0; i < picsize; i++) {
				data[i] = (pixels[i] & 0xff);
			}
		} else if (type == PixelFormat.TYPE_USHORT_GRAY) {
			short[] pixels = (short[]) sourceImage.getData().getDataElements(0, 0, width, height, null);
			for (int i = 0; i < picsize; i++) {
				data[i] = (pixels[i] & 0xffff) / 256;
			}
		} else if (type == PixelFormat.RGB_332) {
            byte[] pixels = (byte[]) sourceImage.getData().getDataElements(0, 0, width, height, null);
            int offset = 0;
            for (int i = 0; i < picsize; i++) {
                int b = pixels[offset++] & 0xff;
                int g = pixels[offset++] & 0xff;
                int r = pixels[offset++] & 0xff;
                data[i] = luminance(r, g, b);
            }
        } else {
			throw new IllegalArgumentException("Unsupported image type: " + type);
		}
	}
 
	
	//Normalise le contraste si besoin
	private void normalizeContrast() {
		int[] histogram = new int[256];
		for (int i = 0; i < data.length; i++) {
			histogram[data[i]]++;
		}
		int[] remap = new int[256];
		int sum = 0;
		int j = 0;
		for (int i = 0; i < histogram.length; i++) {
			sum += histogram[i];
			int target = sum*255/picsize;
			for (int k = j+1; k <=target; k++) {
				remap[k] = i;
			}
			j = target;
		}
		
		for (int i = 0; i < data.length; i++) {
			data[i] = remap[data[i]];
		}
	}
	
	//Créer l'image, 
	private void writeEdges(int pixels[]) {
		//Problème au niveau du fichier image, difficile de faire des images en noir et blanc
		if (edgesImage == null) {
			edgesImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		}
		edgesImage.getWritableTile(0, 0).setDataElements(0, 0, width, height, pixels);
		
		//Transforme l'image rouge en noir et blanc
		
		/*image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = image.getGraphics();
		g.drawImage(edgesImage, 0, 0, null);
		g.dispose();
		edgesImage=image;*/
	}
		
}
