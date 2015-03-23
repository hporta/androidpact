package com.hugo.dome;

import java.io.File;
import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.animation.*;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AudioActivity extends ActionBarActivity {
	ImageView logo = null;
	Button button = null;
	Button button1 = null;
	Animation animation =null;
	MediaRecorder mRecorder = null;
	File mSampleFile =null;
	static final String SAMPLE_PREFIX = "recording";
	static final String SAMPLE_EXTENSION = ".amr";
	private static final String TAG ="AudioActivity";
	private static final String OUTPUT_FILE="/sdcard/audiooutput.amr";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mRecorder = new MediaRecorder();
		setContentView(R.layout.activity_audio);
		button1 = (Button)findViewById(R.id.button_audio);
		button = (Button)findViewById(R.id.button);
		logo = (ImageView)findViewById(R.id.logo);
		animation= AnimationUtils.loadAnimation(this,R.anim.rotation);
		button.setOnClickListener(stopListener);
		button1.setOnClickListener(startListener);
	}
	




        OnClickListener startListener = new OnClickListener() {
            public void onClick(View v) {
            	Thread thread = new Thread(new Runnable(){
            		@Override
            		public void run()
            		{
            			try {
            				Client.commande("biere brune");
            			}
            			
            			catch(Exception e)
            			{
            				e.printStackTrace();
            			}
            		}
            		
            	});
            	thread.start();
            	logo.startAnimation(animation);
            	startRecording();   
            	
            	}
        };

        OnClickListener stopListener = new OnClickListener() {
            public void onClick(View v) {
            	animation.reset();
            	stopRecording();
            	addToDB();
            	}
        };
        protected void addToDB(){
        	ContentValues values = new ContentValues(3);
        	long current = System.currentTimeMillis();
        	values.put(MediaColumns.TITLE, "test_audio");
        	values.put(MediaColumns.DATE_ADDED, (int) (current/1000) );
        	values.put(MediaColumns.MIME_TYPE, "audio/amr");
        	//values.put(MediaColumns.DATA, this.getFilesDir().getAbsolutePath()+"/audiooutput.amr");
        	values.put(MediaColumns.DATA, OUTPUT_FILE);
        	ContentResolver contentResolver = getContentResolver();
        	
        	Uri base =MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        	Uri newUri = contentResolver.insert(base, values);
        	
        	sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        }
        
        protected void startRecording(){
        	this.mRecorder = new MediaRecorder();
        	this.mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            this.mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            //this.mRecorder.setOutputFile(this.getFilesDir().getAbsolutePath()+"/audiooutput.amr");
            this.mRecorder.setOutputFile(OUTPUT_FILE);
            this.mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try{
            	this.mRecorder.prepare();
            } catch(IllegalStateException e1){
            	e1.printStackTrace();
            } catch(IOException e1){
            	e1.printStackTrace();
            }
            this.mRecorder.start();
            
            if(this.mSampleFile == null){
            	File sampleDir = this.getFilesDir();
            	try {
            		this.mSampleFile = File.createTempFile(AudioActivity.SAMPLE_PREFIX, AudioActivity.SAMPLE_EXTENSION, sampleDir);
            	}catch(IOException e){
            		Log.e(AudioActivity.TAG," access error");
            	}
            }
        }
        protected void stopRecording(){
        	this.mRecorder.stop();
        	this.mRecorder.release();
        }
    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

    }
}