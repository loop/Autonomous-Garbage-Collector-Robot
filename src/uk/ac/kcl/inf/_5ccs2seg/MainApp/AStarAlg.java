package uk.ac.kcl.inf._5ccs2seg.MainApp;

import java.util.ArrayList;

/**
 * Astar algorithm for finding a path between 2 coordinates;
 * 
 * @author Adrian Bocai for Team Dijkstra
 * 
 */
public class AStarAlg {
	private GridMap map;
	private Node start;
	private Node current;
	private Node goal;
	private boolean stop = false;
	private ArrayList<Node> openLi = new ArrayList<Node>();
	private ArrayList<Node> closeLi = new ArrayList<Node>();

	public AStarAlg(Node start, Node goal, GridMap map) {

		this.map = map;
		this.start = start;
		this.goal = goal;
		this.start.setFSc(0);

		openLi.add(this.start);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<double[]> plan() {

		while (true) {

			// STEP 1 and 2
			step1and2(); // Get lowest F score (open) move it to close. This is
							// current

			if (stop) {
				break;
			}

			// STEP 3 //for each square adjacent to current do:
			Node cur;
			int[] scArr;
			int index;
			int ii = current.getArr(0);
			int jj = current.getArr(1);
			for (int i = ii - 1; i <= ii + 1; i++) {
				for (int j = jj - 1; j <= jj + 1; j++) {
					cur = new Node(i, j);

					// step 3a
					if (map.getMap()[i][j] == 1 && closeLi.indexOf(cur) < 0) { // if
																				// it
																				// is
																				// free
																				// and
																				// not
																				// on
																				// close
						index = openLi.indexOf(cur);

						// step 3a(I)
						if (index < 0) { // if it is not on open
							cur.setParent(current);
							scArr = calcScore(cur.getArr(), false);
							cur.setSc(scArr[0], scArr[1], scArr[2]);
							openLi.add(cur);
						}

						// step3a(II)
						else { // if it is on open
							cur.setParent(current);
							int[] a = openLi.get(index).getArr();
							if (calcScore(cur.getArr(), true)[1] < a[3]) {
								cur.setHSc(a[4]);
								scArr = calcScore(cur.getArr(), true);
								cur.setGSc(scArr[1]);
								cur.setFSc(scArr[0]);
								openLi.set(index, cur);
							}
						}
					}
				}
			}
		}

		// System.out.println(closeLi);
		// PATH
		int count = 0;
		ArrayList<double[]> res = new ArrayList<double[]>();
		int[] arr = closeLi.get(closeLi.size() - 1).getArr();
		res.add(map.arrayIndexToCoordCalc(arr[0], arr[1]));

		while (true) {
			arr = closeLi.get(closeLi.indexOf(new Node(arr[5], arr[6])))
					.getArr();
			if (arr[5] == 0 && arr[6] == 0) {
				break;
			}
			if (count == 1) {
				res.add(map.arrayIndexToCoordCalc(arr[0], arr[1]));
				count = 0;
			} else {
				count = count + 1;
			}
		}

		// System.out.println(res);

		return res;
	}

	/**
	 * Searches the array and chooses the node with the lowest F score, then
	 * updates
	 */
	private void step1and2() {
		int curLow = 1000000000;
		int cur;

		for (int i = 0; i < openLi.size(); i++) {
			cur = openLi.get(i).getArr(2);
			if (cur < curLow) {
				curLow = cur;
				current = openLi.get(i);
			}
		}
		closeLi.add(current);
		openLi.remove(current);
		if (current.equals(goal)) {
			stop = true;
		}
	}

	/**
	 * Calculates the F,G and H scores
	 * 
	 * @param arr
	 *            the information stored in a node
	 * @param skip
	 *            skips the calculation of the H score if true
	 * @return array of the 3 scores
	 */
	private int[] calcScore(int[] arr, boolean skip) {
		int[] res = new int[3];

		// calc G
		if (arr[0] == arr[5] || arr[1] == arr[6]) {
			res[1] = current.getArr(3) + 10;
		} else {
			res[1] = current.getArr(3) + 14;
		}

		// calc H
		if (!skip) {
			int sq = 0;
			int i = arr[0];
			int gI = goal.getArr(0);
			int j = arr[1];
			int gJ = goal.getArr(1);
			int iDir;
			int jDir;

			if (i != gI) {
				if (i < gI) {
					iDir = 1;
				} else {
					iDir = -1;
				}
				while (i != gI) {
					sq++;
					i = i + iDir;
				}
			}
			if (j != gJ) {
				if (j < gJ) {
					jDir = 1;
				} else {
					jDir = -1;
				}
				while (j != gJ) {
					sq++;
					j = j + jDir;
				}
			}

			res[2] = sq * 10;
			arr[4] = res[2];

		}

		// calc F
		res[0] = res[1] + arr[4];

		return res;
	}

}