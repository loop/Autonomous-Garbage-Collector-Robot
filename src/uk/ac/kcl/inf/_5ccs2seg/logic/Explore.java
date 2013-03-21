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
	private ArrayList<double[]> garbageL = new ArrayList<double[]>();
	private boolean done = false;
	private boolean imminent = false;
	private int threadSleep = 16;
	private int counter = 0;
	private ArrayList<Node> path;
	private int  unreachCount = 0;
	private MasterControlProgram mcp;

	public Explore(MasterControlProgram mcp, Bot cleaner) {
		map = mcp.getGrid();
		cleaner1 = mcp.getCleaner(cleaner.getBot());
		this.mcp = mcp;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		// New thread that updates the map from the sensor readings
		Thread updateGrid = new Thread() {
			public void run() {
				double range;
				double alpha;
				double x;
				double y; 
				double step = 0.09;
				double[] arr;
				int[] arr2;
				double ang;
				while (!getFlag()) {
					ang = 337;
					counter = 0;
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
						counter++;
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
						double dist; double head;
						double arr[];
						while (!getFlag()) {
							
							if (cleaner1.getFidCount() > 0){
								fiduc = cleaner1.getFiducials();
								x = cleaner1.getX();
								y = cleaner1.getY();
								yaw = cleaner1.getYaw();
								head = cleaner1.getHead();
								
							for (int i = 0; i < fiduc.length; i++){
								xx = fiduc[i].getPose().getPx();
								yy = fiduc[i].getPose().getPy();		
								dist = Math.sqrt(Math.pow(xx,2)+Math.pow(yy,2));
								alpha = yaw + Math.atan(yy/xx);
								arr = calcCoord(x, y, alpha, dist);
								
								//adjust for fiducial placement
								double ajust;
								if (head >= 0 && head <= 90){
									ajust = head * 0.0022;
									arr[1] = arr[1] + ajust;
									arr[0] = arr[0] + (0.2-ajust);
								}
								else if (head > 90 && head <= 180){
									head = head - 90;
									ajust = head * 0.0022;
									arr[0] = arr[0] + (ajust*-1);
									arr[1] = arr[1] + (0.2-ajust);
								}
								else if (head >= -180 && head <= -90){
									head = Math.abs(head) - 90;
									ajust = head * 0.0022;
									arr[0] = arr[0] + (ajust*-1);
									arr[1] = arr[1] + ((0.2-ajust)*-1);
								}
								else{
									ajust = Math.abs(head) * 0.0022;
									arr[1] = arr[1] + (ajust*-1);
									arr[0] = arr[0] + (0.2-ajust);
								}
								
								
								int cnt = 0;
								double d;
								int size = garbageL.size();
								for (int j =0; j < size; j++){
									d = Math.sqrt(Math.pow((arr[0]-garbageL.get(j)[0]),2)+
											Math.pow((arr[1]-garbageL.get(j)[1]),2));
									//System.out.println(d);
									if (d<1){break;}
									cnt++;
								}
								
								if ((cnt == size || size == 0) && fiduc[i].getId() != 1){
									garbageL.add(arr);
									map.setSts(arr[0], arr[1], 3); 
								}
								
								
								//TEST
								//for (int j =0; j < garbageL.size(); j++){
									//double[] arrr =  garbageL.get(j);
									//System.out.println(j + ": " + "(" + arrr[0] + ", " + arrr[1] + ")");
								//}
								//System.out.println();
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
						Node start;
						Node goal;
						Node oldGoal = new Node(-1,-1);
						int[] arr;
						ArrayList<double[]> road = new ArrayList<double[]>();
						
						
						System.out.println(Explore.this.mcp.getWallF());
						while(!Explore.this.mcp.getWallF()){try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}}
						
						filterNoise();
						
						while (!getFlag()) {			
							try {
								Thread.sleep(25);
							} catch (InterruptedException e) {
							}
						
							arr = map.coordToArrayIndexCalc(cleaner1.getX(), cleaner1.getY());
						 start = new Node(arr[1],arr[0]);
						 System.out.println("issuse");
						 goal = new NextLoc(start, map).calc();
						
						 //
						 System.out.println(goal);
						
						  //DISPLAY PATH
						if (path != null){
							for (int i = 0; i < path.size(); i++){
								map.setSts(path.get(i).getArr(1), path.get(i).getArr(0), 1);
							}
						}
						 
						 
						 //the whole map has been covered
						 if (goal.getArr(0) < 0 || goal.getArr(1) < 0 ){
							 
							 setFlag(true);
							 filterNoise();
							 Explore.this.mcp.setGlist(garbageL);
							 Explore.this.mcp.setMapped(true);
							 Explore.this.mcp.setMaping(false);
							 System.out.println("Covered");  break;
						 }
						 
						 //deals with unreachable squares
						 if(goal.equals(oldGoal)){
							 unreachCount++;
							 if (unreachCount >= 3){
								 map.setSts(goal.getArr(1), goal.getArr(0), 2);
							 }
						 } else {unreachCount = 0;}
						 oldGoal = goal;
						 
						 
						
						
						//
						double[] arrr = new double[2];
						path = new AStarAlg(start,goal,map).plan();
						for (int i = path.size()-1; i >= 0 ; i--){
							map.setSts(path.get(i).getArr(1), path.get(i).getArr(0), 4);
							arrr = map.arrayIndexToCoordCalc(path.get(i).getArr(1),path.get(i).getArr(0));
							road.add(arrr);
						}
						
						//TEST
						
						for (int i =0; i < road.size(); i++){
							double[] arrrr =  road.get(i);
							System.out.println(i + ": " + "(" + arrrr[0] + ", " + arrrr[1] + ")");
						}
						System.out.println();
						
						//FOLLOW PATH
						 //
						 //
						setIm(false);
						if(path == null)
						{
							cleaner1.setSpeed(0);
							cleaner1.setTRate(0);
						}
							for(int i = 0; i < road.size(); i++)
							{
								if( i % 4 == 0)
								{
									double x = road.get(i)[0];
									double y = road.get(i)[1];
									
									if( path == null ) 
									{
										road.clear();
										break;
									}
									
									double dx = x - cleaner1.getX();
									double dy = y - cleaner1.getY();
									
									double dist = Math.sqrt((dx*dx) +(dy*dy));
									
									while( dist > 0.8 && !getIm())
									{
										cleaner1.moveTo(x, y);
										
										dx = x - cleaner1.getX();
										dy = y - cleaner1.getY();
										
										dist = Math.sqrt((dx*dx) +(dy*dy));
										
										try {
											Thread.sleep(25);
										} catch (InterruptedException e) {
											
										}
									}
									
								}
							}
							road.clear();
						
						
						
						
							}
						}
					
					
				};
				go.start();
				
				Thread collision= new Thread() {
					public void run() {
						while(!Explore.this.mcp.getWallF())
						{try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}}
						while (!getFlag()) {
							for (int i = 9; i < 16; i++){
								if (cleaner1.getRange(i) < 0.25){
									setIm(true);
									System.out.println(cleaner1.getRange(i));
									cleaner1.stop();
									try {
										Thread.sleep(1200);
									} catch (InterruptedException e) {
									}
								}
							}
							
							try {
								Thread.sleep(25);
							} catch (InterruptedException e) {
							}
						}
					}
				};
				collision.setPriority(Thread.MAX_PRIORITY);	
				collision.start();
		
	}

	public static double[] calcCoord(double x, double y, double alpha,
			double range) {

		x = x + (Math.cos(alpha) * range);
		y = y + (Math.sin(alpha) * range);
		return new double[] { x, y };
		
	}

	private synchronized boolean checkCell(int i, int j) {
	
		
		if (map.getSts(i, j) == 0) {
			return true;
		} 
		if(map.getSts(i, j) == 2 && counter == 12){
			return true;
			
		}else {
			return false;
		}

	}

	public  void filterNoise() {
		/*
		for (int i = 1; i < map.getMaxY()-1; i++){
			for (int j = 1; j < map.getMaxX()-1; j++){
				
				if (map.getSts(j, i) == 2){
					if ((map.getSts(j+1,i) == 1 && map.getSts(j-1,i) ==  1) || 
							(map.getSts(j,i+1) ==  1 || map.getSts(j,i-1) == 1)){
						map.setSts(j, i, 1);
					}
				}
				
				else if (map.getSts(j, i) == 1){
					if ((map.getSts(j+1,i) == 2 && map.getSts(j-1,i) ==  2) || 
							(map.getSts(j,i+1) ==  2 || map.getSts(j,i-1) == 2)){
						map.setSts(j, i, 2);
					}
				}
				
				else if (map.getSts(j, i) == 0){
					if ((map.getSts(j+1,i) == 2 && map.getSts(j-1,i) ==  2) || 
							(map.getSts(j,i+1) ==  2 || map.getSts(j,i-1) == 2)){
						map.setSts(j, i, 2);
					}
				}
				
			}
		}
		*/
	}
	
	public synchronized void setFlag(boolean value) {
		done = value;
	}

	public synchronized boolean getFlag() {
		return done;
	}
	
	public synchronized void setIm(boolean value) {
		imminent = value;
	}

	public synchronized boolean getIm() {
		return imminent;
	}
	

}