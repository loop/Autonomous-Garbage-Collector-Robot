package uk.ac.kcl.inf._5ccs2seg.MainApp;
import javaclient3.FiducialInterface;
import javaclient3.GripperInterface;
import javaclient3.PlayerClient;
import javaclient3.Position2DInterface;
import javaclient3.PlayerException;
import javaclient3.structures.PlayerConstants;
import javaclient3.structures.PlayerPose2d;
import javaclient3.structures.fiducial.PlayerFiducialItem;
import javaclient3.RangerInterface;

/**Just a basic robot with all the accessor and control methods needed to be able to write appropriate autonomous software.
 * @author Adrian Bocai
 */
public class Bot {
	
	//player variables
    private PlayerClient robot = null;
    private Position2DInterface pos2D = null;
    private RangerInterface ranger = null;
    private FiducialInterface fiducial = null;
    private GripperInterface grip = null;
    private double fSpeed = 0;
    private double tRate = 0;
    private double[] rangerReadings;
    private double x;
    private double y;
    private double yaw;
    private int breakBeam;
    private int gripSts;
    private PlayerFiducialItem[] fidData;
    private int fidCount;
    
    //flag variables
    private boolean multi = true;
    private boolean mapped = false;
    
    
  
    //constants
    public final int OPEN = 1;
    public final int CLOSE =0;
    
    public final int FRONT_L = 0; public final int FRONT_M = 1; public final int FRONT_R = 2; 
    public final int BACK_L = 9; public final int BACK_M = 10; public final int BACK_R = 11; 
    public final int LEFT_F = 3; public final int LEFT_M = 4; public final int LEFT_B = 5; 
    public final int RIGHT_F = 6; public final int RIGHT_M = 7; public final int RIGHT_B = 8; 
    
    
    //other 
    private int count = 1;
    private Debug deb = null;
    private int botNo;


  public Bot(int index, boolean debug) {
	try {
            robot = new PlayerClient("localhost", 6665);
            pos2D = robot.requestInterfacePosition2D(index,PlayerConstants.PLAYER_OPEN_MODE);  
            ranger = robot.requestInterfaceRanger(index,PlayerConstants.PLAYER_OPEN_MODE);
            fiducial = robot.requestInterfaceFiducial(index, PlayerConstants.PLAYER_OPEN_MODE);
            grip = robot.requestInterfaceGripper(index,PlayerConstants.PLAYER_OPEN_MODE);
        } catch (PlayerException e) {
            System.err.println("Bot" + index + ": Error connecting to Player!\n>>>" + e.toString());
            System.exit(1);
        }

        robot.runThreaded(-1,-1);
       
        botNo = index;
	
	//New thread that updates the range sensor readings every 25 milisecond	:
	Thread rangerThread = new Thread() {
            public void run() {
                while (true) {
                    while (!ranger.isDataReady()) {
                        try {Thread.sleep(25);} catch (InterruptedException e) {} 
                    }
                    rangerReadings = ranger.getData().getRanges();
                    try {Thread.sleep(25);} catch (InterruptedException e) {}
                }
            }
	};
    rangerThread.start();
    
   
	
	//New thread that updates the odometry data every 25 miliseconds:
	Thread odometryThread = new Thread() {
            public void run() {
                while (true) {
                    while (!pos2D.isDataReady()) {
                        try {Thread.sleep(25);} catch (InterruptedException e) {} 
                    }          
                    x = pos2D.getX();
                    y = pos2D.getY();
                    yaw = pos2D.getYaw();	 	            
                    try {Thread.sleep(25);} catch (InterruptedException e) {}
                }
            }
	};
    odometryThread.start();
    
  //New thread that updates the fiducial readings every 25 milisecond
    Thread fiducialThread = new Thread() {
        public void run() {
            while (true) {
                while (!fiducial.isDataReady()) {
                    try {Thread.sleep(25);} catch (InterruptedException e) {} 
                }
                
                fidData = fiducial.getData().getFiducials();
                fidCount = fiducial.getData().getFiducials_count();
                try {Thread.sleep(25);} catch (InterruptedException e) {}
            }
        }
	};
    fiducialThread.start();
    
  //New thread that updates the gripper status readings every 150 milisecond	
  	Thread gripperThread = new Thread() {
              public void run() {
                  while (true) {
                      while (!grip.isDataReady()) {
                          try {Thread.sleep(25);} catch (InterruptedException e) {} 
                      }
                      breakBeam = grip.getData().getBeams();
                      gripSts = grip.getData().getState();
                      try {Thread.sleep(150);} catch (InterruptedException e) {}
                  }
              }
  	};
      gripperThread.start();
      
    	
    if(debug){debug();}
    
  }


