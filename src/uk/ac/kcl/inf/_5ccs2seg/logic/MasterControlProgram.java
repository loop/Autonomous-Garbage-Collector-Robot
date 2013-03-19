package uk.ac.kcl.inf._5ccs2seg.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import uk.ac.kcl.inf._5ccs2seg.data.Bot;
import uk.ac.kcl.inf._5ccs2seg.data.GridMap;
import uk.ac.kcl.inf._5ccs2seg.gui.GUI;

public class MasterControlProgram {

	private boolean gui = false;
	private GUI frame;
	private static boolean solo = true;
	private boolean mapped = false;
	private int numberOfMaps = 0;
	private List<String> argumentOrder = new LinkedList<String>();
	private List<String> mapOutputNames = new LinkedList<String>();

	private GridMap mapRepresentation = new GridMap();

	private double[] targetCollectionPoint = { 0.0, 0.0 };
	private double targetCollectionSize = 0;

	private Bot cleaner1;
	private Bot cleaner2;
	private Bot cleaner3;

	private int maxSizeOfX = mapRepresentation.getMaxX();
	private int maxSizeOfY = mapRepresentation.getMaxY();

	public MasterControlProgram() {

	}

	public static void setSolo(boolean s) {
		solo = s;
	}

	public void setGui(boolean gui) {
		this.gui = gui;
	}

	public void setMap(GridMap map) {
		this.mapRepresentation = map;
		frame.update();

	}

	public void setNumberOfMaps(int numberOfMaps) {
		this.numberOfMaps = numberOfMaps;
	}

	public void setArgumentOrder(List<String> argumentOrder) {
		this.argumentOrder = argumentOrder;
	}

	public void setMapOutputNames(List<String> mapOutputNames) {
		this.mapOutputNames = mapOutputNames;
	}

	public void setCleaner(int bot, Bot cleaner) {
		if (bot == 1) {
			this.cleaner1 = cleaner;
		}
		if (bot == 2) {
			this.cleaner2 = cleaner;
		}
		if (bot == 3) {
			this.cleaner3 = cleaner;
		}
	}

	public Bot getCleaner(int bot) {
		if (bot == 1) {
			return cleaner1;
		}
		if (bot == 2) {
			return cleaner2;
		}
		return cleaner3;
	}

	public static boolean getSolo() {
		return solo;
	}

	public boolean getGUI() {
		return gui;
	}

	public int[][] getMap() {
		return mapRepresentation.getMap();
	}

	public GridMap getGrid() {
		return mapRepresentation;
	}

	public void addMapOutput(String fileName) {
		numberOfMaps++;
		mapOutputNames.add(fileName);
	}

	public int getNumberOfMaps() {
		return numberOfMaps;
	}

	public int getMaxX() {
		return maxSizeOfX;
	}

	public int getMaxY() {
		return maxSizeOfY;
	}

	public List<String> getArgumentOrder() {
		return argumentOrder;
	}

	public List<String> getMapOutputNames() {
		return mapOutputNames;
	}

	/**
	 * The robot(s) will start to explore and map the environment
	 */
	public void explore() {
		if (!solo) {
			System.out.println(getSolo());
			cleaner1 = new Bot(0, false);
			new WallFollow(cleaner1);
			new Explore(this, cleaner1);

			cleaner2 = new Bot(1, false);
			new WallFollow(cleaner2);
			new Explore(this, cleaner2);

			cleaner3 = new Bot(2, false);
			new WallFollow(cleaner3);
			new Explore(this, cleaner3);
			System.out
					.println("Exploring mappig envi(muti) and probably returning data structure");
			mapped = true;
		} else {
			cleaner1 = new Bot(0, false);
			new WallFollow(cleaner1);
			new Explore(this, cleaner1);

			System.out
					.println("Exploring mappig envi(solo) and probably returning data structure");

			mapped = true;
		}
	}

	public void linkFrame(GUI frame) {
		this.frame = frame;
	}

	public void saveMap() {

		if (!mapOutputNames.isEmpty()) {
			String name = mapOutputNames.get(0);
			File saveFile = new File(name + ".png");

			if (!gui) {
				BufferedImage backup = new BufferedImage(maxSizeOfX,
						maxSizeOfY, BufferedImage.TYPE_INT_RGB);
				int red = 0xFF0000;
				int green = 0x00FF00;
				int blue = 0x0000FF;
				int black = 0x000000;
				int grey = 0x888888;
				int white = 0xFFFFFF;

				int scale = 2;
				int check = 0;
				for (int y = 0; y < (maxSizeOfY / scale); y++) {
					for (int x = 0; x < (maxSizeOfX / scale); x++) {
						for (int scaleY = 0; scaleY < scale; scaleY++) {
							for (int scaleX = 0; scaleX < scale; scaleX++) {

								check = getGrid().getSts(x, y);

								if (check == 0) {
									backup.setRGB(((x * scale) + scaleX),
											((y * scale) + scaleY), grey);
								}
								if (check == 1) {
									backup.setRGB(((x * scale) + scaleX),
											((y * scale) + scaleY), white);
								}
								if (check == 2) {
									backup.setRGB(((x * scale) + scaleX),
											((y * scale) + scaleY), black);
								}
								if (check == 3) {
									backup.setRGB(((x * scale) + scaleX),
											((y * scale) + scaleY), red);
								}
							}
						}
					}
				}
				try {
					ImageIO.write(backup, "png", saveFile);
				} catch (IOException ex) {
				}
			} else {
				try {
					ImageIO.write(frame.getMapImage(), "png", saveFile);
				} catch (IOException ex) {
				}
			}
			mapOutputNames.remove(0);
		}
	}

	private int randomNumber(int min, int max) {
		int temp = min + (int) (Math.random() * ((max - min) + 1));
		return temp;
	}

	public void randomiseMap() {
		for (int i = 0; i < maxSizeOfX; i++) {
			for (int j = 0; j < maxSizeOfY; j++) {
				mapRepresentation.setSts(i, j, randomNumber(0, 4));
			}
		}
	}

	public void addCollectionTarget(String x1, String y1, String x2, String y2) {
		double tempX1 = Double.parseDouble(x1);
		double tempY1 = Double.parseDouble(y1);
		double tempX2 = Double.parseDouble(x2);
		double tempY2 = Double.parseDouble(y2);

		targetCollectionPoint[0] = tempX1 + ((tempX2 - tempX1) / 2);
		targetCollectionPoint[1] = tempY1 + ((tempY2 - tempY1) / 2);

		if ((tempX2 - tempX1) > (tempY2 - tempY1)) {
			targetCollectionSize = Math.abs((tempY2 - tempY1));
		} else {
			targetCollectionSize = Math.abs((tempX2 - tempX1));
		}

		System.out.println("Target mid x: " + targetCollectionPoint[0]);
		System.out.println("Target mid y: " + targetCollectionPoint[1]);

		System.out.println("Targetbox size: " + targetCollectionSize);
	}

}
