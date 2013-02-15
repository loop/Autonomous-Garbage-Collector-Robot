package uk.ac.kcl.inf._5ccs2seg.MainApp;

/**
 * The grid representation of the map stored in a 2D array (0 - Unexplored; 1 -
 * Free; 2 - Occupied; 3 - Garbage)
 * 
 * @author Adrian Bocai
 */
public class GridMap {

	private int[][] grid;
	private int maxX = 200;
	private int maxY = 136;

	// this assumes a player map no bigger than (25,17)
	public GridMap() {
		this.grid = new int[maxY][maxX];
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
		double[] arr = locMid(x, y);
		int[] arr2 = convToIndex(arr[0], arr[1]);
		grid[arr2[0]][arr2[1]] = value;
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
		int[] arr = convToIndex(x, y);
		return grid[arr[0]][arr[1]];

	}

	/**
	 * Returns the midpoint of a cell that the coordinates reside in
	 * 
	 * @param x
	 *            - x coordinate of cell
	 * @param y
	 *            - y coordinate of cell
	 * @return an array with the coordinates of the cell
	 */
	private static double[] locMid(double x, double y) {
		int sX = 1;
		int sY = 1;
		if (x < 0) {
			sX = -1;
		}
		if (y < 0) {
			sY = -1;
		}

		x = ((int) (x * 1000));
		x = x / 1000;
		y = ((int) (y * 1000));
		y = y / 1000;
		int modX = (int) (Math.abs(x - (int) x) * 1000);
		int modY = (int) (Math.abs(y - (int) y) * 1000);

		double nModX = near(modX);
		double nModY = near(modY);
		x = Math.abs((int) x) + nModX;
		y = Math.abs((int) y) + nModY;
		x = x * sX;
		y = y * sY;

		double[] arr = new double[2];
		arr[0] = x;
		arr[1] = y;
		return arr;
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
	 * Converts the x and y coordinates of the midpoint of a cell to an i and j
	 * array index
	 * 
	 * @param x
	 *            - x coordinate of cell
	 * @param y
	 *            - y coordinate of cell
	 * @return array with arr[0] = i and arr[1] = j;
	 */
	private static int[] convToIndex(double x, double y) {
		int[] arr = new int[2];

		arr[0] = (int) ((y + 16.875) / 0.25);
		arr[1] = (int) ((x + 24.875) / 0.25);

		return arr;
	}

	/**
	 * Converts the i and j array index to the x and y coordinates of the
	 * midpoint of a cell
	 * 
	 * @param i
	 *            - index of the row
	 * @param j
	 *            - index of the column
	 * @return array with arr[0] = x and arr[1] = y;
	 */
	public static double[] convToCoord(int i, int j) {
		double[] arr = new double[2];

		arr[0] = -24.875 + 0.25 * j;
		arr[1] = -16.875 + 0.25 * i;

		return arr;
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

	/**
	 * Prints the array with the corresponding coordinates to each cell
	 */
	public synchronized String toStringCo() {
		String res = "";
		double[] arr = new double[2];
		for (int i = 0; i < 136; i++) {
			res = res + "\n" + "| ";

			for (int j = 0; j < 200; j++) {
				arr = convToCoord(i, j);
				res = res + grid[i][j] + "(" + arr[0] + " , " + arr[1] + ")"
						+ " | ";
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

}