package serban.stoenescu.bujor.src.symbols;

public class NormalSymbol extends Symbol {

	private SymbolTypes symbolType;
	public NormalSymbol(SymbolTypes symbolType)
	{		
		this.symbolType = symbolType;
	}
	@Override
	public SymbolTypes getSymbolType() {
		return symbolType;
	}

}