  		//CONTROL METHODS
  
  	/** Pauses the current thread by the given time.
     *  @param millis the time given in milliseconds.
  	 */
    public static void pause(long millis) {
	try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {}
    }

    /**Updates the speed and turn rate of the robot
     */
    private void updateSpeed() {
        pos2D.setSpeed(fSpeed, tRate);
    }

    /**Sets the forward speed of the robot to the given speed. 
     * @param speed the required speed in meters per second. 
     */
    public void setSpeed(double speed) {
        fSpeed = speed;
        updateSpeed();
    }

    /**Sets the turn rate of the robot to the given turn rate.
	 * @param rate the required turn rate in radians per second.
     */
    public void setTRate(double rate) {
        tRate = rate;
        updateSpeed();
    }

   /**Stops the robot.
    */
    public void stop() {
        fSpeed = 0;
        tRate = 0;
        updateSpeed();
    }
    
    /**Opens and closes the Bot's gripper
     * @param comand 
     */
    public void gripper(int comand){
    	if(comand == CLOSE){grip.close();}
    	else{grip.open();}
    }

    
    
    
    	//ACCESSOR METHODS:
    
    /**@return the readings of all the range sensors, in meters.
    */
    public synchronized double[] getRanges() {
        return rangerReadings;
    }

    /**@return a particular sensor reading 
     * @param index identifies the sensor required
     * 
     */
    public synchronized double getRange(int index){
    	return rangerReadings[index];
    }
    /**@return x coordinate of the Bot.
     */
    public synchronized double getX() {
    	return x;
    }
    
    /**@return y coordinate of the Bot.
     */
    public synchronized double getY() {
        return y;
    }
    
    /**@return the yaw of the Bot.
     */
    public synchronized double getYaw() {
        return yaw;
    }

    /**@return if the gripper is open, closed or in transition
     */
    public synchronized int getGripSts(){
    	return gripSts;
    }
    
    /**@return if any object is between the griper's "claws"
     */
    public synchronized int getGripBeam(){
    	return breakBeam;
    }
    
    public synchronized PlayerFiducialItem[] getFiducials(){
    	return fidData;
    }
    
    public synchronized int getFidCount(){
    	return fidCount;
    }
    
    
    
    
    //DEBUG METHODS:
    
    protected synchronized double getFSpeed(){
    	return fSpeed;
    }
    
    protected synchronized double getTRate(){
    	return tRate;
    }
    
    /**Displays a GUI with all the robot data, in order to help with debugging.
     */
    public void debug(){
    	if( deb == null) {deb = new Debug(this, botNo);} 	
    }
    
 
    
    
    
    
    //ROBOT COMMAND METHODS:  
    
    /**Sets the robot to multi or solo mode
     * @param flag true for multi, false for solo
     */
    public void setMode(boolean flag){
    	multi = flag;
    }
    
    /** The robot(s) will start to explore and map the environment
     */
    public void explore(){
    	if(multi){ 
    		//new Navigate(this).multi();   
    		System.out.println("Exploring mappig envi(muti) and probably returning data structure"); mapped = true;
    	}
    	else {
    		//new Navigate(this).solo();   
			System.out.println("Exploring mappig envi(solo) and probably returning data structure"); mapped = true;
		}			
    }
    
    /**With the help of the map the robot(s) will start to pick up the garbage and put it into the Collection Area.
     * Which is:
     * @param x1 South of this x-coordinate 
     * @param y1 East of this y-coordinate
     * @param x2 North of this x-coordinate
     * @param y2 West of this y-coordinate
     */
    public void collect(int x1, int y1, int x2, int y2){
    	if (mapped){
    		if (multi){ 
    			//new Clean(x1,y1,x2,y2,this).multi();
    			System.out.println("Collecting garabage and putting it in the trash (multi)" 
    			+ " x1: "+ x1 + " y1: " + y1 + " x2: " + x2 + " y2: " + y2);
    		}
    		else {
    			//new Clean(x1,y1,x2,y2,this).solo();
    			System.out.println("Collecting garabage and putting it in the trash (solo)"
    			+  "x1: "+ x1 + " y1: " + y1 + " x2: " + x2 + " y2: " + y2);
    		}
    	}
    	else{explore();collect(x1,y2,x2,y2);}
    }
    
    /**Outputs a visual representation of the current map.
     * @param fileName the desired name for the outputted map.
     */
    public void map(String fileName){
    	if (fileName != null && mapped){
    		//new outMap(fileName, datastructure);
    		System.out.println("Map with the name " + fileName + ".jpg");
    	}
    	else if(mapped){
    		//new outMap("output" + count, datastructure);
    		System.out.println("Map with the name output" + count + ".jpg"); count++;
    	}
    }
}
