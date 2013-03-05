package uk.ac.kcl.inf._5ccs2seg.MainApp;

import java.util.LinkedList;
import java.util.List;

public class MasterControlProgram {

	private boolean gui = false;
	private boolean solo = true;
	private int numberOfMaps = 0;
	private List<String> argumentOrder = new LinkedList<String>();
	private List<String> mapOutputNames = new LinkedList<String>();

	// Bot cleaner1 = new Bot(0, false);
	// Bot cleaner2 = new Bot(1, false);
	// Bot cleaner3 = new Bot(2, false);

	private GridMap mapRepresentation = new GridMap();

	private int maxSizeOfX = mapRepresentation.getMaxX();
	private int maxSizeOfY = mapRepresentation.getMaxY();

	public MasterControlProgram() {

		// Initialise gridmap

		for (int i = 0; i < maxSizeOfY; i++) {
			mapRepresentation.setSts(34, i, 2);
		}
	}

	public void setSolo(boolean solo) {
		this.solo = solo;
	}

	public void setGui(boolean gui) {
		this.gui = gui;
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

	/*
	 * public void setCleaner1(Bot cleaner1) { this.cleaner1 = cleaner1; }
	 * 
	 * public void setCleaner2(Bot cleaner2) { this.cleaner2 = cleaner2; }
	 * 
	 * public void setCleaner3(Bot cleaner3) { this.cleaner3 = cleaner3; }
	 */
	public boolean getSolo() {
		return solo;
	}

	public boolean getGUI() {
		return gui;
	}

	public int[][] getMap() {
		return mapRepresentation.getMap();
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

	/*
	 * public Bot getCleaner1() { return cleaner1; }
	 * 
	 * public Bot getCleaner2() { return cleaner2; }
	 * 
	 * public Bot getCleaner3() { return cleaner3; }
	 */
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
