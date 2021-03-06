package uk.ac.kcl.inf._5ccs2seg.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import uk.ac.kcl.inf._5ccs2seg.data.Bot;
import uk.ac.kcl.inf._5ccs2seg.data.GridMap;
import uk.ac.kcl.inf._5ccs2seg.gui.GUI;

public class MasterControlProgram {

	private boolean gui = false;
	private GUI frame;
	private static boolean solo = true;
	private boolean mapped = false;
	private boolean maping = false;
	private boolean collecting = false;
	private boolean finished = false;
	private boolean wallF = false;
	private boolean explore = false;
	private boolean collect = false;
	private int numberOfMaps = 0;
	private ArrayList<double[]> garbageL;
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
		if (bot == 0) {
			return cleaner1;
		}
		if (bot == 1) {
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
		if (!getMaping() && !getMapped()) {
			if (!solo) {
				cleaner1 = new Bot(0, false);
				new WallFollow(cleaner1, this);
				new Explore(this, cleaner1);

				cleaner2 = new Bot(1, false);
				new WallFollow(cleaner2, this);
				new Explore(this, cleaner2);

				cleaner3 = new Bot(2, false);
				new WallFollow(cleaner3, this);
				new Explore(this, cleaner3);

			} else {
				cleaner1 = new Bot(0, false);
				
				
				new WallFollow(cleaner1, this);
				new Explore(this, cleaner1);
			}
		}
	}

	public void collect() {

		if (!getCollect()) {

			if (!solo) {

			} else {
				new Collect(this, cleaner1);
			}
		} else if (!getMaping()) {
			explore();
			collect();
		}
	}

	public void linkFrame(GUI frame) {
		this.frame = frame;
	}

	public void saveMap() {

		if (!mapOutputNames.isEmpty()) {
			String name = mapOutputNames.get(0);
			File saveFile = new File(name + ".png");

			if (gui) {
				try {
					ImageIO.write(frame.getMapImage(), "png", saveFile);
				} catch (IOException ex) {
				}
			} else {

				int red = 0xFF0000;
				int green = 0x00FF00;
				int blue = 0x0000FF;
				int black = 0x000000;
				int grey = 0x888888;
				int white = 0xFFFFFF;

				int scale = 2;
				int check = 0;

				BufferedImage backup = new BufferedImage((maxSizeOfX * scale),
						(maxSizeOfY * scale), BufferedImage.TYPE_INT_RGB);

				for (int y = 0; y < maxSizeOfY; y++) {
					for (int x = 0; x < maxSizeOfX; x++) {
						for (int scaleY = 0; scaleY < scale; scaleY++) {
							for (int scaleX = 0; scaleX < scale; scaleX++) {

								check = getGrid().getSts(x, y);

								if (check == 0) {
									backup.setRGB(((x * scale) + scaleX),
											((y * scale) + scaleY), grey);
								} else if (check == 1) {
									backup.setRGB(((x * scale) + scaleX),
											((y * scale) + scaleY), white);
								} else if (check == 2) {
									backup.setRGB(((x * scale) + scaleX),
											((y * scale) + scaleY), black);
								} else if (check == 3) {
									backup.setRGB(((x * scale) + scaleX),
											((y * scale) + scaleY), red);
								} else if (check == 4) {
									backup.setRGB(((x * scale) + scaleX),
											((y * scale) + scaleY), blue);
								}
								else if (check == 6) {
									backup.setRGB(((x * scale) + scaleX),
											((y * scale) + scaleY), white);
								}
							}
						}
					}
				}
				try {
					ImageIO.write(backup, "png", saveFile);
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
	}

	public synchronized void setWallF(boolean value) {
		wallF = value;
	}

	public synchronized boolean getWallF() {
		return wallF;
	}

	public synchronized void setMapped(boolean value) {
		mapped = value;
		frame.setMapped();
	}

	public synchronized boolean getMapped() {
		return mapped;
	}

	public synchronized void setMaping(boolean value) {
		maping = value;
	}

	public synchronized boolean getMaping() {
		return maping;
	}

	public synchronized void setCollect(boolean value) {
		collecting = value;
	}

	public synchronized boolean getCollect() {
		return collecting;
	}
	public synchronized void setFinished(boolean value) {
		finished = value;
		frame.setCollected();
	}

	public synchronized boolean getFinished() {
		return finished;
	}

	public synchronized void setGlist(ArrayList<double[]> list) {
		garbageL = list;
	}

	public synchronized ArrayList<double[]> getGlist() {
		return garbageL;
	}

	public synchronized double[] getCPoint() {
		return targetCollectionPoint;
	}

	public void runCLI() {
		while (getArgumentOrder().size() > 0) {
		String command = getArgumentOrder().remove(0);
		if (command.equals("-solo")) {
			MasterControlProgram.setSolo(true);
		}
		if (command.equals("-multi")) {
			MasterControlProgram.setSolo(false);
		}
		if (command.equals("-explore")) {
			explore = true;
			explore();
		}
		if (command.equals("-map")) {
			if (explore){
				Thread exp= new Thread() {
					public void run() {
						explore = false;
				while (!getMapped()){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
				}
				}
				
					saveMap();
					
				
					}};
					exp.start();
			}
			if(collect){
				Thread col = new Thread() {
					public void run() {
						collect = false;
				while (!getFinished()){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
				}
				}
				
				saveMap();
				
				
					}};
					col.start();
			}
			
			
		}
		if (command.equals("-collect")) {
			collect = true;
			collect();
		}
	}
		
	}
}