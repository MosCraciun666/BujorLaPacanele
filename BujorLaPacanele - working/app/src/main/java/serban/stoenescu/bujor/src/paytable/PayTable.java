package serban.stoenescu.bujor.src.paytable;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;
import serban.stoenescu.bujor.src.symbols.Symbol;
import serban.stoenescu.bujor.src.symbols.SymbolTypes;

public class PayTable {

	private ArrayList<PayTableEntry> entries;
	private ArrayList<Integer> winnings;
	private int freeSpinsWon;
	private static final int FREE_SPINS_WIN = 10;
	
	public PayTable()
	{
		freeSpinsWon = 0;
		entries = new ArrayList<PayTableEntry>();
		winnings = new ArrayList<Integer>();
		
		entries.add(new PayTableEntry(SymbolTypes.NORMAL1,3));
		winnings.add(5);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL1,4));
		winnings.add(15);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL1,5));
		winnings.add(100);
		
		entries.add(new PayTableEntry(SymbolTypes.NORMAL2,3));
		winnings.add(5);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL2,4));
		winnings.add(25);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL2,5));
		winnings.add(150);
		
		entries.add(new PayTableEntry(SymbolTypes.NORMAL3,3));
		winnings.add(50);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL3,4));
		winnings.add(100);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL3,5));
		winnings.add(250);
		
		//entries.add(new PayTableEntry(SymbolTypes.NORMAL4,2));
		//winnings.add(10);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL4,3));
		winnings.add(30);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL4,4));
		winnings.add(200);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL4,5));
		winnings.add(1000);
		
		///entries.add(new PayTableEntry(SymbolTypes.NORMAL5,2));
		///winnings.add(50);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL5,3));
		winnings.add(100);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL5,4));
		winnings.add(500);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL5,5));
		winnings.add(2000);
		
		///entries.add(new PayTableEntry(SymbolTypes.NORMAL6,2));
		///winnings.add(200);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL6,3));
		winnings.add(500);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL6,4));
		winnings.add(1000);
		entries.add(new PayTableEntry(SymbolTypes.NORMAL6,5));
		winnings.add(5000);
		
		entries.add(new PayTableEntry(SymbolTypes.JOKER,5));
		winnings.add(10000);
		
		//wins free spins
		entries.add(new PayTableEntry(SymbolTypes.DISTRIBUTED,3));
		winnings.add(0);
		entries.add(new PayTableEntry(SymbolTypes.DISTRIBUTED,4));
		winnings.add(0);
		entries.add(new PayTableEntry(SymbolTypes.DISTRIBUTED,5));
		winnings.add(0);	
	}
	
	private static boolean fourSound = false;
	public static boolean getFourSound(){return fourSound;}
	public static void resetFourSound(){fourSound = false;}
	private final int[] winsForScatteredSymbol = {25,50,75,100};
	private ArrayList<SymbolTypes> winningSymbols = new ArrayList<SymbolTypes>();
	public ArrayList<SymbolTypes> getWinningSymbols(){return winningSymbols;}
	public int calculateWins(Symbol[] symbols) throws InvalidNumberOfSymbolsException
	{
		if(symbols.length!=PayTableEntry.NUMBER_OF_REELS)
			throw new InvalidNumberOfSymbolsException();
	

		winningSymbols.clear();
		
		freeSpinsWon = 0;
		int wins = 0;
		
		//fourSound = false;
		
		int i;
		for(i=0;i<entries.size();i++)
		{
			PayTableEntry e = entries.get(i);
			if(i>=entries.size()-3)//scattered symbol shit
			{
				if(e.isWinningCombination(symbols))
				{
					freeSpinsWon = FREE_SPINS_WIN;
					winningSymbols.add(e.getWinningSymbol());
				}
			}
			else
			{
				if(e.isWinningCombination(symbols))
				{
					wins += winnings.get(i);
					
					//System.out.println(e);
				}

				if(e.getFourSound())
				{
					Log.v("e.FOURSOUND","e.FOURSOUND: "+e.getFourSound());
					fourSound = true;
				}
			}
			Log.v("FOURSOUND","FOURSOUND . Finally: "+fourSound);
		}
		
		Random r = new Random();
		for(i=0;i<PayTableEntry.NUMBER_OF_REELS;i++)
		{
			if(symbols[i].getSymbolType()==SymbolTypes.DISTRIBUTED)
			{
				int index = r.nextInt(winsForScatteredSymbol.length);
				wins += winsForScatteredSymbol[index];
			}
		}
		
		return wins;
	}
	
	
	public int getFreeSpinsWon()
	{
		return freeSpinsWon;
	}
	
	
}
