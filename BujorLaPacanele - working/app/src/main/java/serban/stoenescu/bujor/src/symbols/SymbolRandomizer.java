package serban.stoenescu.bujor.src.symbols;

import java.util.Hashtable;
import java.util.Random;



public class SymbolRandomizer 
{
	private final int NUMBER_OF_DISTINCT_SYMBOLS = 9;
	private final int PROBABILITY_MAX = 1000;
		private double[] probabilities = {26898.0/78120915.0, 0.0, 8239679.0/78120915.0, 8241081.0/78120915.0, 
			8294234.0/78120915.0,   8237918.0/78120915.0, 8239242.0/78120915.0, 
			8233753.0/78120915.0, 28520558.0/78120915.0, 87552.0/78120915.0};
	private double thresholds[] = new double[NUMBER_OF_DISTINCT_SYMBOLS+1];
	
	
	
	
	public SymbolRandomizer()
	{
		int i;
		
		for(i=0;i<NUMBER_OF_DISTINCT_SYMBOLS;i++)
		{
			System.out.println("Random probability = "+probabilities[i]);
		}
		thresholds[0]=0;
		for(i=0; i<NUMBER_OF_DISTINCT_SYMBOLS;i++)
		{
			thresholds[i+1] = thresholds[i]+probabilities[i];
		}
		
		
		for(i=0;i<NUMBER_OF_DISTINCT_SYMBOLS+1;i++)
		{
			System.out.println("thresholds = " +thresholds[i]);
		}
	}
	
	public SymbolTypes generateRandomSymbol()
	{
		Random r = new Random();
		int value = r.nextInt(PROBABILITY_MAX);
		int i;
		for(i=0;i<NUMBER_OF_DISTINCT_SYMBOLS;i++)
		{
			if(value>=thresholds[i]*PROBABILITY_MAX && value<thresholds[i+1]*PROBABILITY_MAX)
				break;
		}
		
		//System.out.println(SymbolTypes.values()[0]);
		return SymbolTypes.values()[i];		
	}
}
