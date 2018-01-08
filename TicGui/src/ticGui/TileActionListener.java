package ticGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

public class TileActionListener implements ActionListener {

	private TicGui tic;
	private int idx;

	public TileActionListener(TicGui tic, int fieldIndex)
	{
		this.tic = tic;
		idx = fieldIndex;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				int piece = tic.current.pieces[idx];
				if (tic.player2.isHuman() && tic.started && tic.playersTurn)
				{
					if (piece == TicGui.EMPTY)
					{
						tic.current.set(idx, tic.player2.getColor());
						tic.updateBoard();
						tic.playersTurn = false;
						if (!tic.checkWon() && !tic.checkDone())
						{
							tic.current = tic.player1.play(tic.current);
							tic.updateBoard();
							tic.playersTurn = true;
							tic.checkWon();
							tic.checkDone();
						}
					}
				}
			}
		});
	}
}
