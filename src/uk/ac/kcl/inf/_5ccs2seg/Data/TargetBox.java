package uk.ac.kcl.inf._5ccs2seg.Data;

/**
 * @author Chris Jones
 * 
 */
public class TargetBox {
	private boolean inBox = false;
	private double initialX = 0.0;
	private double initialY = 0.0;
	private double topBoundry = 0.0;
	private double bottomBoundry = 0.0;
	private double rightBoundry = 0.0;
	private double leftBoundry = 0.0;
	private int botIndex = 0;
	private final String type;
	private final double heading;
	private final int direction;
	private int counter;

	/**
	 * @param index
	 *            the bots number
	 * 
	 * @param x
	 *            the x coordinate
	 * 
	 * @param y
	 *            the y coordinate
	 * 
	 * @param h
	 *            the heading
	 * 
	 * @param d
	 *            the direction
	 * 
	 * @param b
	 *            the size of the box
	 * 
	 * @param type
	 *            the type box
	 */
	public TargetBox(int index, double x, double y, double h, int d, double b,
			String type) {
		initialX = x;
		initialY = y;
		botIndex = index;
		this.type = type;
		heading = h;
		direction = d;
		counter = 0;

		topBoundry = initialY + (b / 2);
		bottomBoundry = initialY - (b / 2);
		rightBoundry = initialX + (b / 2);
		leftBoundry = initialX - (b / 2);

		/*
		 * if (this.type.equals("start")) { topBoundry = initialY + (b / 2);
		 * bottomBoundry = initialY - (b / 2); rightBoundry = initialX + (b /
		 * 2); leftBoundry = initialX - (b / 2); }
		 * 
		 * 
		 * 
		 * if (this.type.equals("door")) { if ((direction == 0) && ((heading >=
		 * -45.0) && (heading <= 45.0))) { topBoundry = initialY; bottomBoundry
		 * = initialY - b; rightBoundry = initialX + b; leftBoundry = initialX;
		 * }
		 * 
		 * if ((direction == 1) && ((heading >= -45.0) && (heading <= 45.0))) {
		 * topBoundry = initialY + b; bottomBoundry = initialY; rightBoundry =
		 * initialX + b; leftBoundry = initialX; }
		 * 
		 * // ============================================
		 * 
		 * if ((direction == 0) && ((heading > 45.0) && (heading <= 135.0))) {
		 * topBoundry = initialY + b; bottomBoundry = initialY; rightBoundry =
		 * initialX + b; leftBoundry = initialX; }
		 * 
		 * if ((direction == 1) && ((heading >= 45.0) && (heading <= 135.0))) {
		 * topBoundry = initialY + b; bottomBoundry = initialY; rightBoundry =
		 * initialX; leftBoundry = initialX - b; }
		 * 
		 * // ================================================
		 * 
		 * if ((direction == 0) && ((heading > 135.0) || (heading < -135.0))) {
		 * topBoundry = initialY + b; bottomBoundry = initialY; rightBoundry =
		 * initialX; leftBoundry = initialX - b; }
		 * 
		 * if ((direction == 1) && ((heading > 135.0) || (heading < 135.0))) {
		 * topBoundry = initialY; bottomBoundry = initialY - b; rightBoundry =
		 * initialX; leftBoundry = initialX - b; }
		 * 
		 * // ==================================================
		 * 
		 * if ((direction == 0) && ((heading >= -135.0) && (heading < -45.0))) {
		 * topBoundry = initialY; bottomBoundry = initialY - b; rightBoundry =
		 * initialX; leftBoundry = initialX - b; }
		 * 
		 * if ((direction == 1) && ((heading >= -135.0) && (heading < -45.0))) {
		 * topBoundry = initialY; bottomBoundry = initialY - b; rightBoundry =
		 * initialX + b; leftBoundry = initialX; } } `
		 */
	}

	/**
	 * @return initialX
	 */
	public double getInitialX() {
		return initialX;
	}

	/**
	 * @return initialY
	 */
	public double getInitialY() {
		return initialY;
	}

	/**
	 * @return topBoundry
	 */
	public double getTop() {
		return topBoundry;
	}

	public double getBottom() {
		return bottomBoundry;
	}

	public double getRight() {
		return rightBoundry;
	}

	public double getLeft() {
		return leftBoundry;
	}

	public int getIndex() {
		return botIndex;
	}

	public void addCounter() {
		counter++;
	}

	public int getCounter() {
		return counter;
	}

	public boolean checkTarget(double x, double y) {
		double currentX = x;
		double currentY = y;

		if ((currentY <= this.getTop()) && (currentY >= this.getBottom())) {
			if ((currentX <= this.getRight()) && (currentX >= this.getLeft())) {
				inBox = true;
			}
		} else {
			inBox = false;
		}

		return inBox;
	}

}
