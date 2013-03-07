package uk.ac.kcl.inf._5ccs2seg.MainApp;

/**
 * Class that explore the map in solo mode
 * 
 * @author Adrian Bocai for Team Dijkstra
 * 
 */
public class Explore {
	private GridMap map;
	private Bot cleaner1;
	private Bot cleaner2;
	private Bot cleaner3;
	private boolean done = false;
	private int threadSleep = 16;

	public Explore(MasterControlProgram mcp) {
		map = mcp.getGrid();
		cleaner1 = mcp.getCleaner(1);
		cleaner2 = mcp.getCleaner(2);
		cleaner3 = mcp.getCleaner(3);

		
		// New thread that updates the map from the sensor readings from front
		// sensors
		Thread updateFront = new Thread() {
			public void run() {
				double range;
				double alpha;
				double x;
				double y;
				double[] arr;
				int[] arr2;
				while (!getFlag()) {
					// while (getFlag()){try {wait();} catch
					// (InterruptedException e) {}}

					// for (int i = 0; i<3; i++){
					range = cleaner1.getRange(cleaner1.FRONT_M);
					alpha = cleaner1.getYaw();
					x = cleaner1.getX();
					y = cleaner1.getY();
					arr = calcCoord(x, y, alpha, range);

					if (range < 4.97) {
						map.setSts(arr[0], arr[1], 2);
					} else {
						map.setSts(arr[0], arr[1], 1);
					}

					while (range > 0.23) {
						range = range - 0.125;
						arr = calcCoord(x, y, alpha, range);
						arr2 = map.coordToArrayIndexCalc(arr[0], arr[1]);
						if (checkCell(arr2[0], arr2[1])) {
							map.setSts(arr2[0], arr2[1], 1);
						}
					}

					// }
					try {
						Thread.sleep(threadSleep);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		updateFront.start();

		// New thread that updates the map from the sensor readings from Left
		// sensors
		Thread updateLeft = new Thread() {
			public void run() {
				double range;
				double alpha;
				double x;
				double y;
				double[] arr;
				int[] arr2;
				while (!getFlag()) {

					// for (int i = 0; i<3; i++){
					range = cleaner1.getRange(cleaner1.LEFT_M);
					alpha = Math.toRadians(cleaner1.getHead()
							+ Bot.turnBy(cleaner1.getHead(), 90));
					
					System.out.print("head " + cleaner1.getHead());
					System.out.print("head " + Bot.turnBy(cleaner1.getHead(), 90));
					System.out.println();
					
					x = cleaner1.getX();
					y = cleaner1.getY();
					int[] temp = map.coordToArrayIndexCalc(x, y);
					System.out.print("pos " + temp[0] + ' ' + temp[1] + '\n');
					System.out.println("");
					arr = calcCoord(x, y, alpha, range);

					if (range < 4.97) {
						map.setSts(arr[0], arr[1], 2);
					} else {
						map.setSts(arr[0], arr[1], 1);
					}

					while (range > 0.23) {
						range = range - 0.125;
						arr = calcCoord(x, y, alpha, range);
						arr2 = map.coordToArrayIndexCalc(arr[0], arr[1]);

						System.out.print("target set " + arr[0] + " - " + arr[1]);
						System.out.println("");
						if (checkCell(arr2[0], arr2[1])) {
							map.setSts(arr2[0], arr2[1], 1);
						}
					}
					// }
					try {
						Thread.sleep(threadSleep);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		updateLeft.start();

		// New thread that updates the map from the sensor readings from Right
		// sensors
		Thread updateRight = new Thread() {
			public void run() {
				double range;
				double alpha;
				double x;
				double y;
				double[] arr;
				int[] arr2;
				while (!getFlag()) {
					// while (getFlag()){try {wait();} catch
					// (InterruptedException e) {}}

					// for (int i = 0; i<3; i++){
					range = cleaner1.getRange(cleaner1.RIGHT_M);
					alpha = Math.toRadians(cleaner1.getHead()
							+ Bot.turnBy(cleaner1.getHead(), 270));
					x = cleaner1.getX();
					y = cleaner1.getY();
					arr = calcCoord(x, y, alpha, range);

					if (range < 4.97) {
						map.setSts(arr[0], arr[1], 2);
					} else {
						map.setSts(arr[0], arr[1], 1);
					}

					while (range > 0.23) {
						range = range - 0.125;
						arr = calcCoord(x, y, alpha, range);
						arr2 = map.coordToArrayIndexCalc(arr[0], arr[1]);
						if (checkCell(arr2[0], arr2[1])) {
							map.setSts(arr2[0], arr2[1], 1);
						}
					}
					// }
					try {
						Thread.sleep(threadSleep);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		updateRight.start();

		// New thread that updates the map from the sensor readings Back Sensors
		Thread updateBack = new Thread() {
			public void run() {
				double range;
				double alpha;
				double x;
				double y;
				double[] arr;
				int[] arr2;
				while (!getFlag()) {
					// while (getFlag()){try {wait();} catch
					// (InterruptedException e) {}}

					// for (int i = 0; i<3; i++){
					range = cleaner1.getRange(cleaner1.BACK_M);
					alpha = Math.toRadians(cleaner1.getHead()
							+ Bot.turnBy(cleaner1.getHead(), 180));
					x = cleaner1.getX();
					y = cleaner1.getY();
					arr = calcCoord(x, y, alpha, range);

					if (range < 4.97) {
						map.setSts(arr[0], arr[1], 2);
					} else {
						map.setSts(arr[0], arr[1], 1);
					}

					while (range > 0.23) {
						range = range - 0.125;
						arr = calcCoord(x, y, alpha, range);
						arr2 = map.coordToArrayIndexCalc(arr[0], arr[1]);
						if (checkCell(arr2[0], arr2[1])) {
							map.setSts(arr2[0], arr2[1], 1);
						}
					}
					// }
					try {
						Thread.sleep(threadSleep);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		updateBack.start();

		// cleaner1.setTRate(0.5);
		// cleaner1.moveTo(-3, 3);
		// try {Thread.sleep(1000000);} catch (InterruptedException e) {}
		mcp.setMap(map);

		// cleaner1.close();
		// setFlag(true);

		// System.out.println(map.imp());

	}

	private static double[] calcCoord(double x, double y, double alpha,
			double range) {

		x = x + (Math.cos(alpha) * range);
		y = y + (Math.sin(alpha) * range);
		// System.out.println(x);
		// System.out.println(y);
		// System.out.println();
		// System.out.println(range);
		return new double[] { x, y };
	}

	private synchronized boolean checkCell(int i, int j) {
		if (map.getSts(i, j) == 0) {
			return true;
		} else {
			return false;
		}

	}

	public synchronized void setFlag(boolean value) {
		done = value;
	}

	public synchronized boolean getFlag() {
		return done;
	}

}