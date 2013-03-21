package uk.ac.kcl.inf._5ccs2seg.mainapp;

import uk.ac.kcl.inf._5ccs2seg.gui.GUI;
import uk.ac.kcl.inf._5ccs2seg.logic.Input;
import uk.ac.kcl.inf._5ccs2seg.logic.MasterControlProgram;

public class MainApp {

	/**
	 * @param args
	 * @author John Murray, Adrian Bocai for Team Dijkstra
	 * 
	 */
	public static void main(String[] args) {

		MasterControlProgram mcp = new MasterControlProgram();
		Input.ValidateInput(args, mcp);

		if (mcp.getGUI()) {
			GUI window = new GUI(mcp);
			mcp.linkFrame(window);
			window.setVisible(true);
		} else {
			while (mcp.getArgumentOrder().size() > 0) {
				String command = mcp.getArgumentOrder().remove(0);
				if (command.equals("-solo")) {
					MasterControlProgram.setSolo(true);
				}
				if (command.equals("-multi")) {
					MasterControlProgram.setSolo(false);
				}
				if (command.equals("-explore")) {
					mcp.explore();
				}
				if (command.equals("-map")) {
					mcp.saveMap();
				}
				if (command.equals("-collect")) {
					mcp.collect();
				}
			}
		}
	}
}