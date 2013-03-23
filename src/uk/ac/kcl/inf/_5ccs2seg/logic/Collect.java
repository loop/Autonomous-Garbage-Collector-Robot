package uk.ac.kcl.inf._5ccs2seg.logic;

import java.util.ArrayList;

import javaclient3.structures.fiducial.PlayerFiducialItem;

import uk.ac.kcl.inf._5ccs2seg.data.Bot;
import uk.ac.kcl.inf._5ccs2seg.data.GridMap;
import uk.ac.kcl.inf._5ccs2seg.data.Node;


/**
* Class that collects the garbage
* 
* @author Adrian Bocai for Team Dijkstra
* 
*/
public class Collect {
	private GridMap map;
	private Bot cleaner1;
	private ArrayList<double[]> garbageL;
	private boolean done = false;
	private boolean imminent = false;
	private boolean returnn = false;
	private int threadSleep = 16;
	private ArrayList<Node> path;
	private MasterControlProgram mcp;
	private double[] dropLoc;
	private double[] rem = new double[]{1,1};
	
	
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
						
						double dd = Math.sqrt(Math.pow((arr[0]- rem[0]),2)+
								Math.pow((arr[1]-rem[1]),2));
						
						if ((cnt == size || size == 0) && 
								fiduc[i].getId() != 1 && d > 2 && dd > 1){
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
				int count = 0;
				ArrayList<double[]> road = new ArrayList<double[]>();
				
				double d;
				double smallD=0;
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
					//while(getRet()){
						//try {
						//	Thread.sleep(50);
						//} catch (InterruptedException e) {
						//}

					//}
				
				if (garbageL.isEmpty() && !getRet()){
					setFlag(true);
					mcp.setFinished(true);
					cleaner1.close();
					System.out.println("SUCCESS!!!! GARBAGE COLLECTED");
					break;
				}
					
				 arr = map.coordToArrayIndexCalc(cleaner1.getX(), cleaner1.getY());
				 start = new Node(arr[1],arr[0]);
				 
				
				 
				 if (!getRet()){
				 smallD = 200000;
				 for (int i = 0; i< garbageL.size(); i++){
					 d = Math.sqrt(Math.pow((cleaner1.getX()- garbageL.get(i)[0]),2)+
								Math.pow((cleaner1.getY()-garbageL.get(i)[1]),2));
					 if (d < smallD){smallD = d; close = i;}
				 }
				 count++;
				 
				 arr2 = map.coordToArrayIndexCalc(garbageL.get(close)[0], garbageL.get(close)[1]);
				 
				 goal = new Node(arr2[1],arr2[0]);
				 
				 }
				 else { goal = drop;}
				 
				
				 //
				
				
				  //DISPLAY PATH
				if (path != null){
					for (int i = 0; i < path.size(); i++){
						map.setSts(path.get(i).getArr(1), path.get(i).getArr(0), 1);
					}
				}
				
				//
				road.clear();
				double[] arrr = new double[2];
				path = new AStarAlg(start,goal,map).plan();
				for (int i = path.size()-1; i >= 0 ; i--){
					map.setSts(path.get(i).getArr(1), path.get(i).getArr(0), 4);
					arrr = map.arrayIndexToCoordCalc(path.get(i).getArr(1),path.get(i).getArr(0));
					road.add(arrr);
				}
				
				//TEST
				
				//for (int i =0; i < road.size(); i++){
					//double[] arrrr =  road.get(i);
					//System.out.println(i + ": " + "(" + arrrr[0] + ", " + arrrr[1] + ")");
				//}
			//	System.out.println();
				
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
						if( i % 2 == 0)
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
							
							while( dist > 0.5 && !getIm())
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
					if(!getIm()){
					cont();}
					
					
					}
				
				
					}
			
			
		};
		collectG.start();
		
		Thread collision= new Thread() {
			public void run() {
				while (!getFlag()) {
					for (int i = 0; i < 9; i += 7) {
						if (cleaner1.getRange(i) < 1) {							
							
								setIm(true);
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
		//collision.start();
	}
	
	public void cont(){
		double fX=0;
		double fY=0;
		int in =0 ;
		double smallD= 0;
		double d = 0;
		int count = 0;
		boolean found = true;
		double x;
		double y;
		
		double fSpeed = 0;
		double tRate = 0;

		PlayerFiducialItem[] fiduc;

		double curX=0;
		double curY=0;
		double[] arr3;
		double alpha;
		double yaw = cleaner1.getYaw();
		
		
		
		if (!getRet()){
			
		loop:
			while (true){
				if(getFC() <= 0){
					cleaner1.stop();
					while (getFC() <= 0 && !colArea())
					{
						count++;
						if (count >= 125){
							found = false;
							break;
						}
						
						cleaner1.setTRate(1);
						
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {}
						
						
					}
					cleaner1.stop();
					if (!found){
						x = cleaner1.getX();
						y = cleaner1.getY();
						
						for (int i = 0; i< garbageL.size(); i++){
							d = Math.sqrt(Math.pow((x- garbageL.get(i)[0]),2)+
								Math.pow((y-garbageL.get(i)[1]),2));
						
							// removes fake garbage
							if (d < 4.5){
								garbageL.remove(i);
							}
						}
						setRet(false);
						break;
					}
					
					
				}
				else{
	
				while (cleaner1.getGripBeam() == 0){
					fiduc = cleaner1.getFiducials();
			
					
					
					smallD = 30000;
					for (int i = 0; i< fiduc.length; i++){
						if (fiduc[i].getId() == 1) {continue;}
						fX = fiduc[i].getPose().getPx();
						fY = fiduc[i].getPose().getPy();
						
						double ddd = Math.sqrt(Math.pow((dropLoc[0]- fX),2)+
								Math.pow((dropLoc[1]-fY),2));
						
						if (ddd < 2){continue;}
						
						d = Math.sqrt(Math.pow(fX,2)+Math.pow(fY,2));
						if (d < smallD){smallD = d; in = i;} 
					}
					
					if (getFC() <= 0){
						break loop;
					}
					
					curX = fiduc[in].getPose().getPx();
					fSpeed = curX - 0.4;
					cleaner1.setSpeed(fSpeed);
				
					curY = fiduc[in].getPose().getPy();
					tRate = curY;
					cleaner1.setTRate(tRate);
					
					
					if (cleaner1.getRange(7) < 0.85) {
						
					
						cleaner1.setSpeed(-0.15);
						cleaner1.pause(1250);
						cleaner1.setSpeed(0);
						cleaner1.setTRate(-0.5);
						try {
							Thread.sleep((long) ((10 * d / 0.5) * 1000));
						} catch (InterruptedException e) {

						}
						cleaner1.setTRate(0);
						// turn( - (10 * d), 0.5);
						cleaner1.setSpeed(0.4);
						cleaner1.pause(1000);
					}

					if (cleaner1.getRange(1) < 0.84) {
						
					
						cleaner1.setSpeed(-0.4);
						cleaner1.pause(1000);
						cleaner1.setSpeed(0);
						cleaner1.setTRate(0.5);
						try {
							Thread.sleep((long) ((10 * d / 0.5) * 1000));
						} catch (InterruptedException e) {

						}
						cleaner1.setTRate(0);
						// turn((10 * d), 0.5);
						cleaner1.setSpeed(0.4);
						cleaner1.pause(1000);
						
					}
				
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {}
			
			
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {}
				cleaner1.stop();
			
				
				smallD = 30000;
				fiduc = cleaner1.getFiducials();
				for (int i = 0; i< fiduc.length; i++){
					if (fiduc[i].getId() == 1) {continue;}
					fX = fiduc[i].getPose().getPx();
					fY = fiduc[i].getPose().getPy();
					
					double ddd = Math.sqrt(Math.pow((dropLoc[0]- fX),2)+
							Math.pow((dropLoc[1]-fY),2));
					
					if (ddd < 2){continue;}
					
					d = Math.sqrt(Math.pow(fX,2)+Math.pow(fY,2));
					if (d < smallD){smallD = d; in = i;} 
				}
				 
				fX = fiduc[in].getPose().getPx();
				fY = fiduc[in].getPose().getPy();
				yaw = cleaner1.getYaw();
				
				
				alpha = yaw + Math.atan(fY/fX);
				arr3 = Explore.calcCoord(cleaner1.getX(), cleaner1.getY(), alpha, smallD);
				cleaner1.gripper(2);
			
				//calc coord of garbage in gripper
				
			
				//remove that garbage from the list
				smallD = 3000;
				int index =0;
				for (int i = 0; i< garbageL.size(); i++){
					d = Math.sqrt(Math.pow((arr3[0]- garbageL.get(i)[0]),2)+
							Math.pow((arr3[1]-garbageL.get(i)[1]),2));
					if (d < smallD){smallD = d; index = i;}
				}
				rem = garbageL.get(index);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
				garbageL.remove(index);
				
				
				
				
				//remove garbage from map
				map.setSts(rem[0], rem[1], 1);
			
				while (cleaner1.getGripSts() != 2){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
				}
				setRet(true);
			
				break loop;}
			
		}
		}
		else if (getRet()){
			cleaner1.stop();
			cleaner1.gripper(1);
			while (cleaner1.getGripSts() != 1){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
			}
			setRet(false);
		}
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
	
	public synchronized void setRet(boolean value) {
		returnn = value;
	}

	public synchronized boolean getRet() {
		return returnn;
	}
	
	public boolean colArea(){
		if (getFC() > 0){
			int count = 0;
			
			PlayerFiducialItem[] fid = cleaner1.getFiducials();
			for(int i = 0; i < fid.length; i++){
				
				if (fid[i].getId() == 1) {count ++; continue;}
				
				double fX = fid[i].getPose().getPx();
				double fY = fid[i].getPose().getPy();
				
				double d = Math.sqrt(Math.pow((dropLoc[0]- fX),2)+
						Math.pow((dropLoc[1]-fY),2));
				
				if (d < 2) {count ++;}
			}
			
			if (count == fid.length){return true;}
			return false;
		}
		
		
	
		
		return false;
	}
	
	
	public synchronized int getFC() {
		PlayerFiducialItem[] fiduc = cleaner1.getFiducials();
		int f = cleaner1.getFidCount();
		
		if (f==0){
			return f;
		}
		else{
			for (int i = 0; i < fiduc.length; i++){
				if (fiduc[i].getId() == 1) {f--;}
			}
			return f;
		}
		
	}
		
	
	
}
