package uk.ac.kcl.inf._5ccs2seg.MainApp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class MapView extends Component {
	
	private int[][] grid;
	private int maxX;
	private int maxY;

	/**
	 * @param args
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		for(int i = 0; i < maxX; i++){
			for(int j = 0; j< maxY;j++){
			if(i%2==0){
			g.setColor(Color.BLACK);
			}else{
			g.setColor(Color.RED);
			}
			g.drawLine(i, j, i, j);
			}
			}
	}
	
	public MapView(MasterControlProgram mcp){
		grid = mcp.getMap();
		maxX = mcp.getMaxX();
		maxY = mcp.getMaxY();
		System.out.println(maxX);
		System.out.println(maxY);
		System.out.println(grid[0][0]);
		
		
	}

}
