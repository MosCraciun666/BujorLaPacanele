package serban.stoenescu.nivele;

import android.util.Log;
import serban.stoenescu.bujor.main.CanvasView;

public class Levels {
	private static Levels instance = null;
	private int xp = 0;
	private int level = 1;
	public CanvasView caller = null;
	private Levels()
	{		
	}
	public static Levels getInstance()
	{
		if(instance == null)
			instance = new Levels();
		return instance;
	}
	
	private int levelXpLimits(int lev)
	{
		//Log.v("PIZDA MOARTA","XP limita = "+((40+lev*10)*level));
		return (100+lev*100)*lev+1;
		
		//return 3*lev;
	}
	public int getNextLevelXp()
	{
		return levelXpLimits(level);
	}
	public int getPreviousLevelXp()
	{
		return levelXpLimits(level-1);
	}
	public void addXp(int amount)
	{
		xp+=amount;
		Log.v("PIZDA MOARTA"," xp = "+xp+" limita = "+levelXpLimits(level));
		if(xp>levelXpLimits(level))
		{
			level++;
			caller.showDialog();
		}
	}
	public int getXp(){return xp;}
	
	public int getLevel(){return level;}
	public void setLevel(int level) {
		this.level = level;
		
	}
	public void setXp(int xp) {
		this.xp = xp;
		
	}
}
