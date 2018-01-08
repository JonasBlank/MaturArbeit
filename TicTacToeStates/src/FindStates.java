import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FindStates
{

	public static void main(String[] args)
	{

		List<Board> states = findStates();
		List<Board> allowed = removeIllegals(states);
		allowed = removeWinningBoards(allowed);
		List<Board> xTurn = getXTurns(allowed);
		List<Board> oTurn = getOTurns(allowed);

		System.out.println("Number of boards (with illegals): " + states.size());
		System.out.println("Number of boards (without illegals): " + allowed.size());

		System.out.println("X turn: " + xTurn.size());
		System.out.println("O turn: " + oTurn.size());

		writeToFile("x-turn.txt", xTurn, true);
		writeToFile("x-turn_formatted.txt", xTurn, false);
		writeToFile("o-turn.txt", oTurn, true);
		writeToFile("o-turn_formatted.txt", oTurn, false);
		writeToFile("all.txt", allowed, true);
		writeToFile("all_formatted.txt", allowed, false);

		List<Board> outputOS = new ArrayList<Board>();
		for (Board board : oTurn)
		{
			Board output = OutputFinder.findOutputForStateSimple(board, OutputFinder.PLAYER_O);
			if (output != null)
				outputOS.add(output);
		}
		writeToFile("o-turn-output-simple.txt", outputOS, true);
		writeToFile("o-turn-output_formatted-simple.txt", outputOS, false);
		writeToFileIndex("o-turn-output-index.txt", outputOS, true);

		List<Board> outputXS = new ArrayList<Board>();
		for (Board board : xTurn)
		{
			Board output = OutputFinder.findOutputForStateSimple(board, OutputFinder.PLAYER_X);
			if (output != null)
				outputXS.add(output);
		}
		writeToFile("x-turn-output-simple.txt", outputXS, true);
		writeToFile("x-turn-output_formatted-simple.txt", outputXS, false);
		writeToFileIndex("x-turn-output-index.txt", outputXS, true);

		List<Board> outputO = new ArrayList<Board>();
		for (Board board : oTurn)
		{
			Board output = OutputFinder.findOutputForState(board, OutputFinder.PLAYER_O);
			if (output != null)
				outputO.add(output);
		}
		writeToFile("o-turn-output.txt", outputO, true);
		writeToFile("o-turn-output_formatted.txt", outputO, false);

		List<Board> outputX = new ArrayList<Board>();
		for (Board board : xTurn)
		{
			Board output = OutputFinder.findOutputForState(board, OutputFinder.PLAYER_X);
			if (output != null)
				outputX.add(output);
		}
		writeToFile("x-turn-output.txt", outputX, true);
		writeToFile("x-turn-output_formatted.txt", outputX, false);

	}

	public static List<Board> removeWinningBoards(List<Board> boards)
	{
		List<Board> allowed = new ArrayList<Board>();
		for (Board board : boards)
		{
			boolean won = hasWon(board);

			if (!won)
				allowed.add(board);
		}

		return allowed;
	}

	public static boolean hasWon(Board board)
	{
		boolean won = false;

		for (int i = 0; i < 3; i++)
		{
			if ((board.pieces[i * 3] != 0) && (board.pieces[i * 3] == board.pieces[i * 3 + 1]) && (board.pieces[i * 3] == board.pieces[i * 3 + 2]))
				won = true;

			if ((board.pieces[i] != 0) && (board.pieces[i] == board.pieces[i + 3]) && (board.pieces[i] == board.pieces[i + 6]))
				won = true;
		}

		if (board.pieces[0] != 0 && board.pieces[4] == board.pieces[0] && board.pieces[8] == board.pieces[0])
			won = true;

		if (board.pieces[2] != 0 && board.pieces[4] == board.pieces[2] && board.pieces[6] == board.pieces[2])
			won = true;

		return won;
	}

	public static List<Board> removeIllegals(List<Board> boards)
	{
		List<Board> allowed = new ArrayList<Board>();

		for (Board board : boards)
		{
			int xCount = 0;
			int oCount = 0;
			for (int i = 0; i < board.pieces.length; i++)
			{
				switch (board.pieces[i])
				{
				case 1:
					xCount++;
					break;
				case 2:
					oCount++;
					break;
				}
			}

			if (oCount > xCount)
				continue;

			if (xCount > (oCount + 1))
				continue;

			if (xCount + oCount == 9)
				continue;

			allowed.add(board);
		}

		return allowed;
	}

	public static List<Board> getXTurns(List<Board> boards)
	{
		List<Board> X = new ArrayList<Board>();

		for (Board board : boards)
		{
			int xCount = 0;
			int oCount = 0;
			for (int i = 0; i < board.pieces.length; i++)
			{
				switch (board.pieces[i])
				{
				case 1:
					xCount++;
					break;
				case 2:
					oCount++;
					break;
				}
			}

			if (oCount != xCount)
				continue;

			X.add(board);
		}

		return X;
	}

	public static List<Board> getOTurns(List<Board> boards)
	{
		List<Board> O = new ArrayList<Board>();

		for (Board board : boards)
		{
			int xCount = 0;
			int oCount = 0;
			for (int i = 0; i < board.pieces.length; i++)
			{
				switch (board.pieces[i])
				{
				case 1:
					xCount++;
					break;
				case 2:
					oCount++;
					break;
				}
			}

			if (oCount >= xCount)
				continue;

			O.add(board);
		}

		return O;
	}

	public static List<Board> findStates()
	{
		List<Board> boards = new ArrayList<Board>();

		boolean finished = false;
		int[] pieces = new int[9];
		Board board = new Board(pieces);

		while (!finished)
		{
			//Kreiert ein neues Board.
			for (int i = 0; i < pieces.length; i++)
			{
				pieces[i]++;
				if (pieces[i] >= 3)
				{
					pieces[i] = 0;
					finished = i == 8;
				}
				else
					break;
			}

			boolean dupe = false;
			Board testBoard = new Board(pieces);
			board.setPieces(testBoard);
			
			//Prüft, ob das Board schon in der Liste vorhanden ist, wenn man es spiegelt und rotiert
			for (int horizontalFlips = 0; horizontalFlips < 2; horizontalFlips++)
			{
				for (int verticalFlips = 0; verticalFlips < 2; verticalFlips++)
				{
					for (int rotations = 0; rotations < 4; rotations++)
					{
						for (Board fromList : boards)
						{
							if (board.equals(fromList))
							{
								dupe = true;
								break;
							}
						}
						board.rotate90();
					}
					board.flipVertical();
				}
				board.flipHorizontal();
			}

			//Wenn das board noch nicht in der Liste vorhanden ist, wird es hinzugefügt.
			if (!dupe)
				boards.add(testBoard);

		}

		return boards;
	}

	public static void writeToFile(String filename, List<Board> list, boolean raw)
	{
		PrintWriter printer = null;
		try
		{
			printer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, false), StandardCharsets.UTF_8)));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (raw)
			for (Board b : list)
				printer.println(b.getRaw());
		else
			for (Board b : list)
				printer.println(b.getFormatted());
		printer.close();
		System.out.println("Successfully written file " + filename);
	}

	public static void writeToFileIndex(String filename, List<Board> list, boolean raw)
	{
		PrintWriter printer = null;
		try
		{
			printer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, false), StandardCharsets.UTF_8)));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (raw)
			for (Board b : list)
				for (int i = 0; i < b.pieces.length; i++)
				{
					if (b.pieces[i] != OutputFinder.EMPTY)
					{
						printer.println(i + "");
						break;
					}
				}
		else
			for (Board b : list)
				printer.println(b.getFormatted());
		printer.close();
		System.out.println("Successfully written file " + filename);
	}

}
