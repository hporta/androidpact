package com.hugo.dome;

import android.support.v7.app.ActionBarActivity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.widget.Button;
import android.widget.VideoView;

public class VideoActivity extends ActionBarActivity implements SurfaceHolder.Callback {
	private MediaRecorder recorder = null;
	private static final String OUTPUT_FILE = "/sdcard/videorecording.mp4";
	private static final String Tag ="VideoCam";
	private VideoView videoView = null;
	private Button start= null;
	private Button record = null;
	private Boolean playing = false;
	private Boolean recording =false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		start = (Button) findViewById(R.id.bgnBtn);
		record = (Button) findViewById(R.id.playRecordingBtn);
		videoView= (VideoView) findViewById(R.id.videoView);
		final SurfaceHolder holder = videoView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
