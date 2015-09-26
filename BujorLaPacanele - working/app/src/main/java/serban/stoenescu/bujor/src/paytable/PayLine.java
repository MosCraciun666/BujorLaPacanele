package serban.stoenescu.bujor.src.paytable;

import java.util.ArrayList;

import serban.stoenescu.bujor.src.symbols.Symbol;
import serban.stoenescu.bujor.src.symbols.SymbolTypes;

public class PayLine {

	private static final int REEL_HEIGHT = 3;
	private int[] positions;
	private int freeSpinsWon;
	private Symbol[] selectedSymbols;
	public PayLine(int[] positions) throws InvalidNumberOfSymbolsException, InvalidPositionException
	{
		freeSpinsWon = 0;
		if(positions.length!=PayTableEntry.NUMBER_OF_REELS)
			throw new InvalidNumberOfSymbolsException();
		int i;
		for(i=0;i<positions.length;i++)
		{
			if(positions[i]<0 || positions[i]>=REEL_HEIGHT)
				throw new InvalidPositionException("Positions["+i+"] = "+positions[i]);
		}
		this.positions = positions;
	}
	public String toString()
	{
		if(selectedSymbols==null) return "";
		String result="";
		for(int i=0;i<selectedSymbols.length;i++)
			result+=" "+selectedSymbols[i].getSymbolType();
		return result;
	}
	public int calculateWins(Symbol[][] allSymbols) throws InvalidNumberOfSymbolsException
	{
		freeSpinsWon = 0;
		int i;
		
		
		if(allSymbols.length!=REEL_HEIGHT)
			throw new InvalidNumberOfSymbolsException();
		if(allSymbols[0].length!=PayTableEntry.NUMBER_OF_REELS)
			throw new InvalidNumberOfSymbolsException();
		
		PayTable pt = new PayTable();
		
		selectedSymbols = new Symbol[positions.length];
		for(i=0;i<positions.length;i++)
		{
			int position = positions[i];
			selectedSymbols[i] = allSymbols[position][i];
		}
			
		int wins = pt.calculateWins(selectedSymbols);		
		freeSpinsWon = pt.getFreeSpinsWon();
		winningSymbols = pt.getWinningSymbols();
		return wins;
	}
	private ArrayList<SymbolTypes> winningSymbols = null;
	public ArrayList<SymbolTypes> getWinningSymbols(){return winningSymbols;}
	
	public int getFreeSpinsWon()
	{
		return freeSpinsWon;
	}
	public int[] getPositions() {
		return positions;
	}
	

}
