package uk.ac.kcl.inf._5ccs2seg.MainApp;
import java.awt.GridLayout;

import javaclient3.structures.fiducial.PlayerFiducialItem;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**Creates a top-level window with all the sensor data about a Bot 
 * 
 * @author Adrian Bocai
 *
 */
public class Debug {
	
	private Bot bot;
	private double[] arr;
	private String sts;
	private String beam;
	private int fidCount;
	private PlayerFiducialItem[] fid;
	private String fid1;
	private String fid2;
	private String fid3;
	private String fid4;
	
	
	public Debug(final Bot bot, int botNo){
		
		this.bot = bot;
		
		final JLabel positionLabel = new JLabel("------- n/a -------");
        final JLabel yawLabel = new JLabel("------- n/a -------");
        final JLabel speedLabel = new JLabel("------- n/a -------");
        final JLabel rateLabel = new JLabel("------- n/a -------");
       
        
        final JLabel frontL= new JLabel("------- n/a -------");
        final JLabel frontM= new JLabel("------- n/a -------");
        final JLabel frontR= new JLabel("------- n/a -------");
        final JLabel backL= new JLabel("------- n/a -------");
        final JLabel backM= new JLabel("------- n/a -------");
        final JLabel backR= new JLabel("------- n/a -------");
        final JLabel rightF= new JLabel("------- n/a -------");
        final JLabel rightM= new JLabel("------- n/a -------");
        final JLabel rightB= new JLabel("------- n/a -------");
        final JLabel leftF= new JLabel("------- n/a -------");
        final JLabel leftM= new JLabel("------- n/a -------");
        final JLabel leftB= new JLabel("------- n/a -------");
        
        final JLabel gripS= new JLabel("------- n/a -------");
        final JLabel gripB= new JLabel("------- n/a -------");
        
        final JLabel count= new JLabel("------- n/a -------");
        
        final JLabel id1= new JLabel("------- n/a -------");
        final JLabel dist1= new JLabel("------- n/a -------");
        final JLabel xDist1= new JLabel("------- n/a -------");
        final JLabel yDist1= new JLabel("------- n/a -------");
        
        final JLabel id2= new JLabel("------- n/a -------");
        final JLabel dist2= new JLabel("------- n/a -------");
        final JLabel xDist2= new JLabel("------- n/a -------");
        final JLabel yDist2= new JLabel("------- n/a -------");
        
        final JLabel id3= new JLabel("------- n/a -------");
        final JLabel dist3= new JLabel("------- n/a -------");
        final JLabel xDist3= new JLabel("------- n/a -------");
        final JLabel yDist3= new JLabel("------- n/a -------");
        
        
        
        

        JFrame frame = new JFrame("Bot" + (botNo+1) + " Monitor");
        JPanel fiduc = new JPanel(new GridLayout(20,2));
        JPanel contentPane = new JPanel();
        JPanel content = new JPanel(new GridLayout(1,2));
        contentPane.setLayout(new GridLayout(28,2));
        contentPane.add(new JLabel("<html><h2>Robot Pose</h2></html>"));
        contentPane.add(new JLabel());
        contentPane.add(new JLabel("Position"));
        contentPane.add(positionLabel);
        contentPane.add(new JLabel("Yaw"));
        contentPane.add(yawLabel);
        contentPane.add(new JLabel("Forward Speed"));
        contentPane.add(speedLabel);
        contentPane.add(new JLabel("Turn Rate"));
        contentPane.add(rateLabel);

        contentPane.add(new JLabel());contentPane.add(new JLabel());
        
        contentPane.add(new JLabel("<html><h2>Gripper Sensors</h2></html>"));
        contentPane.add(new JLabel());
        contentPane.add(new JLabel("Gripper Status"));
        contentPane.add(gripS);
        contentPane.add(new JLabel("Object inside Gripper"));
        contentPane.add(gripB);
        
       
        contentPane.add(new JLabel());contentPane.add(new JLabel());
        
        contentPane.add(new JLabel("<html><h2>Range Sensors</h2></html>"));
        contentPane.add(new JLabel());
        contentPane.add(new JLabel("Front Left"));
        contentPane.add(frontL);
        contentPane.add(new JLabel("Front Middle"));
        contentPane.add(frontM);
        contentPane.add(new JLabel("Front Right"));
        contentPane.add(frontR);
        contentPane.add(new JLabel());contentPane.add(new JLabel());
        contentPane.add(new JLabel("Left Front"));
        contentPane.add(leftF);
        contentPane.add(new JLabel("Left Middle"));
        contentPane.add(leftM);
        contentPane.add(new JLabel("Left Back"));
        contentPane.add(leftB);
        contentPane.add(new JLabel());contentPane.add(new JLabel());
        contentPane.add(new JLabel("Right Front"));
        contentPane.add(rightF);
        contentPane.add(new JLabel("Right Middle"));
        contentPane.add(rightM);
        contentPane.add(new JLabel("Right Back"));
        contentPane.add(rightB);
        contentPane.add(new JLabel());contentPane.add(new JLabel());
        contentPane.add(new JLabel("Back Left"));
        contentPane.add(backL);
        contentPane.add(new JLabel("Back Middle"));
        contentPane.add(backM);
        contentPane.add(new JLabel("Back Right"));
        contentPane.add(backR);
        
        content.add(contentPane);
        
        fiduc.add(new JLabel("<html><h2>Fiducial Sensors</h2></html>"));
        fiduc.add(new JLabel());
        fiduc.add(new JLabel("Objects Discovered"));
        fiduc.add(count);
        fiduc.add(new JLabel());fiduc.add(new JLabel());
        
        fiduc.add(new JLabel("Object type"));
        fiduc.add(id1);
        fiduc.add(new JLabel("Distance to first object"));
        fiduc.add(dist1);
        fiduc.add(new JLabel("X-distance to first object"));
        fiduc.add(xDist1);
        fiduc.add(new JLabel("Y-distance to first object"));
        fiduc.add(yDist1);
        
        fiduc.add(new JLabel());fiduc.add(new JLabel());
        
        fiduc.add(new JLabel("Object type"));
        fiduc.add(id2);
        fiduc.add(new JLabel("Distance to second object"));
        fiduc.add(dist2);
        fiduc.add(new JLabel("X-distance to second object"));
        fiduc.add(xDist2);
        fiduc.add(new JLabel("Y-distance to second object"));
        fiduc.add(yDist2);
        
        fiduc.add(new JLabel());fiduc.add(new JLabel());
        
        fiduc.add(new JLabel("Object type"));
        fiduc.add(id3);
        fiduc.add(new JLabel("Distance to third object"));
        fiduc.add(dist3);
        fiduc.add(new JLabel("X-distance to third object"));
        fiduc.add(xDist3);
        fiduc.add(new JLabel("Y-distance to third object"));
        fiduc.add(yDist3);
        
        content.add(fiduc);
        
        frame.setContentPane(content);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        /*
        
        */
		
        //thread for updating values
        Thread reportThread = new Thread() {
            public void run() {
                while (true) {
                    positionLabel.setText(String.format("(%4.2f,%4.2f)",bot.getX(),bot.getY()));
                    yawLabel.setText(String.format("%4.4f rad, %3.0f deg",bot.getYaw(),bot.getYaw()*180/Math.PI));
                    speedLabel.setText(String.format("%4.2f",bot.getFSpeed()));
                    rateLabel.setText(String.format("%4.2f",bot.getTRate()));
                    
                     arr = bot.getRanges();
                    
                    frontL.setText(String.format("%4.2f",arr[0]));
                    frontM.setText(String.format("%4.2f",arr[1]));
                    frontR.setText(String.format("%4.2f",arr[2]));
                    leftF.setText(String.format("%4.2f",arr[3]));
                    leftM.setText(String.format("%4.2f",arr[4]));
                    leftB.setText(String.format("%4.2f",arr[5]));
                    rightF.setText(String.format("%4.2f",arr[6]));
                    rightM.setText(String.format("%4.2f",arr[7]));
                    rightB.setText(String.format("%4.2f",arr[8]));
                    backL.setText(String.format("%4.2f",arr[9]));
                    backM.setText(String.format("%4.2f",arr[10]));
                    backR.setText(String.format("%4.2f",arr[11]));
                    
                   
                    if(bot.getGripSts() == 1){sts = "Open";}
         			else if(bot.getGripSts()== 2){sts = "Closed";}
         			else {sts = "In transition";}
         
         			if(bot.getGripBeam() != 0) {beam = "Yes";}
         				else {beam = "No";}   
         			
                    gripS.setText("   " + sts);
                    gripB.setText("   " + beam);
                    
                    fidCount = bot.getFidCount();
                    fid = bot.getFiducials();
                    
                    if (fidCount > 0){count.setText(""+fidCount);calcFid(fid[0]);}
                    else {count.setText("NOT TRACKING"); calcFid();}
                    
                    id1.setText(fid1);
                    dist1.setText(fid2);
                    xDist1.setText(fid3);
                    yDist1.setText(fid4);
                    
                    if (fidCount > 1){calcFid(fid[1]);}
                    else {calcFid();}
                    
                    id2.setText(fid1);
                    dist2.setText(fid2);
                    xDist2.setText(fid3);
                    yDist2.setText(fid4);
                    
                    if (fidCount > 2){calcFid(fid[2]);}
                    else {calcFid();}
                    
                    id3.setText(fid1);
                    dist3.setText(fid2);
                    xDist3.setText(fid3);
                    yDist3.setText(fid4);
                   
                    
                    
                    try {Thread.sleep(250);} catch (InterruptedException e) {}
                }
            }
        };
        reportThread.start();
		
	}
	
	private void calcFid(PlayerFiducialItem fid){
		if (fid.getId() == 1) {fid1 = "Another Bot (1)";}
		else if (fid.getId() == 5){fid1 = "Garbage (5)";}
		else if (fid.getId() == 6){fid1 = "Garbage (6)";}
		else {fid1 = "Garbage (7)";}
		
		fid3 = String.format("%4.2f",fid.getPose().getPx());
		fid4 = String.format("%4.2f",fid.getPose().getPy());
		fid2 =  String.format("%4.2f", Math.sqrt(Math.pow(fid.getPose().getPy(),2) + Math.pow(fid.getPose().getPx(),2)));
	}
	
	private void calcFid(){
		fid1 = "Not Tracking";
		fid2 = "Not Tracking";
		fid3 = "Not Tracking";
		fid4 = "Not Tracking";
	}
	
}
