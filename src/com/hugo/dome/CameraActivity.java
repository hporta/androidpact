package com.hugo.dome;

import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

//Notre classe implémente SurfaceHolder.Callback
public class CameraActivity extends Activity implements SurfaceHolder.Callback{
	private Camera camera;
	private SurfaceView surfaceCamera;
	private Boolean isPreview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		isPreview = false;

		// On applique notre layout
		setContentView(R.layout.activity_camera);

		// On récupère notre surface pour le preview
		surfaceCamera = (SurfaceView) findViewById(R.id.surface_view);

		// Méthode d'initialisation de la caméra
		InitializeCamera();
		/*surfaceCreated(surfaceCamera.getHolder());*/
	}

	public void InitializeCamera() {
		// On attache nos retour du holder à notre activite
		surfaceCamera.getHolder().addCallback(this);

		// On spécifie le type du hoder en mode SURFACE_TYPE_PUSH_BUFFERS
		surfaceCamera.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		if (camera == null)
			camera = Camera.open();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Si le mode preview est lancé alors on le stop
		if (isPreview) {
			camera.stopPreview();
		}
		// On récupère les parametres de la camera
		Camera.Parameters parameters = camera.getParameters();

		// On change la taille
		parameters.setPreviewSize(width, height);

		// On applique nos nouveaux parametres
		camera.setParameters(parameters);
		try {
			// On attache notre previsualisation de la camera au holder de la
			// surface
			camera.setPreviewDisplay(surfaceCamera.getHolder());
		} catch (IOException e) {
		}

		// On lance la previeuw
		camera.startPreview();

		isPreview = true;

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		if (camera != null) {
			camera.stopPreview();
			isPreview = false;
			camera.release();
		}	
	}
	@Override
	public void onResume() {
		super.onResume();
		camera = Camera.open();
	}

	// Mise en pause de l'application
	@Override
	public void onPause() {
		super.onPause();

		if (camera != null) {
			camera.release();
			camera = null;
		}
	}
}