package uk.ac.kcl.inf._5ccs2seg.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import uk.ac.kcl.inf._5ccs2seg.logic.Controller;
import uk.ac.kcl.inf._5ccs2seg.logic.MasterControlProgram;

public class GUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton saveMap;
	private JButton exploreMap;
	private JButton switchSoloMulti;
	private JButton collectGarbage;

	private BufferedImage map;

	private MasterControlProgram mcp;

	private JTextField filename;
	public JTextField x1Drop;
	public JTextField x2Drop;
	public JTextField x3Drop;
	public JTextField x4Drop;

	private JLabel x1Label;
	private JLabel x2Label;
	private JLabel x3Label;
	private JLabel x4Label;

	private Controller buttonAction;
	private JScrollPane mapView;

	private int maxX;
	private int maxY;
	private int scale = 2;

	public GUI(MasterControlProgram mcp) {
		super("Robot GUI");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.mcp = mcp;

		maxX = (mcp.getGrid().getMaxX() * scale);
		maxY = (mcp.getGrid().getMaxY() * scale);

		buttonAction = new Controller(mcp, this);
		initWidgets();
		updateThreadSetup();
		pack();
		setVisible(true);

	}

	private void initWidgets() {

		saveMap = new JButton("Save");
		exploreMap = new JButton("Explore");
		switchSoloMulti = new JButton("Solo");
		switchSoloMulti.setPreferredSize(new Dimension(70, 40));
		collectGarbage = new JButton("Collect");

		map = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);

		saveMap.addActionListener(buttonAction);
		exploreMap.addActionListener(buttonAction);
		switchSoloMulti.addActionListener(buttonAction);
		collectGarbage.addActionListener(buttonAction);

		filename = new JTextField("Enter filename here", 20);
		x1Drop = new JTextField("", 2);
		x2Drop = new JTextField("", 2);
		x3Drop = new JTextField("", 2);
		x4Drop = new JTextField("", 2);

		x1Label = new JLabel("x1:");
		x2Label = new JLabel("y1:");
		x3Label = new JLabel("x2:");
		x4Label = new JLabel("y2:");
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
		a3.add(switchSoloMulti);

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
		mapView = new JScrollPane();

		mapView.add(new JLabel(new ImageIcon(map)));

		add(a3, BorderLayout.NORTH);
		add(new JLabel(new ImageIcon(map)), BorderLayout.CENTER);
		add(b3, BorderLayout.SOUTH);

	}

	public String getFileName() {
		return filename.getText();
	}

	public BufferedImage getMapImage() {
		return map;
	}

	public void update() {

		int red = 0xFF0000;
		int green = 0x00FF00;
		int blue = 0x0000FF;
		int black = 0x000000;
		int grey = 0x888888;
		int white = 0xFFFFFF;

		int check = 0;

		for (int y = 0; y < (maxY / scale); y++) {
			for (int x = 0; x < (maxX / scale); x++) {
				for (int scaleY = 0; scaleY < scale; scaleY++) {
					for (int scaleX = 0; scaleX < scale; scaleX++) {

						check = mcp.getGrid().getSts(x, y);

						if (check == 0) {
							map.setRGB(((x * scale) + scaleX),
									((y * scale) + scaleY), grey);
						} else if (check == 1) {
							map.setRGB(((x * scale) + scaleX),
									((y * scale) + scaleY), white);
						} else if (check == 2) {
							map.setRGB(((x * scale) + scaleX),
									((y * scale) + scaleY), black);
						} else if (check == 3) {
							map.setRGB(((x * scale) + scaleX),
									((y * scale) + scaleY), red);
						} else if (check == 4) {
							map.setRGB(((x * scale) + scaleX),
									((y * scale) + scaleY), blue);
						}
					}
				}
			}
		}
		repaint();
	}

	public void setMulti() {
		switchSoloMulti.setText("Multi");
	}

	public void setSolo() {
		switchSoloMulti.setText("Solo");
	}

	public GUI linkFrame() {
		return this;
	}

	private void updateThreadSetup() {
		Thread updateGUI = new Thread() {
			public void run() {
				while (true) {
					update();
					try {
						Thread.sleep(16);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		updateGUI.start();
	}

}
