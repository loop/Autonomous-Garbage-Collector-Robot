package uk.ac.kcl.inf._5ccs2seg.logic;

import java.util.LinkedList;
import java.util.List;

import uk.ac.kcl.inf._5ccs2seg.data.GridMap;
import uk.ac.kcl.inf._5ccs2seg.gui.GUI;

public class MasterControlProgram {

	private boolean gui = false;
	private GUI frame;
	private boolean solo = true;
	private boolean mapped = false;
	private int numberOfMaps = 0;
	private List<String> argumentOrder = new LinkedList<String>();
	private List<String> mapOutputNames = new LinkedList<String>();

	private GridMap mapRepresentation = new GridMap();

	private WallFollow cleaner1;
	private WallFollow cleaner2;
	private WallFollow cleaner3;

	private int maxSizeOfX = mapRepresentation.getMaxX();
	private int maxSizeOfY = mapRepresentation.getMaxY();

	public MasterControlProgram() {

		cleaner1 = new WallFollow(0, true);

	}

	public void setSolo(boolean solo) {
		this.solo = solo;
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

	public void setCleaner(int bot, WallFollow cleaner) {
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

	public WallFollow getCleaner(int bot) {
		if (bot == 1) {
			return cleaner1;
		}
		if (bot == 2) {
			return cleaner2;
		}
		return cleaner3;
	}

	public boolean getSolo() {
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

	public void addMapname(String fileName) {
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
			new Explore(this);
			System.out
					.println("Exploring mappig envi(muti) and probably returning data structure");
			mapped = true;
		} else {
			new Explore(this);
			System.out
					.println("Exploring mappig envi(solo) and probably returning data structure");
			mapped = true;
		}
	}

	public void linkFrame(GUI frame) {
		this.frame = frame;
	}

	public void updateFrame() {
		frame.update();
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

}
