

import java.io.IOException;

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
			mcp.runCLI();
		}
	}
}