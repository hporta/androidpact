package com.hugo.dome;

import android.support.v7.app.ActionBarActivity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends ActionBarActivity implements SurfaceHolder.Callback 
{
	private MediaRecorder recorder = null;
	private static final String OUTPUT_FILE = "/sdcard/videorecording.mp4";
	private static final String Tag ="VideoCam";
	private VideoView videoView = null;
	private Button start= null;
	private Button record = null;
	private Boolean playing = false;
	private Boolean recording =false;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		start = (Button) findViewById(R.id.bgnBtn);
		record = (Button) findViewById(R.id.playRecordingBtn);
		videoView= (VideoView) findViewById(R.id.videoView);
		final SurfaceHolder holder = videoView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		start.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(!VideoActivity.this.recording & !VideoActivity.this.playing)
				{
					try
					{
						beginRecording(holder);
						playing=false;
						recording=true;
						start.setBackgroundResource(R.drawable.stop);
					} catch (Exception e) {
						Log.e(TAG, e.toString());
						e.printStackTrace();
					}
				}
				else if(VideoActivity.this.recording)
				{
					try
					{
						stopRecording();
						playing=false;
						recording=false;
						start.setBackgroundResource(R.drawable.play);
					}catch (Exception e){
						Log.e(Tag, e.toString());
						e.printStackTrace();
					}
				}
			}
		});
		
		record.setOnClickListener( new OnClickListener() {
			public void onClick(View v){
				if(!VideoActivity.this.playing & !VideoActivity.this.recording)
				{
					try
					{
						playRecording();
						VideoActivity.this.playing=true;
						VideoActivity.this.recording=false;
						record.setBackgroundResource(R.drawable.stop);
					}catch(Exception e){
						Log.e(Tag, e.toString());
						e.printStackTrace();
					}
				}
				else if(VideoActivity.this.playing)
				{
					try
					{
						stopPlayingRecording();
						VideoActivity.this.playing=false;
						VideoActivity.this.recording=false;
						record.setBackgroundResource(R.drawable.play);
					}catch(Exception e){
						Log.e(Tag, e.toString());
						e.printStackTrace();
					}
				}
			}
			
		});		
	}
	
	public void surfaceCreated(SurfaceHolder holder)
	{
		start.setEnable(true);
	}
	
	public void surfaceDestroyed(SurfaceHolder holder)
	{
	}
	
	public void surfaceChanged(SurfaceHilder holder, int format, int width, int height)
	{
		Log.v(Tag, "Width x Height = " + width + "x" + height);
	}
	
	private void playRecording()
	{
		MediaController mc = new MediaController(this);
		videoView.setMediaController (mc);
		videoView.setVideoPath(OUTPUT_FILE);
		videoView.start();
	}
	
	private void stopPlayingRecordingt()
	{
		videoView.stopPlayback();
	}
	
	private void stopRecording() throws Exception
	{
		if(recorder != null)
		{
			recorder.stop();
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
		if(recoder != null)
		{
			recorder.stop();
			recorder.release();
		}
		File outFile = new File(OUTPUT_FILE);
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
			recorder.setVideoSize(/******* VOIR AVEC PATRICK *******/);
			recorder.setVideoFrameRate(/******* VOIR AVEC PATRICK *******/);
			recorder.setMaxDuration(/******* VOIR AVEC PATRICK *******/);
			recorder.setPreviewDisplay(holder.getSurface());
			recorder.setOutputFile(OUT_FILE);
			recorder.prepare();
			recorder.start();
		}
		catch(Exception e)
		{
			Log.e(Tag, e.toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
