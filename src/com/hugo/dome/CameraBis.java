package com.hugo.dome;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import android.support.v7.app.ActionBarActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.Media;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CameraBis extends ActionBarActivity implements SurfaceHolder.Callback {
	Button audio = null;
	private SurfaceView sv;
	private SurfaceHolder sHolder;   
	private Camera mCamera;    
	private Parameters parameters; 
	private FileOutputStream stream;
	private static final String OUTPUT_FILE="/sdcard/imageoutput.jpg";
	private Thread threadbis = new Thread(new Runnable(){
		public void run() {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(sv!=null){
				SavePicture();
			}
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent Activite2 = new Intent(CameraBis.this, MainActivity.class);
			startActivity(Activite2);
			}
		});
	

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_camera_bis); 
			sv = (SurfaceView) findViewById(R.id.surfaceview); 
			audio = (Button)findViewById(R.id.audio);
			audio.setOnClickListener(audioListener);
			sHolder = sv.getHolder();  
			sHolder.addCallback(this);   
			sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			threadbis.start();
		}
		private OnClickListener audioListener = new OnClickListener(){
			public void onClick(View v){
				Intent Activite3 = new Intent(CameraBis.this, AudioBis.class);
				startActivity(Activite3);
			}
		};

			
		
		private void SavePicture() {
			try {
				//SimpleDateFormat timeStampFormat = new SimpleDateFormat(
				//"yyyy-MM-dd-HH.mm.ss");
				String fileName = "imageoutput.jpg";
				/*"photo_" + timeStampFormat.format(new Date())
        				+ ".jpeg";*/

				// Metadata pour la photo
				ContentValues values = new ContentValues();
				values.put(Media.TITLE, fileName);
				values.put(Media.DISPLAY_NAME, fileName);
				values.put(Media.DESCRIPTION, "Image prise par FormationCamera");
				values.put(Media.DATE_TAKEN, new Date().getTime());
				values.put(Media.MIME_TYPE, "image/jpg");
				values.put(MediaColumns.DATA, OUTPUT_FILE);

				// Support de stockage
				Uri taken = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
						values);


				// Ouverture du flux pour la sauvegarde
				stream = (FileOutputStream) getContentResolver().openOutputStream(
						taken);

				mCamera.takePicture(null, null, pictureCallback);

			} catch (Exception e) {
				// TODO: handle exception
			}


		} 
		Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

			public void onPictureTaken(byte[] data, Camera camera) {
				if (data != null) {
					// Enregistrement de votre image
					try {
						if (stream != null) {
							stream.write(data);
							stream.flush();
							stream.close();
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					// On redémarre la prévisualisation
					//mPreview.setVisibility(View.VISIBLE);
				}
			}
		};
		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, acquire the camera and tell it where
			// to draw.
			mCamera = Camera.open();
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException exception) {
				mCamera.release();
				mCamera = null;
				// TODO: add more exception handling logic here
			}
		}
		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			// Now that the size is known, set up the camera parameters and begin
			// the preview.
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(w, h);
			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// Surface will be destroyed when we return, so stop the preview.
			// Because the CameraDevice object is not a shared resource, it's very
			// important to release it when the activity is paused.
			mCamera.stopPreview();
			mCamera.release();
			mCamera =null;
		}
		
}



