package ticGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

public class TicGui {

	public static final int PLAYER_X = 1;
	public static final int PLAYER_O = 2;
	public static final int EMPTY = 0;

	public static final URL COL_EMPTY = TicGui.class.getResource("/resources/images/-.png");
	public static final URL COL_X = TicGui.class.getResource("/resources/images/X.png");
	public static final URL COL_O = TicGui.class.getResource("/resources/images/O.png");
	public static final URL COL_X_WIN = TicGui.class.getResource("/resources/images/XWin.png");
	public static final URL COL_O_WIN = TicGui.class.getResource("/resources/images/OWin.png");

	public int draws = 0;
	public int games = 0;
	
	private JFrame frmTicTacToe;

	public JButton[] fields = new JButton[9];
	public JComboBox<String> mode;
	public JButton start;
	public JToggleButton autorun;
	public JButton reset;
	public JLabel P1;
	public JLabel P2;
	public JLabel S1;
	public JLabel S2;
	public JButton Switch;
	public JPanel buttonPane;

	public Board current = new Board(false);
	public Player player1;
	public Player player2;

	public boolean started = false;
	public boolean isAutorun = false;

	public boolean playersTurn = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					TicGui window = new TicGui();
					window.frmTicTacToe.setVisible(true);

				} catch (Exception e)
				{
					e.printStackTrace();
				}				
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TicGui()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		player1 = new RandomPlayer(PLAYER_X);
		player2 = new NeuralNetPlayer(PLAYER_O);

		frmTicTacToe = new JFrame();
		frmTicTacToe.setTitle("Tic Tac Toe");
		frmTicTacToe.setIconImage(Toolkit.getDefaultToolkit().getImage(TicGui.class.getResource("/resources/images/icon.png")));
		frmTicTacToe.setBounds(100, 100, 616, 728);
		frmTicTacToe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buttonPane = new JPanel();
		buttonPane.setSize(600, 600);
		buttonPane.setLayout(new GridLayout(0, 3, 0, 0));

		for (int i = 0; i < fields.length; i++)
		{
			fields[i] = new JButton("");
			fields[i].setEnabled(true);
			fields[i].setIcon(new ImageIcon(COL_EMPTY));
			fields[i].addActionListener(new TileActionListener(this, i));
			buttonPane.add(fields[i]);
		}

		frmTicTacToe.getContentPane().add(buttonPane);
		JPanel menuPane = new JPanel();
		menuPane.setSize(600, 200);
		menuPane.setLocation(0, 600);

		frmTicTacToe.getContentPane().add(menuPane, BorderLayout.CENTER);
		menuPane.setLayout(null);

		P1 = new JLabel("Random");
		P1.setBounds(150, 626, 60, 14);
		menuPane.add(P1);

		P2 = new JLabel("Neural Net");
		P2.setBounds(150, 651, 60, 14);
		menuPane.add(P2);

		S1 = new JLabel("X");
		S1.setBounds(227, 626, 46, 14);
		menuPane.add(S1);

		S2 = new JLabel("O");
		S2.setBounds(227, 651, 46, 14);
		menuPane.add(S2);

		Switch = new JButton("Switch");
		Switch.setBounds(259, 636, 78, 23);
		Switch.setBackground(Color.WHITE);
		Switch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (player1.getColor() == PLAYER_X)
				{
					player1.setColor(PLAYER_O);
					player2.setColor(PLAYER_X);
					if (player2.isHuman())
						playersTurn = true;
					else
						playersTurn = false;

				} else
				{
					player1.setColor(PLAYER_X);
					player2.setColor(PLAYER_O);
					playersTurn = false;

				}

				S1.setText(player1.getColor() == PLAYER_X ? "X" : "O");
				S2.setText(player2.getColor() == PLAYER_X ? "X" : "O");

			}
		});
		menuPane.add(Switch);

		mode = new JComboBox<String>(new String[]
		{ "Random vs. Neural Net", "Random vs. Player", "Neural Net vs. Player", "AI vs. Neural Net", "AI vs. Player", "AI vs. Random"});
		mode.setToolTipText("Mode");
		mode.setBounds(11, 619, 133, 50);
		mode.setBackground(Color.WHITE);
		mode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int idx = mode.getSelectedIndex();
				switch (idx)
				{
				case 0:
					P1.setText("Random");
					P2.setText("Neural Net");
					player1 = new RandomPlayer(player1.getColor());
					player2 = new NeuralNetPlayer(player2.getColor());
					break;
				case 1:
					P1.setText("Random");
					P2.setText("Player");
					player1 = new RandomPlayer(player1.getColor());
					player2 = new HumanPlayer(player2.getColor());
					if (player2.getColor() == PLAYER_X)
						playersTurn = true;
					else
						playersTurn = false;
					break;
				case 2:
					P1.setText("Neural Net");
					P2.setText("Player");
					player1 = new NeuralNetPlayer(player1.getColor());
					player2 = new HumanPlayer(player2.getColor());
					if (player2.getColor() == PLAYER_X)
						playersTurn = true;
					else
						playersTurn = false;
					break;
				case 3:
					P1.setText("AI");
					P2.setText("Neural Net");
					player1 = new PerfectPlayer(player1.getColor());
					player2 = new NeuralNetPlayer(player2.getColor());
					break;
				case 4:
					P1.setText("AI");
					P2.setText("Player");
					player1 = new PerfectPlayer(player1.getColor());
					player2 = new HumanPlayer(player2.getColor());
					if (player2.getColor() == PLAYER_X)
						playersTurn = true;
					else
						playersTurn = false;
					break;
				case 5:
					P1.setText("AI");
					P2.setText("Random");
					player1 = new PerfectPlayer(player1.getColor());
					player2 = new RandomPlayer(player2.getColor());
					break;
				}
			}
		});
		menuPane.add(mode);

		start = new JButton("Start");
		start.setBounds(357, 626, 74, 43);
		start.setBackground(Color.WHITE);
		start.addActionListener(new StartActionListener(this));
		menuPane.add(start);

		autorun = new JToggleButton("Auto");
		autorun.setEnabled(true);
		autorun.setBounds(431, 626, 74, 43);
		autorun.setBackground(Color.WHITE);
		autorun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				isAutorun = autorun.isSelected();
			}
		});
		menuPane.add(autorun);

		reset = new JButton("Reset");
		reset.setEnabled(false);
		reset.setBounds(505, 626, 74, 43);
		reset.setBackground(Color.WHITE);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{

				start.setEnabled(true);
				reset.setEnabled(false);
				started = false;
				mode.setEnabled(true);
				Switch.setEnabled(true);
				current = new Board(false);
				if (player2.getColor() == PLAYER_X && player2.isHuman())
					playersTurn = true;
				else
					playersTurn = false;
				updateBoard();

			}
		});
		menuPane.add(reset);

	}

	public void updateBoard()
	{
		for (int i = 0; i < current.pieces.length; i++)
		{
			switch (current.pieces[i])
			{
			case EMPTY:
				setButtonIcon(fields[i], COL_EMPTY);
				break;
			case PLAYER_X:
				setButtonIcon(fields[i], COL_X);
				break;
			case PLAYER_O:
				setButtonIcon(fields[i], COL_O);
				break;
			}
		}
	}

	public boolean checkWon()
	{
		int[] p = current.pieces;
		for (int i = 0; i < 7; i += 3)
		{
			if (p[i] == p[i + 1] && p[i] == p[i + 2] && p[i] != EMPTY)
			{
				displayWinningAnimation(i, i + 1, i + 2);
				return true;
			}
		}

		for (int i = 0; i < 3; i++)
		{
			if (p[i] == p[i + 3] && p[i] == p[i + 6] && p[i] != EMPTY)
			{
				displayWinningAnimation(i, i + 3, i + 6);
				return true;
			}
		}

		if (p[0] == p[4] && p[0] == p[8] && p[0] != EMPTY)
		{
			displayWinningAnimation(0, 4, 8);
			return true;
		}

		if (p[2] == p[4] && p[2] == p[6] && p[2] != EMPTY)
		{
			displayWinningAnimation(2, 4, 6);
			return true;
		}

		return false;
	}

	public boolean checkDone()
	{
		int num0 = 0;
		for (int i = 0; i < current.pieces.length; i++)
		{
			if (current.pieces[i] != EMPTY)
				num0++;
		}
		if (num0 == 9)
		{
			delay(1000);
			start.setEnabled(true);
			reset.setEnabled(false);
			started = false;
			mode.setEnabled(true);
			Switch.setEnabled(true);
			current = new Board(false);
			if (player2.getColor() == PLAYER_X && player2.isHuman())
				playersTurn = true;
			else
				playersTurn = false;
			updateBoard();
			draws++;
			System.out.println("Draws: " + draws + ", Games: " + games);
			
			if (isAutorun)
			{
				start(this);
			}

			return true;
		}
		return false;

	}

	public static void start(TicGui tic)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				tic.start.setEnabled(false);
				tic.reset.setEnabled(true);
				tic.started = true;
				tic.mode.setEnabled(false);
				tic.Switch.setEnabled(false);
				tic.games++;

				if (!tic.player1.isHuman() && !tic.playersTurn)
				{
					tic.current = new Board(false);
					if (tic.player1.isFirstPlayer())
					{
						tic.current = tic.player1.play(tic.current);
						tic.updateBoard();
						if (tic.player2.isHuman())
							tic.playersTurn = true;
						else
						{
							while (!tic.checkDone() && !tic.checkWon())
							{
								Board new2 = tic.player2.play(tic.current);
								tic.current = new2;
								tic.updateBoard();
								if (tic.checkDone() || tic.checkWon())
									break;
								Board new1 = tic.player1.play(tic.current);
								tic.current = new1;
								tic.updateBoard();
							}
						}
					}
					else
					{
						tic.current = tic.player2.play(tic.current);
						tic.updateBoard();
						if (tic.player1.isHuman())
							tic.playersTurn = true;
						else
						{
							while (!tic.checkDone() && !tic.checkWon())
							{
								Board new2 = tic.player1.play(tic.current);
								tic.current = new2;
								tic.updateBoard();
								if (tic.checkDone() || tic.checkWon())
									break;
								Board new1 = tic.player2.play(tic.current);
								tic.current = new1;
								tic.updateBoard();
							}
						}
					}
				
				}
			}
		});
	}

	private void displayWinningAnimation(int f1, int f2, int f3)
	{
		URL winCol = current.pieces[f1] == PLAYER_X ? COL_X_WIN : COL_O_WIN;
		URL oldCol = current.pieces[f1] == PLAYER_X ? COL_X : COL_O;

		for (int i = 0; i < 5; i++)
		{
			setButtonIcon(fields[f1], winCol);
			setButtonIcon(fields[f2], winCol);
			setButtonIcon(fields[f3], winCol);
			delay(250);

			setButtonIcon(fields[f1], oldCol);
			setButtonIcon(fields[f2], oldCol);
			setButtonIcon(fields[f3], oldCol);
			delay(250);

		}

		start.setEnabled(true);
		reset.setEnabled(false);
		started = false;
		mode.setEnabled(true);
		Switch.setEnabled(true);
		current = new Board(false);
		if (player2.getColor() == PLAYER_X && player2.isHuman())
			playersTurn = true;
		else
			playersTurn = false;
		updateBoard();

		if (isAutorun)
		{
			start(this);
		}
	}

	private void setButtonIcon(JButton b, URL icon)
	{
		b.setIcon(new ImageIcon(icon));
		buttonPane.paintImmediately(buttonPane.getBounds());
	}

	public static void delay(int ms)
	{
		long targetTime = System.currentTimeMillis() + ms;
		while (targetTime > System.currentTimeMillis())
		{
			// wait
		}
	}
	
	public static URI getURI(URL url)
	{	
		if (url.getProtocol().startsWith("rsrc"))
		{
		     try
			{
				URL updatedURL = new URL("jar", url.getHost(), url.getPort(), url.getPath());
				return updatedURL.toURI();
			} catch (MalformedURLException | URISyntaxException e)
			{
				e.printStackTrace();
			}
		} else
			try
			{
				return url.toURI();
			} catch (URISyntaxException e)
			{
				e.printStackTrace();
			}
		
		return null;
	}

}
