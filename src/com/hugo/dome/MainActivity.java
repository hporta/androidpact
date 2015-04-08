package com.hugo.dome;



import java.io.FileOutputStream;
import java.util.Date;

import android.support.v7.app.ActionBarActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.Media;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class MainActivity extends ActionBarActivity {
	Button audio = null;
	Button image = null;
	Button send = null;
	private FileOutputStream out;
	private Bitmap frame;
	private static final String OUTPUT_FILE="/sdcard/imageoutput.jpg";
	private Handler myHandler;
	
	private Runnable myRunnable = new Runnable(){
		public void run() {
			if (mPreview != null){
			SavePicture();	    		
			myHandler.postDelayed(this,60000);
			}}
	};
	
	private Preview mPreview;
	FileOutputStream stream;

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
        mPreview = new Preview(this);
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable,60000);
        
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
    		
    		Intent Activite3 = new Intent(MainActivity.this, VideoActivity.class);
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

    		mPreview.getCamera().takePicture(null, pictureCallback, pictureCallback);
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
    			}
     
    			// On redémarre la prévisualisation
    			//camera.startPreview();
    		}
    	}
    };
    public void onPause() {
        super.onPause();
        if(myHandler != null)
            myHandler.removeCallbacks(myRunnable); // On arrete le callback
    }
    

}