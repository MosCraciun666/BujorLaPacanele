package serban.stoenescu.bujor.src.paytable;

import java.util.ArrayList;

import android.util.Log;
import serban.stoenescu.bujor.src.symbols.Symbol;

public class PayLineList {

	private static ArrayList<PayLine> lines,winningLines;
	private static ArrayList<Integer> winsPerLine;
	private static ArrayList<Integer> winningLineIndexes;
	private int freeSpins;

	public static ArrayList<PayLine> getWinningLines(){return winningLines;}
	
	private void addLine(int x1,int x2,int x3,int x4,int x5) throws InvalidNumberOfSymbolsException, InvalidPositionException
	{
		int[] positions = {x1,x2,x3,x4,x5};
		PayLine p = new PayLine(positions);
		lines.add(p);
	}

    public static ArrayList<PayLine> getLines(){return lines;}
	public PayLineList() throws InvalidNumberOfSymbolsException, InvalidPositionException
	{
		lines = new ArrayList<PayLine>();
		winningLines = new ArrayList<PayLine>();
		winsPerLine = new ArrayList<Integer>();
		winningLineIndexes = new ArrayList<Integer>();
		freeSpins = 0;
		
		//1-4
		addLine(1,1,1,1,1);
		addLine(2,2,2,2,2);
		//addLine(3,3,3,3,3);
		addLine(0,0,0,0,0);
		
		//5-8
		//addLine(1,2,3,2,1);
		addLine(2,1,0,1,2);
		//addLine(0,0,1,2,3);
		//addLine(3,3,2,1,0);
		
		//9-12
		addLine(1,0,0,0,1);
		//addLine(2,3,3,3,2);
		//addLine(0,1,2,3,3);
		//addLine(3,2,1,0,0);
		
		//13-16
		addLine(1,0,1,2,1);
		//addLine(2,3,2,1,2);
		addLine(0,1,0,1,0);
		//addLine(3,2,3,2,3);
		
		//17-20
		addLine(1,2,1,0,1);
		//addLine(2,1,2,3,1);
		addLine(0,1,1,1,0);
		//addLine(3,2,2,2,3);
		
		//21-22
		//addLine(1,1,2,3,3);
		addLine(2,2,1,0,0);
		
		//23-25
		addLine(1,1,0,1,1);
		//addLine(2,2,3,2,2);
		//addLine(1,2,2,2,3);
		
		//26-27
		addLine(2,1,1,1,0);
		addLine(0,0,1,0,0);
		//addLine(3,3,2,3,3);
		
		//29-30
		//addLine(0,1,2,2,3);
		//addLine(3,2,1,1,0);
		
		//31-34
		addLine(0,0,0,1,2);
		addLine(1,0,0,1,2);
		//addLine(2,3,3,2,1);
		//addLine(3,3,3,2,1);
		
		//35-38
		//addLine(0,1,1,2,3);
		//addLine(3,2,2,1,0);
		//addLine(1,0,1,2,3);
		//addLine(2,3,2,1,0);
		
		//39-40
		//addLine(0,1,2,3,2);
		//addLine(3,2,1,0,1);
	}
	
	public int calculateWins(Symbol[][] allSymbols,int numberOfPayLines) throws InvalidNumberOfSymbolsException, TooManyLinesException
	{
		winningLines.clear();
		winsPerLine.clear();
		winningLineIndexes.clear();
		
		int wins = 0;
		freeSpins = 0;
		if(numberOfPayLines>lines.size()) throw new TooManyLinesException();
		int i;
		for(i=0; i<numberOfPayLines;i++)
		{
			int currentWins = lines.get(i).calculateWins(allSymbols);
			if(currentWins!=0)
			{
				winningLines.add(lines.get(i));
				winsPerLine.add(currentWins);
				winningLineIndexes.add(i);
				Log.v("WINNING_LINE",lines.get(i).toString());
			}
			wins += currentWins;
			freeSpins += lines.get(i).getFreeSpinsWon();
			if(freeSpins>10)
			{
				freeSpins = 10;
			}
		}
		return wins;
	}
	
	public int getFreeSpins()
	{
		return freeSpins;
	}

	public static ArrayList<Integer> getWinsPerLine() {
		return winsPerLine;
	}

	public static ArrayList<Integer> getWinningLineIndexes() {
		return winningLineIndexes;
	}
}
