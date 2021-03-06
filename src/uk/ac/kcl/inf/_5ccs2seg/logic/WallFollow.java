package uk.ac.kcl.inf._5ccs2seg.logic;

import javaclient3.structures.fiducial.PlayerFiducialItem;

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
	public WallFollow(Bot cleaner) {
		cleaner_1 = cleaner;
		cleaner_1.turnTo(90);
		while(cleaner_1.getBackRange() > 0.4 )
		{
			cleaner_1.setSpeed(-0.4);
			cleaner_1.pause(20);
		}
		cleaner_1.setSpeed(0);


	}

	public WallFollow(Bot cleaner, MasterControlProgram mcp) {
		this.mcp = mcp;
		mcp.setMaping(true);
		cleaner_1 = cleaner;
		findNearestWall();
		Target();
		wallFollowThread(1);
		targetThread();
		targetOwnThread();
		// botSeenThread();

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
		
		cleaner_1.turn(-90*d, 0.5);
		
		while (cleaner_1.getFrontRange() > 0.9) {
			cleaner_1.setSpeed(FAST);
			cleaner_1.pause(20);

		}
		
		cleaner_1.setSpeed(0);
		
		
		

	}

	public void Target() {
		TargetBox start = new TargetBox(cleaner_1.getBot(), cleaner_1.getX(),
				cleaner_1.getY(), cleaner_1.getHead(), getDir(), 2.0, "start");
		cleaner_1.addStart(start);
		in = true;
		prevIn = true;

		

	}

	public void corner() {
		cleaner_1.setTRate(-0.15);
		try {
			Thread
					.sleep((long) (((90 * d + cleaner_1.calcTurn(0)) / 0.15) * 1000));
		} catch (InterruptedException e) {

		}

		cleaner_1.setTRate(0);
		cleaner_1.setSpeed(0.4);
		while (cleaner_1.getFrontRange() > 0.9) {
			cleaner_1.setSpeed(FAST);
			cleaner_1.pause(20);
		}
		cleaner_1.setSpeed(0);
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
				while (!STOP) {

					if (dir == 1) {
						cleaner_1.setSpeed(-0.2);

						try {
							Thread.sleep(200);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						cleaner_1.setSpeed(0);

						if (cleaner_1.getLeftRange()
								+ cleaner_1.getRightRange() < 1.2) {
							cleaner_1.setSpeed(-0.5);
							while (cleaner_1.getRightRange() < 0.7) {
								cleaner_1.pause(10);
							}
							cleaner_1.pause(500);
							cleaner_1.setSpeed(0);
						}

						cleaner_1.setTRate(-0.15);
						try {
							Thread.sleep((long) (((180 * d + cleaner_1
									.calcTurn(0)) / 0.15) * 1000));
						} catch (InterruptedException e) {

						}
						 cleaner_1.setTRate(0);
						 cleaner_1.setTRate(0.5);
						 cleaner_1.pause((long)(((90*d)/0.5)*1000));

						cleaner_1.setTRate(0);

						cleaner_1.setSpeed(0.4);

					}

					while (cleaner_1.getFrontRange() > 0.8) {

						cleaner_1.setSpeed(SLOW);

						/*
						 * Doorway algorithm
						 */

						double r11 = cleaner_1.getRange(11);
						double r12 = cleaner_1.getRange(12);

						if (r11 > (2 * r12)) {

							cleaner_1.setSpeed(0.2);
							while (cleaner_1.getRange(12) * 2 < r11)
								;
							cleaner_1.setSpeed(0);
							cleaner_1.setTRate(0.15);
							try {
								Thread.sleep((long) ((45 * d / 0.15) * 1000));
							} catch (InterruptedException e) {

							}
							cleaner_1.setTRate(0);
							// turn(45*d, 0.3);
							cleaner_1.setSpeed(0.2);
							cleaner_1.pause(500);
							cleaner_1.setSpeed(0);
							cleaner_1.setTRate(0.15);
							try {
								Thread.sleep((long) ((40 * d / 0.15) * 1000));
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

						if (cleaner_1.getRange(7) < 0.95) {
							counter = 1;
						
							cleaner_1.setSpeed(-0.15);
							cleaner_1.pause(1250);
							cleaner_1.setSpeed(0);
							cleaner_1.setTRate(-0.5);
							try {
								Thread.sleep((long) ((10 * d / 0.5) * 1000));
							} catch (InterruptedException e) {

							}
							cleaner_1.setTRate(0);
							// turn( - (10 * d), 0.5);
							cleaner_1.setSpeed(SLOW);
						}

						if (cleaner_1.getRange(1) < 0.94) {
							counter = 1;
						
							cleaner_1.setSpeed(-0.4);
							cleaner_1.pause(1000);
							cleaner_1.setSpeed(0);
							cleaner_1.setTRate(0.5);
							try {
								Thread.sleep((long) ((10 * d / 0.5) * 1000));
							} catch (InterruptedException e) {

							}
							cleaner_1.setTRate(0);
							// turn((10 * d), 0.5);
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
								&& cleaner_1.getStart().get(count).checkTarget(
										cleaner_1.getX(), cleaner_1.getY())) {
							mcp.setWallF(true);
							
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
					if (!counter) {
						try {
							Thread.sleep(90000);
						} catch (InterruptedException e) {
						}
						
						counter = true;
					}
					for (int count = 0; count < cleaner_1.getStart().size(); count++) {

						if (cleaner_1.getStart().get(count).checkTarget(
								cleaner_1.getX(), cleaner_1.getY())) {

							mcp.setWallF(true);
						
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

	protected synchronized void botSeenThread() {
		Thread botSeen = new Thread() {
			@Override
			public void run() {

				while (!STOP) {

					PlayerFiducialItem[] fid = cleaner_1.getFiducials();
					for (int i = 0; i < fid.length; i++) {
						if (fid[i].getId() == 1) {
							double dx = fid[i].getPose().getPx();
							double dy = fid[i].getPose().getPy();
							double d = Math.sqrt((dx * dx) + (dy * dy));

							while (d < 1.5 && cleaner_1.getBackRange() < 0.4) {
								cleaner_1.setSpeed(dx - 2);
							}

						}
					}
				}
			}
		};
		botSeen.start();
	}

	// CONTROL METHODS

}