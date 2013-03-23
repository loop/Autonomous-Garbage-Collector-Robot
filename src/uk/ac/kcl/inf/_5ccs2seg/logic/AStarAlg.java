package uk.ac.kcl.inf._5ccs2seg.logic;
import java.util.ArrayList;

import uk.ac.kcl.inf._5ccs2seg.data.GridMap;
import uk.ac.kcl.inf._5ccs2seg.data.Node;

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
	private boolean noPath = false;
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
	public ArrayList<Node> plan() {

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
					
					//if(ii == i || jj == j )
					// step 3a
					//System.out.println(map.getMap()[i][j]);
					if ((map.getMap()[i][j] == 1 || map.getMap()[i][j] == 3) && closeLi.indexOf(cur) < 0) { // if
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
		ArrayList<Node> res = new ArrayList<Node>();
		if (!noPath){
		Node currr = closeLi.get(closeLi.size() - 1);
		res.add(currr);

		while (true) {
			currr = closeLi.get(closeLi.indexOf(new Node(currr.getArr(5),currr.getArr(6))));
			if (currr.getArr(5) == 0 && currr.getArr(6) == 0) {
				break;
			}
			res.add(currr);
				
		}
		}
		else {
			res.add(start);
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
		if (openLi.isEmpty() && !current.equals(start)){
			stop = true; 
			noPath = true;
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
		
		//calc score that ajusts for wall distance
		int difI;
		int difJ;
		int score;
		int bScore = 0;
	
		
		for (int a = 0; a<8; a++){
			if (a == 0){difI = 0; difJ = 1;}
			else if (a == 1){difI = 0; difJ = -1;}
			else if (a == 2){difI = 1; difJ = 0;}
			else if (a == 3){difI = -1; difJ = 0;}
			
			else if (a == 4){difI = 1; difJ = 1;}
			else if (a == 5){difI = -1; difJ = -1;}
			else if (a == 6){difI = -1; difJ = 1;}
			else {difI = 1; difJ = -1;}
				
			
		for (int z = 1; z<=10; z++){
			if (map.getMap()[arr[0] + difI][arr[1] + difJ] == 6){
				score = 880 - (z * 80);
				if (z == 1){ score = score + 5000;}
				else if (z == 2){ score = score + 3000;}
				else if (z == 3){ score = score + 300;}
		
				
				if (score > bScore) {bScore = score;}
				break;
			}
			if (difI > 0){difI++;}
			if (difI < 0){difI--;}
			if (difI > 0){difJ++;}
			if (difI < 0){difJ--;}
		}
		}
		
		
		// calc F
		res[0] = res[0] + bScore;
		
		return res;
	}

}