package com.hugo.dome;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import mainAudio.RealCarte;
import android.support.v7.app.ActionBarActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.Media;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class MainActivity extends ActionBarActivity implements SurfaceHolder.Callback {
	private static final int RECORDER_BPP = 16;
	private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
	private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
	private static final int RECORDER_SAMPLERATE = 16000;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

	private AudioRecord recorder = null;
	private int bufferSize = 0;
	private Thread recordingThread = null;
	private boolean isRecording = false;
	private RealCarte carte = null;
	private String produit = null;
	private String Path ="/mnt/sdcard/AudioRecorder/audio.wav" ;

	Button start = null;
	Button stop =null;
	Button affichecarte = null;
	Button test = null;
	private SurfaceView sv;
	private SurfaceHolder sHolder;   
	private Camera mCamera;    
	private Parameters parameters; 
	private FileOutputStream stream;
	private static final String OUTPUT_FILE="/sdcard/imageoutput.jpg";
	private Handler myHandler;
	private Runnable myRunnable = new Runnable(){
		public void run() {
			//photoListener.onClick(button);
			if(sv!=null){
				SavePicture();
				try {
					Client2.Commande2();
				}

				catch(Exception e)
				{
					e.printStackTrace();
				}
				myHandler.postDelayed(this,80000);
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*try {
        	carte = new RealCarte();
        	carte.fill();
        } catch(Exception e) {
        	e.printStackTrace();
        }*/
		setContentView(R.layout.activity_main);
		test = (Button)findViewById(R.id.test);
		start = (Button)findViewById(R.id.Start);
		stop = (Button)findViewById(R.id.Stop);
		start.setOnClickListener(startListener);
		stop.setOnClickListener(stopListener);
		bufferSize = AudioRecord.getMinBufferSize(8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		affichecarte = (Button)findViewById(R.id.audio);
		affichecarte.setOnClickListener(audioListener);
		test.setOnClickListener(testListener);
		sv = (SurfaceView) findViewById(R.id.surfaceview);  
		sHolder = sv.getHolder();  
		sHolder.addCallback(this);   
		sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		myHandler = new Handler();
		myHandler.postDelayed(myRunnable,80000);

	}
	/* ###### Listener ######*/
	OnClickListener startListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {


			startRecording();
		}
	};
	OnClickListener stopListener = new View.OnClickListener() {
		@Override
		public void onClick(View v){
			stopRecording(); 
			produit = carte.recognize(Path);
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run()
				{
					try {
						Client.commande(produit);
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
			if (sv!= null) {
				SavePicture();
			}
			Intent Activite2 = new Intent(MainActivity.this, AfficheCarteActivity.class);
			startActivity(Activite2);
		}
	};
	private OnClickListener testListener = new OnClickListener(){
		public void onClick(View v){
			if (sv!= null) {
				SavePicture();
			}

			//Intent Activite3 = new Intent(MainActivity.this, CameraBis.class);
			//startActivity(Activite3);
		}
	};
	/* ######## Code photo ########*/
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

			mCamera.takePicture(null, null, pictureCallback);

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
					e.printStackTrace();
				}

				// On redémarre la prévisualisation
				//mPreview.setVisibility(View.VISIBLE);
			}
		}
	};
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
			// TODO: add more exception handling logic here
		}
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(w, h);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		mCamera.stopPreview();
		mCamera.release();
		mCamera =null;
	}
	public void onPause() {
		super.onPause();
		if(myHandler != null)
			myHandler.removeCallbacks(myRunnable); // On arrete le callback
	}

	/* ############### Code de la prise audio #############*/
	private String getFilename(){
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath,AUDIO_RECORDER_FOLDER);

		if(!file.exists()){
			file.mkdirs();
		}
		//Path = file.getAbsolutePath() + "/" + AUDIO_RECORDER_FILE_EXT_WAV;
		return (file.getAbsolutePath() + "/" + "audio" + AUDIO_RECORDER_FILE_EXT_WAV);

	}

	private String getTempFilename(){
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath,AUDIO_RECORDER_FOLDER);

		if(!file.exists()){
			file.mkdirs();
		}

		File tempFile = new File(filepath,AUDIO_RECORDER_TEMP_FILE);

		if(tempFile.exists())
			tempFile.delete();

		return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
	}

	private void startRecording(){
		recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
				RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);

		int i = recorder.getState();
		if(i==1)
			recorder.startRecording();

		isRecording = true;

		recordingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				writeAudioDataToFile();
			}
		},"AudioRecorder Thread");

		recordingThread.start();
	}

	private void writeAudioDataToFile(){
		byte data[] = new byte[bufferSize];
		String filename = getTempFilename();
		FileOutputStream os = null;

		try {
			os = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int read = 0;

		if(null != os){
			while(isRecording){
				read = recorder.read(data, 0, bufferSize);

				if(AudioRecord.ERROR_INVALID_OPERATION != read){
					try {
						os.write(data);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void stopRecording(){
		if(null != recorder){
			isRecording = false;

			int i = recorder.getState();
			if(i==1)
				recorder.stop();
			recorder.release();

			recorder = null;
			recordingThread = null;
		}

		copyWaveFile(getTempFilename(),getFilename());
		deleteTempFile();
	}

	private void deleteTempFile() {
		File file = new File(getTempFilename());

		file.delete();
	}

	private void copyWaveFile(String inFilename,String outFilename){
		FileInputStream in = null;
		FileOutputStream out = null;
		long totalAudioLen = 0;
		long totalDataLen = totalAudioLen + 36;
		long longSampleRate = RECORDER_SAMPLERATE;
		int channels = 2;
		long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

		byte[] data = new byte[bufferSize];

		try {
			in = new FileInputStream(inFilename);
			out = new FileOutputStream(outFilename);
			totalAudioLen = in.getChannel().size();
			totalDataLen = totalAudioLen + 36;

			AppLog.logString("File size: " + totalDataLen);

			WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
					longSampleRate, channels, byteRate);

			while(in.read(data) != -1){
				out.write(data);
			}

			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void WriteWaveFileHeader(
			FileOutputStream out, long totalAudioLen,
			long totalDataLen, long longSampleRate, int channels,
			long byteRate) throws IOException {

		byte[] header = new byte[44];

		header[0] = 'R';  // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f';  // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1;  // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8);  // block align
		header[33] = 0;
		header[34] = RECORDER_BPP;  // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

		out.write(header, 0, 44);
	}

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