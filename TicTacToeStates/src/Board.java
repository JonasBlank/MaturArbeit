import java.util.Arrays;
import java.util.Random;


public class Board
{

	public int[] pieces = new int[9];
	
	public Board(boolean random)
	{
		if(random)
		{
			Random rand = new Random();
			for(int i = 0; i < pieces.length; i++)
			{
				pieces[i] = rand.nextInt(3);
			}
		}
	}
	
	public Board(Board board)
	{
		this.pieces = Arrays.copyOf(board.pieces, board.pieces.length);
	}
	
	public Board(int[] pieces)
	{
		if(pieces.length < 9)
			throw new IllegalArgumentException("Board must contain 9 Pieces!");
		
		this.pieces = Arrays.copyOf(pieces, pieces.length);
	}
	
	public void setPieces(Board other)
	{
		this.pieces = Arrays.copyOf(other.pieces, other.pieces.length);
	}
	
	public String getRaw()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getChars());
		return sb.toString();
	}
	
	public String getFormatted()
	{
		StringBuilder sb = new StringBuilder();
		char[] chars = getChars();
		sb.append("|");
		for(int i = 0; i < pieces.length; i++)
		{
			sb.append(chars[i]+"|");
			if((i + 1) % 3 == 0)
			{
				sb.append("\n");
				if(i != 8)
					sb.append("|");
			}
		}
		
		return sb.toString();
	}
	
	public char[] getChars()
	{
		char[] chars = new char[pieces.length];
		for(int i = 0; i < pieces.length; i++)
		{
			switch(pieces[i])
			{
			case 0:
				chars[i] = ' ';
				break;
			case 1:
				chars[i] = 'X';
				break;
			case 2:
				chars[i] = 'O';
				break;
			}
		}
		
		return chars;
	}
	
	public Board setX(int i)
	{
		this.pieces[i] = OutputFinder.PLAYER_X;
		return this;
	}
	
	public Board setO(int i)
	{
		this.pieces[i] = OutputFinder.PLAYER_O;
		return this;
	}
	
	public Board setEmpty(int i)
	{
		this.pieces[i] = OutputFinder.EMPTY;
		return this;
	}
	
	public Board set(int i, int player)
	{
		this.pieces[i] = player;
		return this;
	}
	
	public void print()
	{
		System.out.println(getFormatted());
	}
	
	@Override
	public String toString()
	{
		return getFormatted();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(pieces);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (!Arrays.equals(pieces, other.pieces))
			return false;
		return true;
	}
	
	public void rotate90()
	{
		int[] original = Arrays.copyOf(pieces, pieces.length);
		pieces[0] = original[6];
		pieces[1] = original[3];
		pieces[2] = original[0];
		pieces[3] = original[7];
		pieces[4] = original[4];
		pieces[5] = original[1];
		pieces[6] = original[8];
		pieces[7] = original[5];
		pieces[8] = original[2];
	}
	
	public void flipHorizontal()
	{
		int[] original = Arrays.copyOf(pieces, pieces.length);
		pieces[0] = original[6];
		pieces[1] = original[7];
		pieces[2] = original[8];
		pieces[3] = original[3];
		pieces[4] = original[4];
		pieces[5] = original[5];
		pieces[6] = original[0];
		pieces[7] = original[1];
		pieces[8] = original[2];
	}
	
	public void flipVertical()
	{
		int[] original = Arrays.copyOf(pieces, pieces.length);
		pieces[0] = original[2];
		pieces[1] = original[1];
		pieces[2] = original[0];
		pieces[3] = original[5];
		pieces[4] = original[4];
		pieces[5] = original[3];
		pieces[6] = original[8];
		pieces[7] = original[7];
		pieces[8] = original[6];
	}
	
	
}
