package com.hugo.dome;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.ContentValues;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.Media;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

//Notre classe implémente SurfaceHolder.Callback
public class CameraActivity extends Activity {
	private static final String OUTPUT_FILE="/sdcard/imageoutput.jpeg";
	private Handler myHandler;
	private Runnable myRunnable = new Runnable(){
		public void run() {
			SavePicture();	    		
			myHandler.postDelayed(this,60000);
			}
	};
	private Preview mPreview;
	FileOutputStream stream;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new Preview(this);
        mPreview.setOnClickListener(photoListener);
        setContentView(mPreview);
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable,60000);
    }
    private OnClickListener photoListener = new OnClickListener(){
    	public void onClick(View v) {
    		// On prend une photo
    		if (mPreview != null) {
    			SavePicture();
    		}
    	}
    };
    private void SavePicture() {
    	try {
    		SimpleDateFormat timeStampFormat = new SimpleDateFormat(
    				"yyyy-MM-dd-HH.mm.ss");
    		String fileName = "photo_" + timeStampFormat.format(new Date())
    				+ ".jpeg";

    		// Metadata pour la photo
    		ContentValues values = new ContentValues();
    		values.put(Media.TITLE, fileName);
    		values.put(Media.DISPLAY_NAME, fileName);
    		values.put(Media.DESCRIPTION, "Image prise par FormationCamera");
    		values.put(Media.DATE_TAKEN, new Date().getTime());
    		values.put(Media.MIME_TYPE, "image/jpeg");
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
    			camera.startPreview();
    		}
    	}
    };
    public void onPause() {
        super.onPause();
        if(myHandler != null)
            myHandler.removeCallbacks(myRunnable); // On arrete le callback
    }
}
	