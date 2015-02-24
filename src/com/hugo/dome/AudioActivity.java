package com.hugo.dome;

import android.support.v7.app.ActionBarActivity;
import android.view.animation.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class AudioActivity extends ActionBarActivity {
	ImageView logo = null;
	Button button = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
		button = (Button)findViewById(R.id.button);
		logo = (ImageView)findViewById(R.id.logo);
		Animation animation= AnimationUtils.loadAnimation(this,R.anim.rotation);
		logo.startAnimation(animation);
		
		button.setOnClickListener(stopListener);
	}
OnClickListener stopListener =new OnClickListener(){
	public void onClick(View v){
		Intent Activite2 = new Intent(AudioActivity.this, MainActivity.class);
		startActivity(Activite2);
	}
	
};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audio, menu);
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
