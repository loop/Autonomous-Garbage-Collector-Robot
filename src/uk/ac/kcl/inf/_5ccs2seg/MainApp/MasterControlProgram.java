package uk.ac.kcl.inf._5ccs2seg.MainApp;

import java.util.LinkedList;
import java.util.List;

public class MasterControlProgram {
	
	private boolean gui = false;
	private int numberOfMaps = 0;
	private List<String> argumentOrder = new LinkedList<String>();
	private List<String> mapOutputNames = new LinkedList<String>();
	Bot cleaner1 = new Bot(0, true);
	Bot cleaner2 = new Bot(1, false);
	Bot cleaner3 = new Bot(2, false);

	private int maxSizeOfX = 100;
	private int maxSizeOfY = 10;
	
	private int[][] mapRepresentation = new int[maxSizeOfY][maxSizeOfX];
	
	public MasterControlProgram(){
		for(int i = 0; i < maxSizeOfY; i++){
			for(int j = 0; j < maxSizeOfX; j++){
				mapRepresentation[i][j] = randomNumber(0,4);
			}
		}
		
		for(int i = 0; i < maxSizeOfY; i++){
			for(int j = 0; j < maxSizeOfX; j++){
				System.out.print(mapRepresentation[i][j]);
			}
			System.out.println();
		}
	}

	private int randomNumber(int min, int max) {
		int temp = min + (int)(Math.random() * ((max - min) + 1));
		return temp;
	}

	public void setGui(boolean gui) {
		this.gui = gui;
	}

	public boolean isGui() {
		return gui;
	}

	public void setNumberOfMaps(int numberOfMaps) {
		this.numberOfMaps = numberOfMaps;
	}

	public int getNumberOfMaps() {
		return numberOfMaps;
	}

	public void setArgumentOrder(List<String> argumentOrder) {
		this.argumentOrder = argumentOrder;
	}

	public List<String> getArgumentOrder() {
		return argumentOrder;
	}

	public void setMapOutputNames(List<String> mapOutputNames) {
		this.mapOutputNames = mapOutputNames;
	}

	public List<String> getMapOutputNames() {
		return mapOutputNames;
	}

	public void setCleaner1(Bot cleaner1) {
		this.cleaner1 = cleaner1;
	}

	public Bot getCleaner1() {
		return cleaner1;
	}

	

}
