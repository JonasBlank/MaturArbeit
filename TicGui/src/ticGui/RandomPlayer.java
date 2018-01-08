package ticGui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RandomPlayer implements Player {
	private int color;

	public RandomPlayer(int col)
	{
		color = col;
	}

	@Override
	public Board play(Board board)
	{
		Random rand = new Random();
		TicGui.delay(250 +  rand.nextInt(250));

		
		List<Integer> idxs = new ArrayList<Integer>();
		for (int i = 0; i < board.pieces.length; i++)
		{
			if (board.pieces[i] == TicGui.EMPTY)
				idxs.add(i);
		}

		if (idxs.size() > 0)
		{
			int ran = idxs.get(rand.nextInt(idxs.size()));

			return board.set(ran, color);
		}
		return board;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNeuralNet()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
