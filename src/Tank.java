/**
 * Created by Ali J on 4/26/2016.
 */
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Tank extends GameObject {
	// Global
	private ArrayList<Image> imageFrames;
	private int y,fireNowFrame, tankHealth;
	private boolean fireNow;
	
	public Tank(){
		imageFrames = new ArrayList<Image>();
		fireNowFrame = 0;
		setY(0);
		setTankHealth(100);
		setFireNow(false);
		loadImages();
	}// end of Tank()
	
	// Get
	public int getFrames(){	return imageFrames.size(); }
	public int getY(){ return y; }
	public int getTankHealth(){	return tankHealth; }
	public boolean getFireNow(){ return fireNow; }
	// Set
	public void setTankDamaged(){ tankHealth -= 10; }
	public void setTankHealth(int newTankHealth){ tankHealth = newTankHealth; }
	public void fireNow(){ fireNow = true;	}
	public void setFireNow(boolean fireNowStatus){ fireNow = fireNowStatus; }
	public void setY(int newY){ y = newY; }
		
	
	// moveDown & moveUp checks if the tank is not over the screen first@
	public void moveDown(){	
		if(y <= 470){
			y += 18; 
		}
	}// end of moveDown()
	public void moveUp(){ 
		if(y >= 1){
			y -= 18; 
		}
	}// end of moveUp()
	public void loadImages(){
		try {
			for(int i = 1; i < 15; i++){
				//Image a = (Image)new ImageIcon(getClass().getResource("images/Tank/" + i + ".gif")).getImage();
				Image a = (Image)new ImageIcon("images/Tank/" + i + ".gif").getImage();
				imageFrames.add(a);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}// end of loadImages()
	public void draw(Graphics g){
		if(fireNow){
			if(fireNowFrame >= 13){
				fireNowFrame = 0;
				fireNow = false;
			}else{ fireNowFrame++; }
			
			g.drawImage(imageFrames.get(fireNowFrame), 700, y, null);
		}else{
			g.drawImage(imageFrames.get(0), 700, y, null);
		}
	}// end draw()
	@Override
	void update(){}	
}
