package uk.ac.kcl.inf._5ccs2seg.MainApp;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

	private JLabel x1Label;
	private JLabel x2Label;
	private JLabel x3Label;
	private JLabel x4Label;

	private Controller buttonAction;
	private JScrollPane mapView;

	private MasterControlProgram mcp;

	public GUI1(MasterControlProgram mcp) {
		super("Robot GUI");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.mcp = mcp;
		buttonAction = new Controller(mcp, this);
		initWidgets();
		pack();
		setVisible(true);

	}

	private void initWidgets() {

		saveMap = new JButton("Save");
		exploreMap = new JButton("Explore");
		switchSolo = new JButton("Solo");
		switchMulti = new JButton("Multi");
		collectGarbage = new JButton("Collect");

		saveMap.addActionListener(buttonAction);
		exploreMap.addActionListener(buttonAction);
		switchSolo.addActionListener(buttonAction);
		switchMulti.addActionListener(buttonAction);
		collectGarbage.addActionListener(buttonAction);

		filename = new JTextField("Enter filename here");
		x1Drop = new JTextField(" ");
		x2Drop = new JTextField(" ");
		x3Drop = new JTextField(" ");
		x4Drop = new JTextField(" ");

		x1Label = new JLabel("x1:");
		x2Label = new JLabel("x2:");
		x3Label = new JLabel("x3:");
		x4Label = new JLabel("x4:");

		setLayout(new BorderLayout(1, 1));

		JPanel a1 = new JPanel();
		a1.add(filename);
		a1.add(saveMap);

		JPanel a2 = new JPanel();
		a2.setLayout(new BoxLayout(a2, BoxLayout.PAGE_AXIS));
		a2.add(a1);
		a2.add(exploreMap);

		JPanel a3 = new JPanel();
		a3.add(a2);
		a3.add(switchSolo);

		JPanel b1 = new JPanel();
		b1.add(x1Label);
		b1.add(x1Drop);
		b1.add(x2Label);
		b1.add(x2Drop);
		b1.add(x3Label);
		b1.add(x3Drop);
		b1.add(x4Label);
		b1.add(x4Drop);

		JPanel b2 = new JPanel();
		b2.setLayout(new BoxLayout(b2, BoxLayout.PAGE_AXIS));
		b2.add(b1);
		b2.add(collectGarbage);

		JPanel b3 = new JPanel();
		b3.add(b2);
		b3.add(switchMulti);
		
		mapView.add(new MapView(mcp));

		add(a3, BorderLayout.NORTH);
		add(b3, BorderLayout.SOUTH);
		add(mapView, BorderLayout.CENTER);

	}

	public String getFileName() {
		return filename.getText();
	}

}
