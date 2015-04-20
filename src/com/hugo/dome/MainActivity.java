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
import android.widget.TextView;
import android.widget.VideoView;



public class MainActivity extends ActionBarActivity implements SurfaceHolder.Callback {
	private VideoView VV;
	private SurfaceHolder hHolder;     
	private MediaRecorder recorder = null;
	private static final String OUTPUT2_FILE = "/sdcard/videorecording.mp4";
	private static final String Tag ="VideoCam";
	Button audio = null;
	private TextView texte;
	//Thread principale pour l'action du système embarqué
	private Thread threadbis = new Thread(new Runnable(){
		public void run() {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(VV!=null){
				  runOnUiThread(new Runnable() {
				        public void run() {
				          texte.setText("Ne pas touché au bouton audio");
				        }
				      });
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
				runOnUiThread(new Runnable() {
			        public void run() {
			          texte.setText("Bienvenue sur le système embarqué de DOME");
			        }
			      });
			}
			try {
				Thread.sleep(40000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Intent Activite2 = new Intent(MainActivity.this, CameraBis.class);
			startActivity(Activite2);
		
		}
		
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		texte = (TextView)findViewById(R.id.texte);
		audio = (Button)findViewById(R.id.audio);
		audio.setOnClickListener(audioListener);
		VV= (VideoView) findViewById(R.id.videoView);
		hHolder = VV.getHolder();
		hHolder.addCallback(this);
		hHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		threadbis.start();
	}



	/* ###### Listener ######*/

	private OnClickListener audioListener = new OnClickListener(){
		public void onClick(View v){
			Intent Activite3 = new Intent(MainActivity.this, AudioBis.class);
			startActivity(Activite3);
		}
	};
	public void surfaceCreated(SurfaceHolder holder)
	{
	}
	
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		Log.v("VideoCam", "Width x Height = " + width + "x" + height);
			
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
	{//méthode si jamais l'appli est détruite la caméra est libérée
		super.onDestroy();
		if(recorder != null)
		{
			recorder.release();
		}
	}

	private void beginRecording(SurfaceHolder holder) throws Exception
	{
		
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
			//instanciation du recorder
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