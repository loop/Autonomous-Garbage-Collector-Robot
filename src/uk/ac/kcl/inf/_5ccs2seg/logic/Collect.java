package uk.ac.kcl.inf._5ccs2seg.logic;

import java.util.ArrayList;

import javaclient3.structures.fiducial.PlayerFiducialItem;

import uk.ac.kcl.inf._5ccs2seg.data.Bot;
import uk.ac.kcl.inf._5ccs2seg.data.GridMap;
import uk.ac.kcl.inf._5ccs2seg.data.Node;

public class Collect {
	private GridMap map;
	private Bot cleaner1;
	private ArrayList<double[]> garbageL;
	private boolean done = false;
	private boolean imminent = false;
	private int threadSleep = 16;
	private ArrayList<Node> path;
	private MasterControlProgram mcp;
	private double[] dropLoc;
	
	
	public Collect (MasterControlProgram mcp, Bot cleaner){
		this.mcp = mcp;
		
		Thread check = new Thread() {
			public void run() {
		while (!Collect.this.mcp.getMapped()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
		}
		}
		
		startTh();
		
			}};
			check.start();
			
		cleaner1 = mcp.getCleaner(cleaner.getBot());
		
		

}
			
	public void startTh(){
		dropLoc = mcp.getCPoint();
		
		mcp.setCollect(true);
		map = mcp.getGrid();
		garbageL = mcp.getGlist();
		
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
						arr = Explore.calcCoord(x, y, alpha, dist);
						
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
						
						d = Math.sqrt(Math.pow((arr[0]- dropLoc[0]),2)+
								Math.pow((arr[1]-dropLoc[1]),2));
						
						if ((cnt == size || size == 0) && fiduc[i].getId() != 1 && d > 2){
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


		Thread collectG = new Thread() {
			public void run() {	
				Node start;
				Node goal;
				Node drop;
				int[]arr2;
				int[] arr;
				boolean returnn = false;
				ArrayList<double[]> road = new ArrayList<double[]>();
				
				PlayerFiducialItem[] fiduc;
				//double x; 
				//double y; 
				//double yaw; 
				double d;
				double smallD;
				int close = 0;
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				
				arr = map.coordToArrayIndexCalc(dropLoc[0], dropLoc[1]);
				 drop = new Node(arr[1],arr[0]); 
				
				while (!getFlag()) {			
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
					}
				
				 arr = map.coordToArrayIndexCalc(cleaner1.getX(), cleaner1.getY());
				 start = new Node(arr[1],arr[0]);
				 
				 
				 if (!returnn){
				 smallD = 200000;
				 for (int i = 0; i< garbageL.size(); i++){
					 d = Math.sqrt(Math.pow((cleaner1.getX()- garbageL.get(i)[0]),2)+
								Math.pow((cleaner1.getY()-garbageL.get(i)[1]),2));
					 if (d < smallD){close = i;}
				 }
				 
				 arr2 = map.coordToArrayIndexCalc(garbageL.get(close)[0], garbageL.get(close)[1]);
				 goal = new Node(arr2[1],arr2[0]);}
				 else { goal = drop;}
				 
				
				 //
				 System.out.println(goal);
				
				  //DISPLAY PATH
				if (path != null){
					for (int i = 0; i < path.size(); i++){
						map.setSts(path.get(i).getArr(1), path.get(i).getArr(0), 1);
					}
				}
				
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
		collectG.start();
		
		Thread collision= new Thread() {
			public void run() {
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
