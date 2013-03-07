package uk.ac.kcl.inf._5ccs2seg.MainApp;




import javaclient3.FiducialInterface;
import javaclient3.GripperInterface;
import javaclient3.PlayerClient;
import javaclient3.PlayerException;
import javaclient3.Position2DInterface;
import javaclient3.RangerInterface;
import javaclient3.structures.PlayerConstants;
import javaclient3.structures.PlayerPose2d;
import javaclient3.structures.PlayerPose3d;
import javaclient3.structures.fiducial.PlayerFiducialItem;

/**
 * Just a basic robot with all the access and control methods needed to be able
 * to write appropriate autonomous software.
 * 
 * @author Adrian Bocai Team Dijkstra
 */
public class Bot {

	// player variables
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
	private double heading;

	
	 private final static int PRECISION = 250;

	// constants
	public final int OPEN = 1;
	public final int CLOSE = 0;

	public final int FRONT_L = 0;
	public final int FRONT_M = 1;
	public final int FRONT_R = 2;
	public final int LEFT_F = 3;
	public final int LEFT_M = 4;
	public final int LEFT_B = 5;
	public final int RIGHT_F = 6;
	public final int RIGHT_M = 7;
	public final int RIGHT_B = 8;
	public final int BACK_L = 9;
	public final int BACK_M = 10;
	public final int BACK_R = 11;

	// other
	private int count = 1;
	private Debug deb = null;
	private int botNo;
	
