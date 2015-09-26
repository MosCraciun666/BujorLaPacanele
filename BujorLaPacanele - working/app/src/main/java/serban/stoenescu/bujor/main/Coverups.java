package serban.stoenescu.bujor.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Coverups {
	 public static void drawCoverUps(Canvas canvas, Paint paint, int width,int height,int dimensiuneCadru,int yTerminare,int threshold) 
	 {
		 paint.setColor(Color.WHITE);
		 canvas.drawRect(0, 0,width,dimensiuneCadru,paint);

		 canvas.drawRect(0, yTerminare,width,threshold,paint);
	 }
}
