package serban.stoenescu.bujor.src.symbols;

public abstract class Symbol 
{
	public static final int DISTICT_SYMBOL_TYPES = 8;//see SymbolTypes enum
	public Symbol()
	{
		
	}
	
	
	public abstract SymbolTypes getSymbolType();
}
