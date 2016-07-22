/**
 * Created by Ali J on 4/26/2016.
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Monster extends GameObject {
	// Global
	private int x,y,hitCount,imageFrameNr,explodeFrameNr,monstersKilled;
	private Tank tank;
	private boolean explode,die,removeMe;
	private ArrayList<Image> imageFrames, explosionFrames;
	
	public Monster(Tank mytank,int monstersKilledNow){
		tank = mytank;
		setExplode(false);
		setDie(false);
		setX(x);
		setY( (int)(Math.random() * 460) + 30 );
		imageFrameNr = 0;
		monstersKilled = monstersKilledNow;
		imageFrames = new ArrayList<Image>();
		explosionFrames = new ArrayList<Image>();
		loadImages();
	}// end Monster()
	// Set
	public void setExplode(boolean explodeStatus){ explode = explodeStatus; }
	public void setDie(boolean dieStatus){ die = dieStatus; }
	public void setBulletHit(){	hitCount++; }
	public void setRemoveMe(boolean removeStatus){ removeMe = removeStatus; }
	public void setX(int xValue){ x = xValue; }
	public void setY(int yValue){ y = yValue; }
	// Get
	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getExplosionFrameNr(){ return explodeFrameNr; }
	public int getBulletHits(){	return hitCount; }
	public boolean getRemoveMe(){ return removeMe; }
	public boolean getExplode(){ return explode;	}
	public boolean getDied(){ return die; }
	
	public void loadImages(){
		try {
			for(int i = 1; i < 13; i++){
				//Image a = (Image)new ImageIcon(getClass().getResource("images/Enemy/" + i + ".gif")).getImage();
				Image a = (Image)new ImageIcon("images/Enemy/" + i + ".gif").getImage();
				imageFrames.add(a);
			}
			for(int i = 1; i < 24; i++){
				//Image a = (Image)new ImageIcon(getClass().getResource("images/Explosion/" + i + ".gif")).getImage();
				Image a = (Image)new ImageIcon("images/Explosion/" + i + ".gif").getImage();
				explosionFrames.add(a);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}// end loadImages()
	@Override
	void draw(Graphics g) {
		if(!explode && !die){
			g.drawImage(imageFrames.get(imageFrameNr), x, y, null);
		}else if(explode || die){
			g.drawImage(explosionFrames.get(explodeFrameNr), x, (y - 150), null);
		}
		
	}// end draw()
	@Override
	void update() {
		if(y > (tank.getY() - 50) && y < (tank.getY() + 50) && x >= 670 && x <= 770 ){
			setExplode(true);
		}else if(hitCount >= 1){ die = true; }
		
		if(!explode && !die){
			if(x >= 800){ setRemoveMe(true); }
			else{	
				if(monstersKilled >= 28)
					x +=15;
				else
					x+=1;
			}
			
			if(imageFrameNr >= (imageFrames.size()-1)){
				imageFrameNr = 0;
			}else{ imageFrameNr++;	}
			
		}else if(explode || die){
			if(explodeFrameNr >= (explosionFrames.size()-1)){
				// Do nothing.
			}else{	explodeFrameNr++; }
		}
	}// update()
}// end Monster Class
