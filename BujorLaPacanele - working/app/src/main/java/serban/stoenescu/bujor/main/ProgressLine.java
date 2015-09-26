package serban.stoenescu.bujor.main;

import serban.stoenescu.nivele.Levels;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class ProgressLine {
	public static void draw(Canvas canvas,int x,int y,Paint paint)
	{
		paint.setColor(Color.RED);
		canvas.drawLine(x, y, x+100, y, paint);
		paint.setColor(Color.GREEN);
		float progress = ((float)Levels.getInstance().getXp()-(float)Levels.getInstance().getPreviousLevelXp())/
				((float)Levels.getInstance().getNextLevelXp()-(float)Levels.getInstance().getPreviousLevelXp());
		
		canvas.drawLine(x, y, x+(int)(100*progress), y, paint);
	}
}
