package uk.ac.kcl.inf._5ccs2seg.MainApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * 
 * @author John Murray
 * 
 */
public class Controller implements ActionListener {

	MasterControlProgram model;
	GUI1 frame;

	public Controller(MasterControlProgram model, GUI1 frame) {
		this.model = (MasterControlProgram) model;
		this.frame = (GUI1) frame;

	}

	@Override
	public void actionPerformed(ActionEvent action) {
		String actionRequested = action.getActionCommand();
		if (actionRequested.equals("Save")) {
			if (frame.getFileName().length() < 3) {
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
			model.setSolo(true);
		}
		if (actionRequested.equals("Multi")) {
			model.setSolo(false);
		}
		if (actionRequested.equals("Explore")) {
			JOptionPane.showMessageDialog(frame,
					"We haven't implemented the exploring yet :(",
					"Sorry about that", JOptionPane.ERROR_MESSAGE);
		}
		if (actionRequested.equals("Collect")) {
			JOptionPane.showMessageDialog(frame,
					"We haven't implemented the collecting yet :(",
					"Sorry about that", JOptionPane.ERROR_MESSAGE);
		}
	}

}
