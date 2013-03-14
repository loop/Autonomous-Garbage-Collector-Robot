package uk.ac.kcl.inf._5ccs2seg.logic;

import java.util.ArrayList;

import uk.ac.kcl.inf._5ccs2seg.data.Bot;
import uk.ac.kcl.inf._5ccs2seg.data.GridMap;
import uk.ac.kcl.inf._5ccs2seg.data.Node;

import javaclient3.structures.fiducial.PlayerFiducialItem;



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
	private ArrayList<double[]> garbageL = new ArrayList<double[]>();
	private boolean done = false;
	private int threadSleep = 16;

	public Explore(MasterControlProgram mcp) {
		map = mcp.getGrid();
		cleaner1 = mcp.getCleaner(1);
		cleaner2 = mcp.getCleaner(2);
		cleaner3 = mcp.getCleaner(3);

		
		// New thread that updates the map from the sensor readings
		Thread updateGrid = new Thread() {
			public void run() {
				double range;
				double alpha;
				double x;
				double y; 
				double step = 0.125;
				double[] arr;
				int[] arr2;
				double ang;
				while (!getFlag()) {
					ang = 337;
					for (int i = 0; i<9; i++){
							
					if (i==4){ang = 0;}
					else if (i == 5){ang = 8;}	
					alpha = Math.toRadians(Bot.turnBy(cleaner1.getHead(), ang));
					range = cleaner1.getRange(i);
					x = cleaner1.getX();
					y = cleaner1.getY();
					arr = calcCoord(x, y, alpha, range);
					//if (i==4){System.out.println(arr[0] + "---" + arr[1]); }
					ang = ang + 5;
					
					if (range < 5) {
						map.setSts(arr[0], arr[1], 2);
					} 
					while (range > 0) {
						arr = calcCoord(x, y, alpha, range);
						arr2 = map.coordToArrayIndexCalc(arr[0], arr[1]);
						if (checkCell(arr2[0], arr2[1])) {
							map.setSts(arr2[0], arr2[1], 1);
						}
						range = range - step;
					}

					}
					try {
						Thread.sleep(threadSleep);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		updateGrid.start();

		// New thread that updates the map from the sensor readings
				Thread updateFid = new Thread() {
					public void run() {
						PlayerFiducialItem[] fiduc;
						double x; double xx;
						double y; double yy;
						double yaw; double alpha;
						double dist;
						double arr[];
						while (!getFlag()) {
							
							if (cleaner1.getFidCount() > 0){
								fiduc = cleaner1.getFiducials();
								x = cleaner1.getX();
								y = cleaner1.getY();
								yaw = cleaner1.getYaw();
								
							for (int i = 0; i < fiduc.length; i++){
								xx = fiduc[i].getPose().getPx();
								yy = fiduc[i].getPose().getPy();		
								dist = Math.sqrt(Math.pow(xx,2)+Math.pow(yy,2));
								alpha = yaw + Math.atan(yy/xx);
								arr = calcCoord(x, y, alpha, dist);
								arr[1] = arr[1] + 0.2;
								
								int cnt = 0;
								double d;
								int size = garbageL.size();
								for (int j =0; j < size; j++){
									d = Math.sqrt(Math.pow((arr[0]-garbageL.get(j)[0]),2)+
											Math.pow((arr[1]-garbageL.get(j)[1]),2));
									//System.out.println(d);
									if (d<0.6){break;}
									cnt++;
								}
								
								if (cnt == size || size == 0 ){
									garbageL.add(arr);
									map.setSts(arr[0], arr[1], 3); 
								}
								//System.out.println(garbageL);
								
								//TEST
								for (int j =0; j < garbageL.size(); j++){
									double[] arrr =  garbageL.get(j);
									System.out.println(j + ": " + "(" + arrr[0] + ", " + arrr[1] + ")");
								}
								System.out.println();
							}
							}
							try {
								Thread.sleep(threadSleep);
							} catch (InterruptedException e) {
							}
						}
					}
				};
				updateFid.start();
		
		
				Thread go = new Thread() {
					public void run() {
						
						while (!getFlag()) {
		
							
							
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
							}
							int[] arr = map.coordToArrayIndexCalc(cleaner1.getX(), cleaner1.getY());
						Node f = new Node(arr[0],arr[1]);
						 Node n = new NextLoc(f, map).calc();
						 double[] arr2 = map.arrayIndexToCoordCalc(n.getArr(0), n.getArr(1));
						 
						 System.out.println(arr2[1] + "======" + arr2[0]);
							}
						}
					
					
				};
				//go.start();
		
	
		// setFlag(true);
	}

	private static double[] calcCoord(double x, double y, double alpha,
			double range) {

		x = x + (Math.cos(alpha) * range);
		y = y + (Math.sin(alpha) * range);
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