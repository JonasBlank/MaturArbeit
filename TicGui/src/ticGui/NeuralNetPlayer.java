package ticGui;

import java.util.HashMap;
import java.util.Random;

public class NeuralNetPlayer implements Player {

	private int color;
	private static NeuralNet netO;
	private static NeuralNet netX;

	public NeuralNetPlayer(int col)
	{
		color = col;
		if(PerfectPlayer.lookup == null)
		{
			PerfectPlayer.lookup = new HashMap<Board, Board>();
			PerfectPlayer.loadLookup();
		}
		
		if(netO == null)
		{
			double[][] ihWeights = (double[][]) NeuralNet.readWeights(true, false, false);
			double[][] hoWeights = (double[][]) NeuralNet.readWeights(false, false, false);

			double hBias = (double) NeuralNet.readWeights(true, true, false);
			double oBias = (double) NeuralNet.readWeights(false, true, false);
			
			netO = new NeuralNet(9, 109, 9, ihWeights, hoWeights, hBias, oBias);
		}
		
		if(netX == null)
		{
			double[][] ihWeights = (double[][]) NeuralNet.readWeights(true, false, true);
			double[][] hoWeights = (double[][]) NeuralNet.readWeights(false, false, true);

			double hBias = (double) NeuralNet.readWeights(true, true, true);
			double oBias = (double) NeuralNet.readWeights(false, true, true);
			
			netX = new NeuralNet(9, 109, 9, ihWeights, hoWeights, hBias, oBias);
		}
	}

	@Override
	public Board play(Board board)
	{
		Random rand = new Random();
		TicGui.delay(350 + rand.nextInt(300));
		
		Board originalBoard = new Board(board);
		HashMap<Board, Board> clone = new HashMap<Board, Board>();
		for(Board b : PerfectPlayer.lookup.keySet())
		{
			clone.put(new Board(b), new Board(PerfectPlayer.lookup.get(b)));
		}
		
		NeuralNet net = null;
		if(getColor() == TicGui.PLAYER_X)
			net = netX;
		else
			net = netO;
		
		
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
							int[] ret = net.calc(board.pieces);
							Board solution = new Board(ret);
							Board actualSolution = clone.get(board);
							if(!solution.equals(actualSolution))
							{
								System.out.println("Neural net made a mistake!");
								solution = actualSolution;
							}
							
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
		return true;
	}

}
