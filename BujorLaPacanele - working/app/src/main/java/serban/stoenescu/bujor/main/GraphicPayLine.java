package serban.stoenescu.bujor.main;

import java.util.ArrayList;

import main.temporary.Main;
import serban.stoenescu.bujor.pariu.Pariu;
import serban.stoenescu.bujor.src.paytable.InvalidNumberOfSymbolsException;
import serban.stoenescu.bujor.src.paytable.InvalidPositionException;
import serban.stoenescu.bujor.src.paytable.PayLine;
import serban.stoenescu.bujor.src.paytable.PayLineList;
import serban.stoenescu.bujor.src.symbols.SymbolTypes;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class GraphicPayLine {

    private static int[] colors =
            {
              Color.RED,
              Color.GREEN,
              Color.BLUE,
              Color.YELLOW,

              Color.CYAN,
              Color.MAGENTA,
              Color.parseColor("#660066"),
              Color.parseColor("#CC6600"),

              Color.parseColor("#CC3399"),
              Color.parseColor("#CC9966"),
              Color.parseColor("#666600"),
              Color.parseColor("#330033"),

              Color.parseColor("#000000"),
              Color.parseColor("#555555"),
              Color.parseColor("#999999")
            };

	private static final int NUMBER_OF_COLORS = 15;
	private static int shit=0;
	private static final int MAX_SHIT = 200;

    private static int pula = MAX_SHIT*2;

    public static void reseteazaPula(){pula = MAX_SHIT;}
    public static void punePulaPeZero(){pula = 0;}

    public static void drawAllLines(Canvas canvas, Paint paint, int startX,int endX,int[] ySymbol,int maxY,int yCorrection, int radius,int numberOfLinesSelected) throws InvalidNumberOfSymbolsException, InvalidPositionException
    {
        ArrayList<PayLine> winningLines = PayLineList.getLines();

        if(winningLines == null || pula == 0)
        {
            string = "";
            return;
        }

        try
        {
            for(int i=0;i<numberOfLinesSelected;i++) {
                paint.setColor(colors[i]);

                boolean[] winning = new boolean[5];
                for (int j = 0; j < 5; j++)
                    winning[j] = false;

                draw(canvas, paint, winningLines.get(i).getPositions(), startX, endX, ySymbol, maxY, yCorrection, radius, winning);
            }
        }
        catch(IndexOutOfBoundsException e)
        {
            //ignore, this is normal
        }
        if(pula>0)
            pula--;
    }

    public static void drawWinningLines(Canvas canvas, Paint paint, int startX,int endX,int[] ySymbol,int maxY,int yCorrection, int radius,int bet) throws InvalidNumberOfSymbolsException, InvalidPositionException
	{
		ArrayList<PayLine> winningLines = PayLineList.getWinningLines();
		ArrayList<Integer> winsPerLine = PayLineList.getWinsPerLine();
		ArrayList<Integer> winningLineIndexes = PayLineList.getWinningLineIndexes();
		
		if(winningLines == null) 
		{
			string = "";
			return;
		}
		int i = currentLine;

		    paint.setColor(colors[i]);

			try
			{
				ArrayList<SymbolTypes> winningSymbols = winningLines.get(i).getWinningSymbols();
				float actualWins = (float)(winsPerLine.get(i)/Pariu.getInstance().ADJUST)*bet;
				actualWins = (int)(actualWins*100);
				actualWins = (float)actualWins/100;
				
				if(actualWins==1)
					string = "Linia "+(1+winningLineIndexes.get(i))+" iti aduce "+actualWins+" credit";
				else if(actualWins<1)
					string = "";
				else
					string = "Linia "+(1+winningLineIndexes.get(i))+" iti aduce "+actualWins+" credite";
				
				boolean[] winning = new boolean[5];
				for(int j=0;j<5;j++)
					winning[j]=false;
				for(SymbolTypes s: winningSymbols)
				{
					int[] pos = winningLines.get(i).getPositions();
					
					for(int j=0;j<5;j++)
					{
						if(Main.getSymbols()[j][pos[j]]==s.ordinal()|| Main.getSymbols()[j][pos[j]]==SymbolTypes.JOKER.ordinal())
						{
							winning[j]=true;
						}
					}
				}
				draw(canvas,paint,winningLines.get(i).getPositions(),startX,endX,ySymbol,maxY,yCorrection, radius,winning);
			    
			}
			catch(IndexOutOfBoundsException e)
			{
				//ignore, this is normal
			}
		shit=(shit+1)%MAX_SHIT;
		if(shit==0)
			increaseLine();
	}
	
	private static int currentLine = 0;
	private static void increaseLine()
	{
		currentLine = currentLine + 1;
		if(PayLineList.getWinningLines().isEmpty()==false)
			currentLine = currentLine % PayLineList.getWinningLines().size();
		else currentLine = 0;
	}
	private static void draw(Canvas canvas, Paint paint,int[] positions,int startX,int endX,int[] ySymbol,int maxY,int yCorrection,int radius,boolean[] whites)
	{
		paint.setStrokeWidth(5);
		int step = (endX-startX)/5;
		
		int color = paint.getColor();
		if(whites[0]==true) paint.setColor(Color.WHITE);
		else paint.setColor(color);
		canvas.drawCircle(startX,maxY-ySymbol[1+positions[0]]-yCorrection, radius/3, paint);
		paint.setColor(color);
		canvas.drawLine(startX, maxY-ySymbol[1+positions[0]]-yCorrection, startX+step, 
				maxY-ySymbol[1+positions[1]]-yCorrection, paint);//[1+positions[...]] pentru ca primul este y-ul care iese din ecran
		

		if(whites[1]==true) paint.setColor(Color.WHITE);
		else paint.setColor(color);
		canvas.drawCircle(startX+step,maxY-ySymbol[1+positions[1]]-yCorrection, radius/3, paint);
		paint.setColor(color);
		canvas.drawLine(startX+step, maxY-ySymbol[1+positions[1]]-yCorrection, 
				startX+2*step, maxY-ySymbol[1+positions[2]]-yCorrection, paint);
		

		if(whites[2]==true) paint.setColor(Color.WHITE);
		else paint.setColor(color);
		canvas.drawCircle(startX+2*step,maxY-ySymbol[1+positions[2]]-yCorrection, radius/3, paint);
		paint.setColor(color);
		canvas.drawLine(startX+2*step, maxY-ySymbol[1+positions[2]]-yCorrection,
				startX+3*step, maxY-ySymbol[1+positions[3]]-yCorrection, paint);
		

		if(whites[3]==true) paint.setColor(Color.WHITE);
		else paint.setColor(color);
		canvas.drawCircle(startX+3*step,maxY-ySymbol[1+positions[3]]-yCorrection, radius/3, paint);
		paint.setColor(color);
		canvas.drawLine(startX+3*step, maxY-ySymbol[1+positions[3]]-yCorrection,
				startX+4*step, maxY-ySymbol[1+positions[4]]-yCorrection, paint);
		

		if(whites[4]==true) paint.setColor(Color.WHITE);
		else paint.setColor(color);
		canvas.drawCircle(startX+4*step,maxY-ySymbol[1+positions[4]]-yCorrection, radius/3, paint);
		paint.setColor(color);
	}
	public static String string = "";
	public static String getString() {
		return string;
	}

}
