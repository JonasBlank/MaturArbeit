package ticGui;

public class HumanPlayer implements Player {

	private int color;

	public HumanPlayer(int col)
	{
		color = col;
	}

	@Override
	public Board play(Board board)
	{
		return null;
	}


	@Override
	public int getColor()
	{
		return color;
	}

	@Override
	public void setColor(int c)
	{
		color = c;
	}

	@Override
	public boolean isHuman()
	{
		return true;
	}

	@Override
	public boolean isNeuralNet()
	{
		return false;
	}

}
