package serban.stoenescu.bujor.main;

public class Column {
	private int passes;
	public Column()
	{
		reset();
		
	}
	public void reset()
	{
		passes = 0;
	}
	public void pass()
	{
		passes++;
	}
	public int getPasses(){return passes;}
}

