package uk.ac.kcl.inf._5ccs2seg.MainApp;

/**
 * The grid representation of the map stored in a 2D array (0 - Unexplored; 1 -
 * Free; 2 - Occupied; 3 - Garbage)
 * 
 * @author Adrian Bocai, John Murray
 */
public class GridMap {

	private final int[][] grid;
	private final static int maxX = 500;
	private final static int maxY = 500;
	private final int cellsPerMeter = 4;

	// this assumes a player map no bigger than (25,17)
	public GridMap() {
		this.grid = new int[maxX][maxY];

		// initialise grid to unexplored
		for (int i = 0; i < maxX; i++) {
			for (int j = 0; j < maxY; j++) {
				grid[i][j] = 0;
			}
		}

	}

	/**
	 * Set the status of a cell, give array indexes
	 * 
	 * @param i
	 *            - index of the row
	 * @param j
	 *            - index of the column
	 * @param value
	 *            - the status of that cell expressed as an integer
	 */
	public synchronized void setSts(int i, int j, int value) {
		grid[i][j] = value;
	}

	/**
	 * Set the status of a cell, give player/stage coordinates
	 * 
	 * @param x
	 *            - x coordinate of cell
	 * @param y
	 *            - y coordinate of cell
	 * @param value
	 */
	public synchronized void setSts(double x, double y, int value) {
		int[] arr = coordToArrayIndexCalc(x, y);
		grid[arr[0]][arr[1]] = value;
	}

	/**
	 * Retrieves the status of a cell, by its array indexes
	 * 
	 * @param i
	 *            - index of the row
	 * @param j
	 *            - index of the column
	 * @return status of cell expressed as integer
	 */
	public synchronized int getSts(int i, int j) {
		return grid[i][j];
	}

	/**
	 * Retrieves the status of a cell, by its player/stage coordinates
	 * 
	 * @param x
	 *            - x coordinate of cell
	 * @param y
	 *            - y coordinate of cell
	 * @return status of cell expressed as integer
	 */
	public synchronized int getSts(double x, double y) {
		int[] arr = coordToArrayIndexCalc(x, y);
		return grid[arr[0]][arr[1]];
	}

	/**
	 * Returns the indexes of a cell that the coordinates reside in
	 * 
	 * @param x
	 *            - x coordinate of cell
	 * @param y
	 *            - y coordinate of cell
	 * @return an array with the coordinates of the cell
	 */
	private int[] coordToArrayIndexCalc(double x, double y) {
		int[] indexes = new int[2];
		int tempX = (int) (x * cellsPerMeter) + (maxX / 2);
		int tempY = (int) (y * cellsPerMeter) + (maxY / 2);
		indexes[0] = tempX;
		indexes[1] = tempY;
		return indexes;
	}
	
	/**
	 * Returns the coordinates of a particular cell.
	 * Assumes that whatever calls this method knows the array is [x,y]
	 * 
	 * @param x
	 *            - x coordinate of cell
	 * @param y
	 *            - y coordinate of cell
	 * @return an array with the coordinates of the cell in [x,y] format
	 */
	public double[] arrayIndexToCoordCalc(int x, int y) {
		double[] indexes = new double[2];
		double tempX = (x / cellsPerMeter) - (maxX / 2);
		double tempY = (y / cellsPerMeter) - (maxY / 2);
		indexes[0] = tempX;
		indexes[1] = tempY;
		return indexes;
	}

	private static double near(int val) {
		int[] arr = { 125, 375, 625, 875 };

		int closeVal = 0;
		int diff = 1000000;
		int nDiff;
		for (int i = 0; i < 4; i++) {
			nDiff = Math.abs(val - arr[i]);
			if (nDiff < diff) {
				diff = nDiff;
				closeVal = arr[i];
			}
		}

		double res;
		if (closeVal == 125) {
			res = 0.125;
		} else if (closeVal == 375) {
			res = 0.375;
		} else if (closeVal == 625) {
			res = 0.625;
		} else
			res = 0.875;

		return res;
	}

	/**
	 * Prints the array (will be use only by the programmers to test and debug)
	 */
	public synchronized String toString() {
		String res = "";

		for (int i = 0; i < 136; i++) {
			res = res + "\n" + "| ";

			for (int j = 0; j < 200; j++) {
				res = res + grid[i][j] + " | ";
			}
		}

		return res;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int[][] getMap(){
		return grid;
	}
}