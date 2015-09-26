package main.temporary;
import java.util.Random;

import android.util.Log;
import serban.stoenescu.bujor.pariu.Pariu;
import serban.stoenescu.bujor.src.paytable.InvalidNumberOfSymbolsException;
import serban.stoenescu.bujor.src.paytable.InvalidPositionException;
import serban.stoenescu.bujor.src.paytable.PayLine;
import serban.stoenescu.bujor.src.paytable.PayLineList;
import serban.stoenescu.bujor.src.paytable.PayTable;
import serban.stoenescu.bujor.src.paytable.TooManyLinesException;
import serban.stoenescu.bujor.src.symbols.NormalSymbol;
import serban.stoenescu.bujor.src.symbols.Symbol;
import serban.stoenescu.bujor.src.symbols.SymbolRandomizer;
import serban.stoenescu.bujor.src.symbols.SymbolTypes;


public class Main {
	
	private static int[] symbolCountZero = new int[10];
	private static int zeroCount = 0, totalSpinCount = 0;;

	private static SymbolRandomizer sr = new SymbolRandomizer();
	private static Symbol[][] symbols = null;
	public static int[][] getSymbols()
	{
		int[][] result = new int[3][5];
		int i,j;
		for(i=0;i<3;i++)
			for(j=0;j<5;j++)
			{
				Log.v("PIZDA MOARTA","Ba boule, i="+i+" j="+j);
				result[i][j]=symbols[i][j].getSymbolType().ordinal();
			}
		return result;//TODO: make this int!
	}
	public static double playARound(int bet,int lines) 
	{
		
		double wins=0;
		try 
		{
			Log.v("PIZDA MOARTA","Ba boule, imi sugi pula de cacat ce esti tu cu ma-ta");
			PayLineList pll = new PayLineList();
			
			symbols = new Symbol[3][];
			int i;
			for(i=0;i<3;i++)
				symbols[i]=new Symbol[5];
			int j;
			for(i=0;i<3;i++)
			{
				for(j=0;j<5;j++)
				{
					SymbolTypes cacat = sr.generateRandomSymbol();
					symbols[i][j] = new NormalSymbol(cacat);
				}
			}
			wins = Pariu.getInstance().getWins(symbols, bet, lines);
			totalSpinCount++;
			if(wins==0)
			{
				zeroCount++;
				for(i=0;i<3;i++)
				{
					for(j=0;j<5;j++)
					{
						symbolCountZero[symbols[i][j].getSymbolType().ordinal()]++;
					}
				}
			}
			int freeSpins = pll.getFreeSpins();
		//System.out.println("End of the spin. Won: "+wins+"$ "+freeSpins+" free spins");
		} 
		catch (InvalidNumberOfSymbolsException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidPositionException e) 
		{
			e.printStackTrace();
		} catch (TooManyLinesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wins;
	}
	
	

	private static void randomSearch() {
		int i;
		
		double credits = 1000;

		int gameNumber;
		final int RETRY = 100;
		final int SPINS = 10;
		final int betPerLine = 10;
		final int LINES = 15;

		sr =  new SymbolRandomizer();
		

		for(gameNumber  =0 ;gameNumber<RETRY;gameNumber++)
		{
			
			
			//System.err.println(gameNumber+"****************NEW GAME****************************");
			for(i=0;i<SPINS;i++)
			{
				credits-=(int)betPerLine*LINES;
				System.out.println("Scadem cu:"+((int)betPerLine*LINES));
				double sumWon ;
				int retryCount = 0;
				do
				{
					sumWon = playARound(betPerLine,LINES); 
					retryCount++;
				}while(sumWon!=0 && retryCount<2);
				credits+= sumWon;
				if(sumWon>0) System.out.println("Won $"+sumWon);
				System.out.println("Credits = "+credits);
			}
			
		}
	}

	public static void main(String[] args) 
	{
		for(int i =0; i<10;i++)
			symbolCountZero[i] = 0;
		randomSearch();
		//playARound(10,15);
		System.out.println("Am terminat la tine-n gura");
	}


}
