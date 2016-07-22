/**
 * Created by Ali J on 4/26/2016.
 */
import javax.swing.*;
import java.awt.*;

public class TankBullet extends GameObject {
	// Global
	private int x,y;
	private boolean done;
	private Image bulletImage;
	
	public TankBullet(int tankY){
		x = 722;
		y = tankY + 40;
		done = false;
		//bulletImage = (Image)new ImageIcon(getClass().getResource("images/Tank/bullet.gif")).getImage();
		bulletImage = (Image)(new ImageIcon("images/Tank/bullet.gif").getImage());
	}// end TankBullet()
	public int getX(){ return x; }
	public int getY(){ return y; }
	public boolean getDone(){ return done; }
	@Override
	void draw(Graphics g) {
		if(x >= -1){
			g.drawImage(bulletImage,x, y, 30, 8,null);
		}else {
			done = true;
		}
		
	}// end draw()
	@Override
	void update() {
		if(x >= -1)
			x -= 10;
	}// end update()
}// end TankBullet Class
