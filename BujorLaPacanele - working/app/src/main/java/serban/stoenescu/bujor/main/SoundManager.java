package serban.stoenescu.bujor.main;

import java.util.ArrayList;
import java.util.Random;

import serban.stoenescu.bujor.main.R;

import serban.stoenescu.bujor.src.paytable.PayTable;
import serban.stoenescu.bujor.src.paytable.PayTableEntry;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class SoundManager 
{
	private static SoundManager instance = null;
	private ArrayList<Integer> suneteNecenzurate;
	private ArrayList<Integer> suneteCenzurate;
	private Context context = null;
	private Random r;
	private SoundPool soundPool;
	private boolean backgroundSoundIsPlaying = false;
	
	private int lastWinSound = -1,lastLoseSound = -1;
	private boolean lastWon = false;
	
	private SoundManager()
	{
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		

		r = new Random();
		suneteNecenzurate = new ArrayList<Integer>();
		suneteNecenzurate.add(R.raw.necenzurat2);
		suneteNecenzurate.add(R.raw.necenzurat3);
		suneteNecenzurate.add(R.raw.necenzurat4);
		suneteNecenzurate.add(R.raw.necenzurat5);
		suneteNecenzurate.add(R.raw.necenzurat6);
		suneteNecenzurate.add(R.raw.necenzurat7);
		suneteNecenzurate.add(R.raw.necenzurat8);
		suneteNecenzurate.add(R.raw.necenzurat9);
		
 
		suneteCenzurate = new ArrayList<Integer>();
		suneteCenzurate.add(R.raw.cenzurat2);
		suneteCenzurate.add(R.raw.cenzurat3);
		suneteCenzurate.add(R.raw.cenzurat4);
		suneteCenzurate.add(R.raw.cenzurat5);
		suneteCenzurate.add(R.raw.cenzurat6);
		suneteCenzurate.add(R.raw.cenzurat7);
		suneteCenzurate.add(R.raw.cenzurat8);
		suneteCenzurate.add(R.raw.cenzurat9);

		setContext(MainActivity.getContext());
		playSoundLoop(R.raw.busy);
		backgroundSoundIsPlaying = true;
		
	}
	public void setContext(Context context)
	{
		this.context=context;
	}
	
	public static SoundManager getInstance()
	{
		if(instance==null)
		{
			instance = new SoundManager();
		}
		return instance;
	}
	
	public SoundPool getSoundPool()
	{
		return soundPool;
	}
	
	public void activityPaused()
	{
		soundPool.autoPause();
		backgroundSoundIsPlaying = false;
	}
	
	void playStartSound()
	{
		int x = r.nextInt(4);
		playSound(R.raw.quarter);
		if(x==0)
			playSound(R.raw.start);

		if(backgroundSoundIsPlaying==false)
		{
			soundPool.autoResume();
			//playSoundLoop(R.raw.busy);
		}
	}
	
	private void playSoundMedia(int soundID)
	{      
		MediaPlayer mp = MediaPlayer.create(context, soundID); 
		mp.start();
	}
	public void playSound(int soundID) 
	{
		soundID = soundPool.load(context, soundID, 1);
		//TODO: check if sound was loaded!
		//soundPool.play(soundID, 1.0f, 1.0f, 1, 0, 1f);
		int streamID = -1;
		do {
		    streamID = soundPool.play(soundID, 1.0f, 1.0f, 1, 0, 1f);
		} while(streamID==0);
	}
	public void playSoundLoop(int soundID) 
	{
		soundID = soundPool.load(context, soundID, 1);
		//TODO: check if sound was loaded!
		//soundPool.play(soundID, 1.0f, 1.0f, 1, 0, 1f);
		int streamID = -1;
		do {
		    streamID = soundPool.play(soundID, 0.3f, 0.15f, 1, -1, 1f);
		} while(streamID==0);
	}
		    

	public void playWinningSound(int cash,int paidSum,boolean cenzurat)
	{
		playSound(R.raw.cash);

		lastWon = true;
		
		int x = r.nextInt(3);
		if(x!=0 && cash < (int)(paidSum*0.66)) return;
		
		int newSound=-1;
		do
		{
			newSound = r.nextInt(suneteNecenzurate.size());//poti sa pui si cenzurate, aceeasi dimensiune trebuie sa o aiba lista
			
		}while(newSound == lastWinSound || newSound==5);
		lastWinSound = newSound;
		
		if(PayTable.getFourSound())
			lastWinSound = 5;
		Log.v("FOURSOUND","FOURSOUND: "+PayTable.getFourSound());
		
		if(cenzurat)
			playSound(suneteCenzurate.get(lastWinSound));
		else 	playSound(suneteNecenzurate.get(lastWinSound));
		//playSound(suneteNecenzurate.get(0));
	}
	public void playLosingSound() {
		int x = r.nextInt(3);
		if(x==0) return;
		x = r.nextInt(2);
		if(x==lastLoseSound)
			x=1-lastLoseSound;
		if(lastWon==true)
		{
		if(x==0)
			playSound(R.raw.nucastiga1);
		else playSound(R.raw.nucastiga2);
		}
		lastWon = false;
		lastLoseSound = x;
	}
	public void playStopSound() {
		playSound(R.raw.stop);
		
	}

}
