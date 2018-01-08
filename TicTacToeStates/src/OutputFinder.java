import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class OutputFinder
{
	public static final int PLAYER_X = 1;
	public static final int PLAYER_O = 2;
	public static final int EMPTY = 0;
	
	public static Board findOutputForState(Board state, int player)
	{
		int field;
		
		if((field = canWinInOneGo(state, player)) != -1)
			return state.set(field, player);
		
		if((field = canWinInOneGo(state, getEnemy(player))) != -1)
			return state.set(field, player);
		
		if((field = canChooseMiddleField(state)) != -1)
			return state.set(field, player);
		
		if((field = chooseRandomEmptyField(state)) != -1)
			return state.set(field, player);
		
		return null;
	}
	
	public static Board findOutputForStateSimple(Board state, int player)
	{
		int field;
		
		if((field = canWinInOneGo(state, player)) != -1)
			return new Board(false).set(field, player);
		
		if((field = canWinInOneGo(state, getEnemy(player))) != -1)
			return new Board(false).set(field, player);
		
		if((field = canChooseMiddleField(state)) != -1)
			return new Board(false).set(field, player);
		
		if((field = chooseRandomEmptyField(state)) != -1)
			return new Board(false).set(field, player);
		
		return null;
	}
	
	//Testet, ob das mittlere Feld frei ist
	public static int canChooseMiddleField(Board state)
	{
		if(state.pieces[4] == EMPTY)
			return 4;
		
		return -1;
	}

	//Wählt ein zufälliges Feld aus
	public static int chooseRandomEmptyField(Board board)
	{
		List<Integer> idxes = new ArrayList<Integer>();
		for(int i = 0; i < board.pieces.length; i++)
		{
			if(board.pieces[i] == EMPTY)
			{
				idxes.add(i);
			}
		}
		Random rand = new Random();
		if(idxes.size() == 0)
			return -1;
		
		return idxes.get(rand.nextInt(idxes.size()));
	}
	
	//Testet, ob der jeweilige Spieler in einem Zug gewinnen kann
	public static int canWinInOneGo(Board board, int player)
	{
		int winningField = -1;
		
		for(int i = 0; i < board.pieces.length; i++)
		{
			if(board.pieces[i] == EMPTY)
			{
				Board copy = new Board(board);
				copy.set(i, player);
				if(hasWon(copy, player))
					winningField = i;
			}
		}
		
		return winningField;
	}
	
	public static int getEnemy(int player)
	{
		return player == PLAYER_X ? PLAYER_O : PLAYER_X;
	}
	
	//Testet, ob jemand gewonnen hat
	public static boolean hasWon(Board board, int player)
	{
		boolean won = false;

		for (int i = 0; i < 3; i++)
		{
			if ((board.pieces[i * 3] == player) && (board.pieces[i * 3] == board.pieces[i * 3 + 1]) && (board.pieces[i * 3] == board.pieces[i * 3 + 2]))
				won = true;

			if ((board.pieces[i] == player) && (board.pieces[i] == board.pieces[i + 3]) && (board.pieces[i] == board.pieces[i + 6]))
				won = true;
		}

		if (board.pieces[0] == player && board.pieces[4] == board.pieces[0] && board.pieces[8] == board.pieces[0])
			won = true;

		if (board.pieces[2] == player && board.pieces[4] == board.pieces[2] && board.pieces[6] == board.pieces[2])
			won = true;
		
		return won;
	}
}
