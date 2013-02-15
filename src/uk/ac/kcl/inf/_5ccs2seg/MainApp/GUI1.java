package uk.ac.kcl.inf._5ccs2seg.MainApp;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class GUI1 extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton saveMap;
	private JButton exploreMap;
	private JButton switchSolo;
	private JButton switchMulti;
	private JButton collectGarbage;
	
	private JTextField filename;
	private JTextField x1Drop;
	private JTextField x2Drop;
	private JTextField x3Drop;
	private JTextField x4Drop;
	
	private JScrollPane mapView;
	
	private MasterControlProgram mcp;
	
	public GUI1(MasterControlProgram mcp){
		super("Robot GUI");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.mcp = mcp;
		initWidgets();
		
	}

	private void initWidgets() {
		// TODO Auto-generated method stub
		
	}

}
