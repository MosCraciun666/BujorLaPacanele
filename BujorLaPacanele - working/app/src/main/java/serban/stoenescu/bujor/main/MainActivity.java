package serban.stoenescu.bujor.main;

import android.view.Display;
import com.google.android.gms.analytics.HitBuilders;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Random;
import android.graphics.Point;

import com.apptracker.android.track.AppTracker;
import serban.stoenescu.bujor.main.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
public class MainActivity extends Activity {

	private CanvasView canvasView = null;
	private boolean firstAdShown = false;
    private Tracker mTracker ;

	
	@Override
	protected void onResume() {
	  super.onResume();
        try {
            if(mTracker==null)
            {
                // Obtain the shared Tracker instance.
                AnalyticsApplication application = (AnalyticsApplication) getApplication();
                mTracker = application.getDefaultTracker();
            }
            mTracker.setScreenName("Image~Main");

            mTracker.send(new HitBuilders.ScreenViewBuilder().build());

            Log.v("KEY", "OnResume");
            checkForBonus();

            if (firstAdShown == false) {
                AppTracker.loadModule(context.getApplicationContext(), "inapp");
            } else {
                Random r = new Random();
                int random = r.nextInt(3);
                if (random == 0) {
                    AppTracker.loadModule(context.getApplicationContext(), "inapp");
                }
            }
        }
        catch(Exception e)
        {
            TextView textView = new TextView(this);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String message = "Exception "+e.getMessage()+" "+e.getLocalizedMessage()+" "+e.getCause()+" "+sw.toString();
            textView.setText(message);
            setContentView(textView);
        }
	}

	@Override
	protected void onPause() {
	  super.onPause();
	  Log.v("KEY","OnPause");
	  SoundManager.getInstance().activityPaused();
      if(canvasView!=null)
	     canvasView.writeFile();
	}
		  @Override
		  public boolean onKeyDown(int keyCode, KeyEvent event) 
		  {
			  Log.v("Key","KeyEvent: "+event);
		      if (keyCode == KeyEvent.KEYCODE_BACK) {
				  Log.v("Key","KeyEvent ba boule: BACK dipshit");
			
				  SoundManager.getInstance().activityPaused();
		          canvasView.writeFile();
		          return super.onKeyDown(keyCode, event);
		      }

		      return super.onKeyDown(keyCode, event);
		  }
	
	public static void playSound(Context context, int soundID)
	{      
		MediaPlayer mp = MediaPlayer.create(context, soundID); 
		mp.start();
	}
	private static SoundPool soundPool;
    private static HashMap soundPoolMap;
    
    /** Populate the SoundPool*/
    public static void initSounds(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap(3);     
        final int S1 =  R.raw.necenzurat2;
   		soundPoolMap.put( S1, soundPool.load(context, R.raw.necenzurat2, 1) );
    }
	 public static void playSoundPool(Context context, int soundID) 
	 {
		 if(soundPool == null || soundPoolMap == null){
		    initSounds(context);
		 }
		 float volume = 1.0f;// whatever in the range = 0.0 to 1.0

		 // play sound with same right and left volume, with a priority of 1, 
		 // zero repeats (i.e play once), and a playback rate of 1f
		 soundPool.play((int)soundPoolMap.get(soundID), volume, volume, 1, 0, 1f);
	  }
	  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        Log.v("SIZE","Size.x = "+width);
        Log.v("SIZE","Size.y = "+height);

        try {
            context = this;

            //if(savedInstanceState == null) {
            // Initialize Leadbolt SDK with your api key
            Log.v("LEADBOLT", "Apelez startSession");
            AppTracker.startSession(getApplicationContext(), "hfu1iFQ6mevAcxVgTY7nMGcqMm3J2hNT");
            //}
            // cache Leadbolt Ad without showing it
            Log.v("LEADBOLT", "Apelez loadModuleToCache");
            AppTracker.loadModuleToCache(getApplicationContext(), "inapp");


            CanvasView.displayWidth = width;
            CanvasView.displayHeight = height;
            CanvasView.initializeScaleFactor();
            canvasView = new CanvasView(this);

            setContentView(canvasView);
            checkForBonus();

            SoundManager.getInstance().setContext(this);
        }
        catch(Exception e)
        {
            TextView textView = new TextView(this);
            String message = "Pula = "+CanvasView.pula+"Cioara="+width+"x"+height+" Exception (onCreate) "+e.getMessage()+" "+e.getLocalizedMessage()+" "+e.getCause();
            textView.setText(message);
            setContentView(textView);
        }
    }
    
    private void checkForBonus() {
        if(canvasView!=null)
		    canvasView.checkForBonus();

	}
	private static Context context;
	public static Context getContext() {
		return context;
	}
}



