package serban.stoenescu.bujor.src.paytable;

import serban.stoenescu.bujor.src.symbols.Symbol;
import serban.stoenescu.bujor.src.symbols.SymbolTypes;

public class PayTableEntry {
	private SymbolTypes winningSymbol;
	public SymbolTypes getWinningSymbol(){return winningSymbol;}
	private int numberOfSymbolsRequired;
	public static final int NUMBER_OF_REELS = 5;
	public PayTableEntry(SymbolTypes winningSymbol,int numberOfSymbolsRequired)
	{
		this.winningSymbol = winningSymbol;
		this.numberOfSymbolsRequired = numberOfSymbolsRequired;
	}
	
	private static boolean fourSound = false;
	public static boolean getFourSound()
	{
		return fourSound;
	}
	public boolean isWinningCombination(Symbol[] symbols) throws InvalidNumberOfSymbolsException
	{
		fourSound = false;
		if(symbols.length!=NUMBER_OF_REELS)
			throw new InvalidNumberOfSymbolsException();
		int i;
		int sum = 0;
		int realSymbols = 0;
		for(i=0;i<NUMBER_OF_REELS;i++)
		{
			if(symbols[i].getSymbolType() == winningSymbol ||
					(symbols[i].getSymbolType()==SymbolTypes.JOKER && winningSymbol!=SymbolTypes.DISTRIBUTED) )
			{				
				sum++;
				if(symbols[i].getSymbolType() == winningSymbol)
				{
					realSymbols++;
				}
			}
		}
		if(sum==4 && numberOfSymbolsRequired==4 && realSymbols!=0)
			fourSound = true;
		return (sum == numberOfSymbolsRequired)&&(realSymbols!=0);
	}


}
