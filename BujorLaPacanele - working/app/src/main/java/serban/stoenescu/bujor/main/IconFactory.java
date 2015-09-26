package serban.stoenescu.bujor.main;

import java.util.Random;

import serban.stoenescu.bujor.main.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class IconFactory {

	private Bitmap[] scaledXBitmaps = new Bitmap[15];
	private Bitmap icon1,icon2,icon3,icon4,icon5,icon6,icon7;
	private Bitmap scaledicon1 = null,scaledicon2 = null,scaledicon3 = null,
				   scaledicon4 = null,scaledicon5 = null,scaledicon6 = null, scaledIcon7_bujor = null;
	private CanvasView canvasView;
	public IconFactory(CanvasView canvasView)
	{
		this.canvasView = canvasView;
	}

	public void makeBitmaps(int raza) 
	{
  
		 icon1 = BitmapFactory.decodeResource(canvasView.getResources(), R.drawable.spades_styled);
		 icon2 = BitmapFactory.decodeResource(canvasView.getResources(), R.drawable.x_mark_red);
		 icon3 = BitmapFactory.decodeResource(canvasView.getResources(), R.drawable.icon3);
		 icon4 = BitmapFactory.decodeResource(canvasView.getResources(), R.drawable.icon4);
		 icon5 = BitmapFactory.decodeResource(canvasView.getResources(), R.drawable.icon5);
		 icon6 = BitmapFactory.decodeResource(canvasView.getResources(), R.drawable.icon6);
		 icon7 = BitmapFactory.decodeResource(canvasView.getResources(), R.drawable.icon2);
		 
         Bitmap x1 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x1);
         Bitmap x2 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x2);
         Bitmap x3 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x3);
         Bitmap x4 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x4);
         Bitmap x5 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x5);
         Bitmap x6 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x6);
         Bitmap x7 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x7);
         Bitmap x8 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x8);
         Bitmap x9 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x9);
         Bitmap x10 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x10);
         Bitmap x11 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x11);
         Bitmap x12 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x12);
         Bitmap x13 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x13);
         Bitmap x14 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x14);
         Bitmap x15 = BitmapFactory.decodeResource(canvasView.getResources(),R.drawable.x15);
         

			 scaledXBitmaps[0]=Bitmap.createScaledBitmap (x1, raza*2, raza*2, true);
			 scaledXBitmaps[1]=Bitmap.createScaledBitmap (x2, raza*2, raza*2, true);
			 scaledXBitmaps[2]=Bitmap.createScaledBitmap (x3, raza*2, raza*2, true);
			 scaledXBitmaps[3]=Bitmap.createScaledBitmap (x4, raza*2, raza*2, true);
			 scaledXBitmaps[4]=Bitmap.createScaledBitmap (x5, raza*2, raza*2, true);
			 scaledXBitmaps[5]=Bitmap.createScaledBitmap (x6, raza*2, raza*2, true);
			 scaledXBitmaps[6]=Bitmap.createScaledBitmap (x7, raza*2, raza*2, true);
			 scaledXBitmaps[7]=Bitmap.createScaledBitmap (x8, raza*2, raza*2, true);
			 scaledXBitmaps[8]=Bitmap.createScaledBitmap (x9, raza*2, raza*2, true);
			 scaledXBitmaps[9]=Bitmap.createScaledBitmap (x10, raza*2, raza*2, true);
			 scaledXBitmaps[10]=Bitmap.createScaledBitmap (x11, raza*2, raza*2, true);
			 scaledXBitmaps[11]=Bitmap.createScaledBitmap (x12, raza*2, raza*2, true);
			 scaledXBitmaps[12]=Bitmap.createScaledBitmap (x13, raza*2, raza*2, true);
			 scaledXBitmaps[13]=Bitmap.createScaledBitmap (x14, raza*2, raza*2, true);
			 scaledXBitmaps[14]=Bitmap.createScaledBitmap (x15, raza*2, raza*2, true);			
         
		 
		if(scaledicon1 == null)
			scaledicon1 = Bitmap.createScaledBitmap(icon1, raza*2, raza*2, true);
		if(scaledicon2 == null)
			scaledicon2 = Bitmap.createScaledBitmap(icon2, raza*2, raza*2, true);
		if(scaledicon3 == null)
			scaledicon3 = Bitmap.createScaledBitmap(icon3, raza*2, raza*2, true);
		if(scaledicon4 == null)
			scaledicon4 = Bitmap.createScaledBitmap(icon4, raza*2, raza*2, true);
		if(scaledicon5 == null)
			scaledicon5 = Bitmap.createScaledBitmap(icon5, raza*2, raza*2, true);
		if(scaledicon6 == null)
			scaledicon6 = Bitmap.createScaledBitmap(icon6, raza*2, raza*2, true);
		if(scaledIcon7_bujor == null)
			scaledIcon7_bujor = Bitmap.createScaledBitmap(icon7, raza*2, raza*2, true);
		
	}
	
	public Bitmap getIcon(int iconNumber)
	{
		final int NUMBER_OF_ICONS = 7; 
		int trueNumber = iconNumber % NUMBER_OF_ICONS;
		switch(trueNumber)
		{
			case 0: return scaledicon1;
			case 1: 
			{
				Random r = new Random();				
				int index = r.nextInt(15);
				return scaledXBitmaps[index];
			}
			case 2: return scaledicon3;
			case 3: return scaledicon4;
			case 4: return scaledicon5;
			case 5: return scaledicon6;
			case 6: return scaledIcon7_bujor;
		}
		Log.v("icoana","Ce pula mea mi-ai dat? "+iconNumber);
		return null;
	}

}
