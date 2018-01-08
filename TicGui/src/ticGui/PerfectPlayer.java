package ticGui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PerfectPlayer implements Player {

	public static final URL O_TURN =  TicGui.class.getResource("/resources/data/o-turn.txt");
	public static final URL X_TURN =  TicGui.class.getResource("/resources/data/x-turn.txt");
	public static final URL O_TURN_OUTPUT =  TicGui.class.getResource("/resources/data/o-turn-output.txt");
	public static final URL X_TURN_OUTPUT =  TicGui.class.getResource("/resources/data/x-turn-output.txt");
	private int color;

	public static Map<Board, Board> lookup;

	
	public PerfectPlayer(int col)
	{
		color = col;
		if(lookup == null)
		{
			lookup = new HashMap<Board, Board>();
			loadLookup();
		}
	}

	@Override
	public Board play(Board board)
	{
		Random rand = new Random();
		TicGui.delay(350 + rand.nextInt(300));
		Board originalBoard = new Board(board);
		HashMap<Board, Board> clone = new HashMap<Board, Board>();
		for(Board b : lookup.keySet())
		{
			clone.put(new Board(b), new Board(lookup.get(b)));
		}
		
		for (int hflips = 0; hflips < 2; hflips++)
		{
			for (int vflips = 0; vflips < 2; vflips++)
			{
				for (int rot = 0; rot < 4; rot++)
				{
					for (Board fromList : clone.keySet())
					{
						if (board.equals(fromList))
						{
							Board solution = clone.get(board);
							
							for (int hflips1 = 0; hflips1 < 2; hflips1++)
							{
								for (int vflips1 = 0; vflips1 < 2; vflips1++)
								{
									for (int rot1 = 0; rot1 < 4; rot1++)
									{
										int acc = originalBoard.compare(solution);
										if(acc == 8)
											return solution;
										solution.rotate90();
									}
									solution.flipVertical();
								}
								solution.flipHorizontal();
							}
						}
					}
					board.rotate90();
				}
				board.flipVertical();
			}
			board.flipHorizontal();
		}
		
		return originalBoard;
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
		return false;
	}

	@Override
	public boolean isNeuralNet()
	{
		return false;
	}
	
	public static void loadLookup()
	{
		try
		{
			List<String> o_input = Files.readAllLines(Paths.get(TicGui.getURI(O_TURN)));
			List<String> x_input = Files.readAllLines(Paths.get(TicGui.getURI(X_TURN)));
			List<String> o_output = Files.readAllLines(Paths.get(TicGui.getURI(O_TURN_OUTPUT)));
			List<String> x_output = Files.readAllLines(Paths.get(TicGui.getURI(X_TURN_OUTPUT)));

			for (int i = 0; i < o_input.size(); i++)
			{
				Board in = Board.fromString(o_input.get(i));
				Board out = Board.fromString(o_output.get(i));

				if (in != null && out != null)
				{
					lookup.put(in, out);
				}
			}

			for (int i = 0; i < x_input.size(); i++)
			{
				Board in = Board.fromString(x_input.get(i));
				Board out = Board.fromString(x_output.get(i));

				if (in != null && out != null)
				{
					lookup.put(in, out);
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
