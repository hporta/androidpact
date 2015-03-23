package com.hugo.dome;



import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class MainActivity extends ActionBarActivity {
	Button audio = null;
	Button image = null;
	Button send = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send =(Button)findViewById(R.id.send);
        audio = (Button)findViewById(R.id.audio);
        audio.setOnClickListener(audioListener);
        image = (Button)findViewById(R.id.image);
        image.setOnClickListener(imageListener);
        send.setOnClickListener(sendListener);
    }
    private OnClickListener sendListener = new OnClickListener(){
    	public void onClick(View v){
    		Thread thread = new Thread(new Runnable(){
        		@Override
        		public void run()
        		{
        			try {
        				Client2.Commande2();
        			}
        			
        			catch(Exception e)
        			{
        				e.printStackTrace();
        			}
        		}
        		
        	});
            thread.start();
    	}
    };
    private OnClickListener audioListener = new OnClickListener(){
    	public void onClick(View v){
    		
    		Intent Activite2 = new Intent(MainActivity.this, AudioBis.class);
    		startActivity(Activite2);
    	}
    };
    private OnClickListener imageListener = new OnClickListener(){
    	public void onClick(View v){
    		
    		Intent Activite3 = new Intent(MainActivity.this, CameraActivity.class);
    		startActivity(Activite3);
    	}
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
