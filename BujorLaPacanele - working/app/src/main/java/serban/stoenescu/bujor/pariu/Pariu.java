package serban.stoenescu.bujor.pariu;

import serban.stoenescu.bujor.src.paytable.InvalidNumberOfSymbolsException;
import serban.stoenescu.bujor.src.paytable.InvalidPositionException;
import serban.stoenescu.bujor.src.paytable.PayLineList;
import serban.stoenescu.bujor.src.paytable.TooManyLinesException;
import serban.stoenescu.bujor.src.symbols.Symbol;

public class Pariu {
	
	private static Pariu instance = null;
	private PayLineList payLineList;
	private int freeSpins = 0;
	public int getFreeSpins()
	{
		return freeSpins;
	}
	private Pariu() throws InvalidNumberOfSymbolsException, InvalidPositionException
	{
		payLineList = new PayLineList();
	}
	public static Pariu getInstance() throws InvalidNumberOfSymbolsException, InvalidPositionException
	{
		if(instance == null) instance = new Pariu();
		return instance;
	}
	//private final double ADJUST = 7.7;
	public final double ADJUST = 3*2; //3.5=prea mult; 3=prea putin (sau e OK?)//asta cu doua retry-uri
	//5 ok +prea mult
	//7.5 ok (9/10)
	//7.65 ok(7/10)
	//7.7 PERFECT (5/10)
	//7.75 (3/10)
	//8=foarte bun (4/10)
	//10 nok -
	private final int MAX_RETRIES = 2;
	public double getWins(Symbol[][] allSymbols,int bet,int lines) throws InvalidNumberOfSymbolsException, TooManyLinesException
	{
		freeSpins = 0;
		int retryLimit = 1+bet*2;
		int wins, retryNumber=0;;
		do
		{
			wins = payLineList.calculateWins(allSymbols, lines);
			if(wins==0)
				break;
			retryNumber++;
		}while(wins!=0 && retryNumber<retryLimit);
		if(wins!=0)
			System.out.println("Initial win = "+wins);

		double finalWins = ((double)wins*bet/ADJUST);
		//if(finalWins<1) finalWins = 1;
		return finalWins;
	}

}
