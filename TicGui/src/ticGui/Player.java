package ticGui;

public interface Player
{

	public Board play(Board board);
	
	public int getColor();
	
	public void setColor(int c);

	public boolean isHuman();
	
	public boolean isNeuralNet();
	
	default boolean isFirstPlayer()
	{
		return getColor() == TicGui.PLAYER_X;
	}
}
