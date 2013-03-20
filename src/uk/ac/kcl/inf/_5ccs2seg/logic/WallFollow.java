package uk.ac.kcl.inf._5ccs2seg.logic;

import java.util.ArrayList;

import uk.ac.kcl.inf._5ccs2seg.data.Bot;
import uk.ac.kcl.inf._5ccs2seg.data.TargetBox;

public class WallFollow {
	

	/** The Constant FAST. */
	private final static double FAST = 0.4;

	protected boolean STOP = false;
	
	protected boolean prevIn;
	
	protected boolean in;

	/** The Constant SLOW. */
	private final double SLOW = 0.4;

	/** The direction. */
	private int dir;

	/** Constant to convert degrees to radians. */
	private final double d = Math.PI / 180;

	/** The counter. */
	private int counter = 1;
			
	private Bot cleaner_1;
	
	MasterControlProgram mcp;

	/**
	 * Instantiates a new follow2_3.
	 * 
	 * @param index
	 *            the index
	 * @param debug
	 *            the debug
	 */
	public WallFollow(Bot cleaner, MasterControlProgram mcp) {
		this.mcp = mcp;
		cleaner_1 = cleaner;
		findNearestWall();
		wallFollowThread(1);
		targetThread();
		targetOwnThread();
		
	}

	public int getDir() {
		return dir;
	}

	public int getIndex() {
		return cleaner_1.getBot();
	}
		

	/**
	 * Find nearest wall.
	 */
	public synchronized void findNearestWall() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		
		cleaner_1.setSpeed(0);
		cleaner_1.turnTo(90);
	
		double q = cleaner_1.getShortestDistDir();
	
		if ((q + cleaner_1.getHead()) > 180) {
			cleaner_1.turnTo(179.8);
		} else {
			cleaner_1.turnTo(cleaner_1.getHead() + q);
		}
	
		while (cleaner_1.getFrontRange() > 0.9) {
			cleaner_1.setSpeed(FAST);
			cleaner_1.pause(20);
	
		}
		cleaner_1.setSpeed(0);
	
		TargetBox start = new TargetBox(cleaner_1.getBot(), cleaner_1.getX(), cleaner_1.getY(),
				cleaner_1.getHead(), getDir(), 2.0, "start");
		cleaner_1.addStart(start);
		in = true;
		prevIn = true;
	
