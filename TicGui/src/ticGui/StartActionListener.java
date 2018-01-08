package ticGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartActionListener implements ActionListener {

	private TicGui tic;
	
	public StartActionListener(TicGui tic)
	{
		this.tic = tic;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		TicGui.start(tic);
	}		
}
