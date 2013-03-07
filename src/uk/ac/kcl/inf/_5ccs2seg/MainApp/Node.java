package uk.ac.kcl.inf._5ccs2seg.MainApp;

/**
 * 
 * @author Adrian Bocai for Team Dijkstra
 *
 */
public class Node {
	private int[] arr = new int[7];
	
	public Node(int i, int j){
		arr[0] = i;
		arr[1] = j;
	}
	
	public int getArr(int i){
		return arr[i];
	}
	
	public int[] getArr(){
		return arr;
	}
	
	public void setFSc(int f){
		arr[2] = f;
	}
	public void setGSc(int g){
		arr[3] = g;
	}
	public void setHSc(int h){
		arr[4] = h;
	}
	public void setSc(int f, int g, int h){
		arr[2] = f; arr[3] = g; arr[4] = h;
	}
	
	public void setParent(int i, int j){
		arr[5] = i;
		arr[6] = j;
	}
	
	public void setParent(Node n){
		arr[5] = n.getArr(0);
		arr[6] = n.getArr(1);
	}

	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Node n = (Node) obj;
        if (this.arr[0] == n.arr[0] && this.arr[1] == n.arr[1]) {
            return true;
        }
        return false;
    }
 
	
	public String toString(){
		String res;
		
		res = "("+arr[0]+","+arr[1]+") ; F="+arr[2]+", G="+arr[3]+", H="+arr[4]+"; Par("+arr[5]+","+arr[6]+") ||";
		
		return res;
	
	}
		
}