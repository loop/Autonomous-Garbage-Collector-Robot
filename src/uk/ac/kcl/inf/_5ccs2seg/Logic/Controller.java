package uk.ac.kcl.inf._5ccs2seg.Logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import uk.ac.kcl.inf._5ccs2seg.GUI.GUI;

/**
 * 
 * @author John Murray
 * 
 */
public class Controller implements ActionListener {

	MasterControlProgram model;
	GUI frame;

	public Controller(MasterControlProgram model, GUI frame) {
		this.model = (MasterControlProgram) model;
		this.frame = (GUI) frame;

	}

	@Override
	public void actionPerformed(ActionEvent action) {
		String actionRequested = action.getActionCommand();
		if (actionRequested.equals("Save")) {
			if (frame.getFileName().length() > 3) {
				model.addMapname(frame.getFileName());
			} else {
				JOptionPane
						.showMessageDialog(
								frame,
								"Please enter a filename with more than 3 characters containing only leters, numbers or periods/full stops.",
								"Filename error", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (actionRequested.equals("Solo")) {
			frame.setMulti();
			model.setSolo(true);
			model.getGrid().setSts(9, 11, 3);
			for(int y = 0; y < 10; y++){
				for(int x = 0; x < 20; x++){
					System.out.print(model.getGrid().getSts(x, y));
				}
				System.out.println(" ");
			}
			frame.update();
		}
		if (actionRequested.equals("Multi")) {
			frame.setSolo();
			model.setSolo(false);
			frame.update();
		}
		if (actionRequested.equals("Explore")) {
			model.explore();
		}
		if (actionRequested.equals("Collect")) {
			JOptionPane.showMessageDialog(frame,
					"We haven't implemented the collecting yet :(",
					"Sorry about that", JOptionPane.ERROR_MESSAGE);
		}
	}

}
