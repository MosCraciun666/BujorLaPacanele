package serban.stoenescu.bujor.main;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import main.temporary.InvalidSymbolNumberException;

public class GraphicSymbol 
{
	private int originalX,originalY;
	private int currentY;
	private int currentBitmap;
	private IconFactory iconFactory;
	public static int raza;
	private static int threshold, restartPoint;
	private Bitmap bitmap = null;
	
	public GraphicSymbol(int x,int y, int currentBitmap,IconFactory iconFactory)
	{
		originalX = x;
		originalY = y;
		currentY = y;
		this.iconFactory = iconFactory;
		this.currentBitmap = currentBitmap;
	}
	
	public static void setThreshold(int t)
	{
		threshold = t;
	}
	public static void setRestartPoint(int r)
	{
		restartPoint = r;
	}
	public void updateY(int amount,Column column,int columnNumber,int[][]preselectedIcons)
	{
		currentY = currentY + amount;
		if(currentY>threshold)
		{
			currentY = restartPoint;
			column.pass();
			Random r = new Random();
			//final int NUMBER_OF_ICONS = 6;
			//currentBitmap = r.nextInt(NUMBER_OF_ICONS);
			currentBitmap = columnNumber+1;

			int offset = 1+2*columnNumber;
			if(column.getPasses()>=offset && column.getPasses()<=2+offset) 
			{
				//currentBitmap = 6;
				currentBitmap = preselectedIcons[column.getPasses()-1-(2*columnNumber)][columnNumber];
			}
			bitmap = iconFactory.getIcon(currentBitmap);
		}
	}
	
    public void drawSymbol(Canvas canvas, Paint paint) throws InvalidSymbolNumberException
    {
    	//Log.v("ERROR",symbolNumber+". original x = " +originalX+" current y = "+currentY+" raza = "+raza);
    	    	//canvas.drawCircle(originalX,currentY, raza, paint);
    	if(bitmap!=null)
    		canvas.drawBitmap(bitmap, originalX-raza/2, currentY-raza/2, paint);
    	else 
    	{
    			bitmap = iconFactory.getIcon(currentBitmap);
    	}
    }
    
    public boolean isStopped(int speed, int[] targetY)
    {
    	int nearest = findNearest(targetY);
		if(Math.abs(currentY - targetY[nearest]) <= speed)
		{
			return true;
		}
		return false;
    }
    
    public void align(int speed, int[] targetY) {
    	int nearest = findNearest(targetY);
		if(currentY>targetY[nearest])
		{
			currentY -= speed;
		}
		else if(currentY<targetY[nearest])
		{
			currentY += speed;
		}
	}
	private int findNearest(int[] targetY) {
		int result = 0;
		int diff = 66666;
		for(int i=0;i<targetY.length;i++)
		{
			if(Math.abs(currentY-targetY[i])<diff)
			{
				result = i;
				diff = Math.abs(currentY-targetY[i]);
			}
		}
		return result ;
	}


}
