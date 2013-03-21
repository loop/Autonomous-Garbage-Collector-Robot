package uk.ac.kcl.inf._5ccs2seg.logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import uk.ac.kcl.inf._5ccs2seg.gui.GUI;

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
				model.addMapOutput(frame.getFileName());
				model.saveMap();
				JOptionPane.showMessageDialog(frame, frame.getFileName()
						+ ".png saved to main application folder", "OK",
						JOptionPane.INFORMATION_MESSAGE);
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
			MasterControlProgram.setSolo(false);
		}
		if (actionRequested.equals("Multi")) {
			frame.setSolo();
			MasterControlProgram.setSolo(true);
		}
		if (actionRequested.equals("Explore")) {
			model.explore();
		}
		if (actionRequested.equals("Collect")) {
			model.addCollectionTarget(frame.x1Drop.getText(), frame.x2Drop
					.getText(), frame.x3Drop.getText(), frame.x4Drop.getText());
			model.collect();
		}
	}

}