		System.out.println("Y top = " + start.getTop() + " Y bottom = "
				+ start.getBottom() + "\n X left = " + start.getLeft()
				+ " X Right = " + start.getRight());	
		
	}

	/**
	 * Wall follow thread.
	 * 
	 * @param i
	 *            0 = antiClockwise follow. 1 = Clockwise Follow
	 * 
	 */
	protected synchronized void wallFollowThread(int i) {
		dir = i;
		final Thread wallFollow = new Thread() {
			@Override
			public void run() {
				while (!STOP) 
				{

					if (dir == 0) {
						cleaner_1.setSpeed(0);
						cleaner_1.setTRate(0.2);
						try {
							Thread.sleep((long)(((90*d+ cleaner_1.calcTurn(0))/0.2)*1000));
						} catch (InterruptedException e) {
							
						}
						cleaner_1.setTRate(0);
						// turn(90*d + calcTurn(0),0.4);
						cleaner_1.setSpeed(0.4);
					}
					if (dir == 1) {						
						cleaner_1.setSpeed(0);
						cleaner_1.setTRate(-0.3);
						try {
							Thread.sleep((long)(((180*d + cleaner_1.calcTurn(0))/0.3)*1000));
						} catch (InterruptedException e) {
							
						}
						cleaner_1.setTRate(0);
						cleaner_1.setTRate(0.5);						
						cleaner_1.pause((long)(((90*d)/0.5)*1000));						
							
						
						cleaner_1.setTRate(0);
						
						cleaner_1.setSpeed(0.4);
						
					} else {
						System.err.println("Invalid direction!");
						System.exit(1);
					}

					while (cleaner_1.getFrontRange() > 0.9) {
						
						
							cleaner_1.setSpeed(SLOW);
						

						/*
						 * Doorway algorithm
						 */

						double r11 = cleaner_1.getRange(11);
						double r12 = cleaner_1.getRange(12);

						if (r11 > (2 * r12)) {
							
							
							cleaner_1.setSpeed(0.2);
								while(cleaner_1.getRange(12)*2 < r11);
								cleaner_1.setSpeed(0);
								cleaner_1.setTRate(0.2);
								try {
									Thread.sleep((long)((45*d/0.2)*1000));
								} catch (InterruptedException e) {
									
								}
								cleaner_1.setTRate(0);
								// turn(45*d, 0.3);
								cleaner_1.setSpeed(0.2);
								cleaner_1.pause(500);
								cleaner_1.setSpeed(0);
								cleaner_1.setTRate(0.3);
								try {
									Thread.sleep((long)((40*d/0.3)*1000));
								} catch (InterruptedException e) {
									
								}
								cleaner_1.setTRate(0);
								// turn(40*d, 0.3);						
	
								cleaner_1.setSpeed(SLOW);
								// pause(1500);
							
	
								counter = 1;
						}

						/*
						 * Proximity algorithm
						 */

						if (cleaner_1.getRange(6) < 0.94) {
							counter = 1;
							System.out.println("prox");
							cleaner_1.setSpeed(-0.4);
							cleaner_1.pause(1000);
							cleaner_1.setSpeed(0);
							cleaner_1.setTRate(-0.5);
							try {
								Thread.sleep((long)((10*d/0.5)*1000));
							} catch (InterruptedException e) {
								
							}
							cleaner_1.setTRate(0);
							//turn( - (10 * d), 0.5);
							cleaner_1.setSpeed(SLOW);
						}
						
						

						if (cleaner_1.getRange(2) < 0.94) {
							counter = 1;
							System.out.println("prox");
							cleaner_1.setSpeed(-0.4);
							cleaner_1.pause(1000);
							cleaner_1.setSpeed(0);
							cleaner_1.setTRate(0.5);
							try {
								Thread.sleep((long)((10*d/0.5)*1000));
							} catch (InterruptedException e) {
								
							}
							cleaner_1.setTRate(0);
							//turn((10 * d), 0.5);
							cleaner_1.setSpeed(SLOW);
						}

						/*
						 * Wall Align algorithm
						 */

						if ((counter % 15) == 0) {
							
							cleaner_1.setSpeed(0);
							cleaner_1.turn(cleaner_1.calcTurn(1), 0.25);
							cleaner_1.setSpeed(SLOW);
						}
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
						}
						counter++;

						/***************************/

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}

					}

				}
				cleaner_1.setSpeed(0);
				

			}
		};
		wallFollow.start();
	}

	/**
	 * Target thread.
	 */
	protected synchronized void targetThread() {
		Thread target = new Thread() {
			@Override
			public void run() {
				while (!STOP) {					
					
					for (int count = 0; count < cleaner_1.getStart().size(); count++) {						
						
						if ((cleaner_1.getStart().get(count).getIndex() != getIndex())
								&& cleaner_1.getStart().get(count).checkTarget(cleaner_1.getX(),
										cleaner_1.getY())) {
							mcp.setWallF(true);
							System.out.println("2");
							STOP = true;
							
						}
						
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}

				}
			}
		};
		target.start();
	}
	
	
	protected synchronized void targetOwnThread() {
		Thread targetOwn = new Thread() {
			@Override
			public void run() {
				boolean counter = false;
				while (!STOP) {
					if (!counter)
					{
						try {
							Thread.sleep(90000);
						} catch (InterruptedException e) {
						}
						System.out.println(cleaner_1.getBot()+" OwnBox go");
						counter = true;
					}
					for (int count = 0; count < cleaner_1.getStart().size(); count++) {						
						
						if (cleaner_1.getStart().get(count).checkTarget(cleaner_1.getX(),
										cleaner_1.getY())) {
							
							mcp.setWallF(true);
							System.out.println("1");
							STOP = true;
							
							
							
						}
						
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}

				}
			}
		};
		targetOwn.start();
	}
	
	// CONTROL METHODS

}