	/**
	 * 
	 * @param index
	 * @param debug
	 */
	public Bot(int index, boolean debug) {
		try {
			robot = new PlayerClient("localhost", 6665);
			pos2D = robot.requestInterfacePosition2D(index,
					PlayerConstants.PLAYER_OPEN_MODE);
			ranger = robot.requestInterfaceRanger(index,
					PlayerConstants.PLAYER_OPEN_MODE);
			fiducial = robot.requestInterfaceFiducial(index,
					PlayerConstants.PLAYER_OPEN_MODE);
			grip = robot.requestInterfaceGripper(index,
					PlayerConstants.PLAYER_OPEN_MODE);
		} catch (PlayerException e) {
			System.err.println("Bot" + index
					+ ": Error connecting to Player!\n>>>" + e.toString());
			System.exit(1);
		}

		robot.runThreaded(-1, -1);

		botNo = index;

		// New thread that updates the range sensor readings every 25 milisecond
		// :
		Thread rangerThread = new Thread() {
			public void run() {
				while (true) {
					while (!ranger.isDataReady()) {
						try {Thread.sleep(25);} 
						catch (InterruptedException e) {}
					}
					rangerReadings = ranger.getData().getRanges();
					try {Thread.sleep(25);} 
					catch (InterruptedException e) {
					}
				}
			}
		};
		rangerThread.start();

		// New thread that updates the odometry data every 25 miliseconds:
		Thread odometryThread = new Thread() {
			public void run() {
				while (true) {
					while (!pos2D.isDataReady()) {
						try {
							Thread.sleep(25);
						} catch (InterruptedException e) {
						}
					}
					x = pos2D.getX();
					y = pos2D.getY();
					yaw = pos2D.getYaw();
					heading = yaw*180/Math.PI;
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		odometryThread.start();

		// New thread that updates the fiducial readings every 25 milisecond
		Thread fiducialThread = new Thread() {
			public void run() {
				while (true) {
					while (!fiducial.isDataReady()) {
						try {
							Thread.sleep(25);
						} catch (InterruptedException e) {
						}
					}

					fidData = fiducial.getData().getFiducials();
					fidCount = fiducial.getData().getFiducials_count();
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		fiducialThread.start();

		// New thread that updates the gripper status readings every 150
		// milisecond
		Thread gripperThread = new Thread() {
			public void run() {
				while (true) {
					while (!grip.isDataReady()) {
						try {
							Thread.sleep(25);
						} catch (InterruptedException e) {
						}
					}
					breakBeam = grip.getData().getBeams();
					gripSts = grip.getData().getState();
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		gripperThread.start();

		if (debug) {
			debug();
		}

	}

	// CONTROL METHODS

	/**
	 * Pauses the current thread by the given time.
	 * 
	 * @param millis
	 *            the time given in milliseconds.
	 */
	public synchronized void pause(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Updates the speed and turn rate of the robot
	 */
	private synchronized void updateSpeed() {
		pos2D.setSpeed(fSpeed, tRate);
	}

	/**
	 * Sets the forward speed of the robot to the given speed.
	 * 
	 * @param speed
	 *            the required speed in meters per second.
	 */
	public synchronized void setSpeed(double speed) {
		fSpeed = speed;
		updateSpeed();
	}

	/**
	 * Sets the turn rate of the robot to the given turn rate.
	 * 
	 * @param rate
	 *            the required turn rate in radians per second.
	 */
	public synchronized void setTRate(double rate) {
		tRate = rate;
		updateSpeed();
	}
	
	public synchronized void moveTo(double x, double y) {
		PlayerPose2d pose = new PlayerPose2d(x, y, 0);
		pos2D.setPosition(pose, new PlayerPose2d(), 1);
	}


	/**
	 * Stops the robot.
	 */
	public synchronized void stop() {
		fSpeed = 0;
		tRate = 0;
		updateSpeed();
	}

	/**
	 * Opens and closes the Bot's gripper
	 * 
	 * @param comand
	 */
	public synchronized void gripper(int comand) {
		if (comand == CLOSE) {
			grip.close();
		} else {
			grip.open();
		}
	}
	
	
	
	/**@param ang the angle the robot will turn to
     */
    public synchronized void turnTo(double ang){
    	double turnDirection;
    	
    	if (ang == 180){ang = 179.9;}
    	else if (ang == -180){ang = -179.9;}
    	else if (ang >= 0 && ang < 0.2){ang = 0.2;}
    	else if (ang <= 0 && ang > -0.2){ang = -0.2;}
    	
    	if (Math.abs((ang - getHead())) < 0.1) {
    		stop(); return; 
        }
    	
    	double diff = ang - getHead();
    	if (diff<0) diff = diff + 360;
    	if (diff<180) {
    		turnDirection = 1;
    	} else {
    		turnDirection = -1;
    	}
    	
    	double difference = Math.abs(round(getHead()-ang));
    	while (difference > 25) {          
            difference = Math.abs(round(getHead()-ang));
            //System.out.println("diff: " + difference);
            setTRate(turnDirection*difference/(PRECISION*100));
            pause(50);
        }
    	System.out.println("finish");
    	stop();
    }
    
    public synchronized static double turnBy(double head, double ang) {
    	double res;
    	
    	
    	if (ang > 0){
    		res = head + ang;
    		if (res > 180){res = res - 360;}	
    	}
    	else {
    		res = head + ang;
    		if (res < -180){res = res + 360;}
    	}
    	
    	return res;
    }
    
    private synchronized double round(double no) {
    	return(Math.round(no*PRECISION));
	}

    /**Will get the angle the robot needs to turn to face a location on the map straight on
     * 
     * @param x x-coordinate of location
     * @param y y-coordinate of location
     * @return the heading the robot needs to have
     */
    public synchronized double getAng(double x, double y, double x2, double y2){
		int sign = 1;
		double res;
    	
		if (Math.abs(x - x2) < 0.1){
			System.out.println("Predef same x");
			if (y < y2) {res = -90;}
			else {res = 90;}
		}
		else if (Math.abs(y - y2) < 0.1) {
			System.out.println("Predef same y");
			if (x < x2) {res = 180;}
			else {res = 0;}
		}
		else {
			if (y < y2){sign = -1;}
			else if (y > y2){sign = 1;}
			
    	
			res = Math.atan(Math.abs((y - y2))/Math.abs((x-x2)));
			res = res*180/Math.PI;
    	
			if (x < x2) {res = 180 - res;}
    	
			res = res * sign;
		}
    	System.out.println("Angle " + res);
    	return res;
    	
    }

	// ACCESSOR METHODS:

	/**
	 * @return the readings of all the range sensors, in meters.
	 */
	public synchronized double[] getRanges() {
		return rangerReadings;
	}

	/**
	 * @return a particular sensor reading
	 * @param index
	 *            identifies the s6ensor required
	 * 
	 */
	public synchronized double getRange(int index) {
		return rangerReadings[index];
	}

	/**
	 * @return x coordinate of the Bot.
	 */
	public synchronized double getX() {
		return x;
	}

	/**
	 * @return y coordinate of the Bot.
	 */
	public synchronized double getY() {
		return y;
	}	


	/**
	 * @return the yaw of the Bot.
	 */
	public synchronized double getYaw() {
		return yaw;
	}
	
	/**@return the yaw of the bot in degrees.
     */
    public synchronized double getHead() {
        return heading;
    }

	/**
	 * @return if the gripper is open, closed or in transition
	 */
	public synchronized int getGripSts() {
		return gripSts;
	}

	/**
	 * @return if any object is between the griper's "claws"
	 */
	public synchronized int getGripBeam() {
		return breakBeam;
	}

	public synchronized PlayerFiducialItem[] getFiducials() {
		return fidData;
	}

	public synchronized int getFidCount() {
		return fidCount;
	}

	// DEBUG METHODS:

	protected synchronized double getFSpeed() {
		return fSpeed;
	}

	protected synchronized double getTRate() {
		return tRate;
	}

	/**cleaner1
	 * Displays a GUI with all the robot data, in order to help with debugging.
	 */
	public void debug() {
		if( deb == null) {deb = new Debug(this, botNo);}
	}
	
	public synchronized void close(){
		robot.close();
	}
	
	public static void main(String[] args){
		Bot bla = new Bot(0,true);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		
		
		
	}
}

		