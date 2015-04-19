package com.hugo.dome;

import android.util.Log;
import android.view.SurfaceHolder;

public class VideoCallback implements SurfaceHolder.Callback{
	public void surfaceCreated(SurfaceHolder holder)
	{
		/*mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
			// TODO: add more exception handling logic here
		}*/
	}
	
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		/*mCamera.stopPreview();
		mCamera.release();
		mCamera =null;*/
		
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		Log.v("VideoCam", "Width x Height = " + width + "x" + height);
					/*Camera.Parameters parameters = mCamera.getParameters();
					parameters.setPreviewSize(width, height);
					mCamera.setParameters(parameters);
					mCamera.startPreview();*/
	}

}
