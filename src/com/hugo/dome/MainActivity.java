package com.hugo.dome;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;







import android.support.v7.app.ActionBarActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.VideoView;



public class MainActivity extends ActionBarActivity implements SurfaceHolder.Callback {
	private VideoView VV;
	private SurfaceHolder hHolder;     
	private MediaRecorder recorder = null;
	private static final String OUTPUT2_FILE = "/sdcard/videorecording.mp4";
	private static final String Tag ="VideoCam";
	Button audio = null;
	private SurfaceView sv;
	private SurfaceHolder sHolder;   
	private Camera mCamera;    
	private Parameters parameters; 
	private FileOutputStream stream;
	private static final String OUTPUT_FILE="/sdcard/imageoutput.jpg";
	private Handler myHandler;
	private Thread thread = new Thread( new Runnable(){
		public void run() {
			if(sv!=null){
				SavePicture();
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			threadbis.start();
			myHandler.postDelayed(this,60000);
		}
	});
	private Thread threadbis = new Thread(new Runnable(){
		public void run() {
			if(VV!=null){
				try {
					beginRecording(hHolder);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					stopRecording();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	});
	/*try {
					Client2.Commande2();
				}

				catch(Exception e)
				{
					e.printStackTrace();
				}*/




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		audio = (Button)findViewById(R.id.audio);
		audio.setOnClickListener(audioListener);
		sv = (SurfaceView) findViewById(R.id.surfaceview);  
		sHolder = sv.getHolder();  
		sHolder.addCallback(this);   
		sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		myHandler = new Handler();
		myHandler.postDelayed(thread,60000);
		VV= (VideoView) findViewById(R.id.videoView);
		hHolder = VV.getHolder();
		hHolder.addCallback(new VideoCallback());
		hHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}



	/* ###### Listener ######*/

	private OnClickListener audioListener = new OnClickListener(){
		public void onClick(View v){
			Intent Activite2 = new Intent(MainActivity.this, AudioBis.class);
			startActivity(Activite2);
		}
	};


	/* ######## Code photo ########*/
	private void SavePicture() {
		try {

			String fileName = "imageoutput.jpg";

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
			}
		}
	};
	public void surfaceCreated(SurfaceHolder holder) {
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
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(w, h);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

		if(mCamera!=null){
			mCamera.stopPreview();
			mCamera.release();
			mCamera =null;}
	}
	public void onPause() {
		super.onPause();
		if(myHandler != null)
			myHandler.removeCallbacks(thread); // On arrete le callback
	}

	private void stopRecording() throws Exception
	{
		if(recorder != null)
		{
			recorder.stop();
			recorder.release();
		}
	}

	protected void onDestroy()
	{
		super.onDestroy();
		if(recorder != null)
		{
			recorder.release();
		}
	}

	private void beginRecording(SurfaceHolder holder) throws Exception
	{
		mCamera.stopPreview();
		mCamera.release();
		mCamera =null;
		if(recorder != null)
		{
			recorder.stop();
			recorder.release();
		}
		File outFile = new File(OUTPUT2_FILE);
		if(outFile.exists())
		{
			outFile.delete();
		}
		try
		{
			recorder = new MediaRecorder ();
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setVideoSize(320, 240);
			recorder.setVideoFrameRate(15);
			recorder.setMaxDuration(20000);
			recorder.setPreviewDisplay(holder.getSurface());
			recorder.setOutputFile(OUTPUT2_FILE);
			recorder.prepare();
			recorder.start();
		}
		catch(Exception e)
		{
			Log.e(Tag, e.toString());
			e.printStackTrace();
		}
	}



}