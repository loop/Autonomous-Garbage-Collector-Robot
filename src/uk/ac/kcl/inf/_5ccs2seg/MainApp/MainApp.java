package uk.ac.kcl.inf._5ccs2seg.MainApp;


public class MainApp {

	/**
	 * @param args
	 * @author John Murray, Adrian Bocai for Team Dijkstra
	 * 
	 */
	public static void main(String[] args) {
		
		MasterControlProgram mcp = new MasterControlProgram();
		Input.ValidateInput(args,mcp);
		GUI1 window = new GUI1(mcp);
		window.setVisible(true);

		while (mcp.getArgumentOrder().size() > 0) {
			System.out.println(mcp.getArgumentOrder().remove(0));
		}

		while (mcp.getMapOutputNames().size() > 0) {
			System.out.println(mcp.getMapOutputNames().remove(0));
		}

		System.out.println("GUI value is: " + mcp.isGui());

	}

}
