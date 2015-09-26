package serban.stoenescu.bujor.main;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class MoneyAnimation 
{
	
	private final int NUMBER_OF_COINS = 40;
	private final int SPEED = 4;
	private Coin[] coins;
	private int threshold, margine;
	private Bitmap bmp;
	public MoneyAnimation(int margine,int width,int height,int radius, Bitmap bmp)
	{
		this.margine = margine;
		this.bmp = bmp;
		Random r = new Random();
		int i;
		threshold = height;
		coins = new Coin[NUMBER_OF_COINS];
		for(i=0;i<NUMBER_OF_COINS;i++)
		{
			coins[i] = new Coin(r.nextInt(width-2*margine)+margine, r.nextInt(height), radius);
		}
	};
	public void draw(Canvas canvas, Paint paint)
	{
		int i;
		for(i=0;i<NUMBER_OF_COINS;i++)
		{
			coins[i].draw(canvas, paint, bmp);
			coins[i].update(SPEED, threshold);
		}
	}
	public boolean done()
	{
		for(int i=0;i<NUMBER_OF_COINS;i++)
			if(coins[i].isDone()==false)
				return false;
		return true;
	}
}



class Coin
{
	private int x,y, radius;
	private boolean done;
	public Coin(int x,int y, int radius)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
		done = false;
	}
	public boolean isDone()
	{
		return done;
	}
	
	public void draw(Canvas canvas,Paint paint, Bitmap bmp)
	{
		if(!done)
		{
			paint.setColor(Color.parseColor("#FFFF00"));
			
			/*RectF tmpRect = new RectF();
			tmpRect.top = y-radius;
			tmpRect.bottom = y+radius;
			tmpRect.left =  x-radius;
			tmpRect.right = x+radius;

			//This call will draw nothing
			canvas.drawCircle(tmpRect.centerX(), tmpRect.centerY(), tmpRect.width() / 2, paint);
			//This call will draw a circle
			canvas.drawOval(tmpRect, paint);
			canvas.drawCircle(x,y,radius/2,paint);*/
			canvas.drawBitmap(bmp, x, y, paint);
		}
	}
	
	public void update(int speed, int threshold)
	{
		if(y<threshold) 
		{
			y+=speed;
			done = false;
		}
		else 
		{ 
			done = true;
		}		
	}
}
