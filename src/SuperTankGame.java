/**
 * Created by Ali J on 4/26/2016.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class SuperTankGame extends JFrame {

	// Global
	private BufferStrategy bf;
	private Graphics g;
	private boolean done;
	private long difference;
	private int monstersKilled,monsterPassed;
	private Tank mySuperTank;
	private ArrayList<GameObject> gameObjects;

	public SuperTankGame(){
		super("Ali J's Super Tank Game");
		monstersKilled = 0;
		done = false;
		gameObjects = new ArrayList<GameObject>();
		gameObjects.add(mySuperTank = new Tank());
		
		JOptionPane.showMessageDialog(null, "Welcome To SuperTank Game\n\n" +
				"Objective:\nKill 50 terrorists before they blow up on you.\n" +
				"If one of them bypasses you, then they will destroy your city and win." +
				"\n\nControls:\nUse the up & down arrow keys (or use the mouse) to navigate your tank." +
				"\nPress the space bar to shoot at enemies." +
				"\n\nGood Luck SuperSoldier !\n -Ali (Commander)." +
				"\n\n\nCopyright:" +
				"\nCoded by: Ali J");
				
		setSize(800,600);
		addListeners();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Senter JFrame to screen
		setResizable(false);
		setFocusable(true); // focus to listen to keyboard/mouse
		setVisible(true);
		createBufferStrategy(2); // we need to buffers.
		bf = getBufferStrategy();
		gameLoop();
	}// end Client()
	
	public void addListeners(){
		// Add mouseListeners
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(e.getY() <= 480){
					mySuperTank.setY(e.getY());
				}
			}
		}); // end addMouseMotionListener()
		
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tankFireBullet();
			}
		}); // end addMouseListener()
		
		// Add KeyboardListener
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE){
					tankFireBullet();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					mySuperTank.moveDown();
				} else if (e.getKeyCode() == KeyEvent.VK_UP){
					mySuperTank.moveUp();
				}
			} // end keyReleased()
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_DOWN){
					mySuperTank.moveDown();
				} else if (e.getKeyCode() == KeyEvent.VK_UP){
					mySuperTank.moveUp();
				}
			}// end keyPressed()
		}); // end KeyListener()
	} // end addKeyboardListeners()
	
	public void tankFireBullet(){
		mySuperTank.fireNow();
		gameObjects.add(new TankBullet(mySuperTank.getY()));
	}
	// Thanks to StackOverFlow Users & CodeGoat for finding correct FPS
	private void gameLoop(){
		long msPerFrame = 1000 / 30;
		long now = System.currentTimeMillis();
		long next = now + msPerFrame;
		
		while(!done)
		{
		    now = System.currentTimeMillis();
		    if(now >= next)
		    {
		        next = now + msPerFrame;
		        renderGame();
		    }
		    else
		    {
		        // no chance of negative, we know next > now
		        difference = next - now;
		        try { Thread.sleep(1); } 
		        catch(Exception ex) 
		        { 
		        	ex.printStackTrace(); 
		        }
		    }
		} // end while
		endGameScreen();
	}// end gameLoop()
	
	private void renderGame() {
				try {
										
					g = bf.getDrawGraphics();
					clearScreen();
					
					/*
					 * Bad performance while using short loop(s) !
					 * I do not really know why.
					 * 
					 * for(GameObject gObject : gameObjects){
						gObject.update();
					}

					for(GameObject gObject : gameObjects){
						gObject.draw(g);
					}	*/
					
					for(int i = 0; i < gameObjects.size();i++){
						gameObjects.get(i).update();
					}
					

					for(int i = 0; i < gameObjects.size();i++){
						gameObjects.get(i).draw(g);
					}
					
					//Detect Attacks
					detectAttack();
					
					// Remove unused game objects
					cleanBattleField();
					
					// Generate Enemy
					generateMonster();
					
					// Print Game Status information
					g.setColor(Color.black);
					g.fillRect(0, 565, 800, 40);
					g.setColor(Color.green);
					g.drawString(difference + " fps -  Tank Health ("+mySuperTank.getTankHealth()+")" +
							"                "   +
							"[ Monster(s) Killed: " + monstersKilled +" ]     " +
							"[ Monster(s) Passed You: "+ monsterPassed + " ] " +
							"                 By Ali J.", 10, 585);
				} catch(Exception e){
					e.printStackTrace();
				}finally {
					g.dispose();
				}
				bf.show();
				
				// Win or lose logic
			    if(mySuperTank.getTankHealth() <= 0 || monsterPassed >= 1 || monstersKilled >= 50)
			    {  done = true;   }
			    
	} // end renderGame()
	
	// See if a bullet hits a monster or not
	public void detectAttack(){
		for(int i = 0; i < gameObjects.size(); i++){
			if(gameObjects.get(i) instanceof TankBullet){
				
				TankBullet b = (TankBullet)gameObjects.get(i);
				
				for(int j = 0; j < gameObjects.size(); j++){
					if(gameObjects.get(j) instanceof Monster){
						
						Monster m = (Monster)gameObjects.get(j);
						
						if(b.getY() >= m.getY() && b.getY() <= (m.getY() + 80) && b.getX() >= m.getX() && b.getX() <= (m.getX() + 65)){
							m.setBulletHit();
							gameObjects.remove(i);
						}
					}
				}
			}
		}
	} // end detectAttack()

	// Removes unused objects from the GameObject ArrayList
	public void cleanBattleField(){
		// Bullets
		for(int i = 0; i < gameObjects.size(); i++){
			if(gameObjects.get(i) instanceof TankBullet){
				if(((TankBullet)gameObjects.get(i)).getDone()){
					gameObjects.remove(i);
				}
			}
		}
		
		// Monsters
		for(int i = 0; i < gameObjects.size(); i++){
			if(gameObjects.get(i) instanceof Monster){
				
				// If the monster hit the superTank !
				if(((Monster)gameObjects.get(i)).getExplode() && ((Monster)gameObjects.get(i)).getExplosionFrameNr() >= 21){
					gameObjects.remove(i);
					mySuperTank.setTankDamaged();
					
				// If tank's bullet hit a monster
				}else if(((Monster)gameObjects.get(i)).getDied() && ((Monster)gameObjects.get(i)).getExplosionFrameNr() >= 21){
					gameObjects.remove(i);
					monstersKilled++;
					
				// If the monster is outside the screen
				}else if(((Monster)gameObjects.get(i)).getRemoveMe()){
					gameObjects.remove(i);
					monsterPassed++;
				}
			}
		} 
	} // end cleanBattlefield()
		
	// Places monster(s) on the screen by random.
	public void generateMonster(){
		int nr = (int)(Math.random() * 100);
		if(nr == 99 || nr == 0 || nr == 20 || nr == 30 || nr == 50){
			gameObjects.add(new Monster(mySuperTank, monstersKilled));
		}
	} // end generateMonster
	
	// Clears game screen by drawing over old graphics
	public void clearScreen(){
		//g.drawImage(new ImageIcon(getClass().getResource("images/bg.gif")).getImage(), 0, 0, 800, 600, null);
		g.drawImage(new ImageIcon("images/bg.gif").getImage(), 0, 0, 800, 600, null);
	}// end clearScreen
	
	// Shows Game Status information at game end. 
	public void endGameScreen(){		
		try {
			g = bf.getDrawGraphics();
			clearScreen();
			
			g.setFont(new Font("Helvetica",Font.BOLD,40));
			if(monstersKilled >= 50){
				//g.drawImage(new ImageIcon(getClass().getResource("images/win.gif")).getImage(), 0, 0, 800, 600, null);
				g.drawImage(new ImageIcon("images/win.gif").getImage(), 0, 0, 800, 600, null);
			}else if( monsterPassed >= 1){
				//g.drawImage(new ImageIcon(getClass().getResource("images/lose.gif")).getImage(), 0, 0, 800, 600, null);
				g.drawImage(new ImageIcon("images/lose.gif").getImage(), 0, 0, 800, 600, null);
			}
			g.setFont(new Font("Helvetica",Font.PLAIN,20));
			g.setColor(Color.black);
			g.drawString("Monsters Killed: " + monstersKilled, 550, 100);
			g.drawString("Monsters Passed: " + monsterPassed, 550, 150);
			g.drawString("SuperTank Health: " + mySuperTank.getTankHealth(), 550, 200);
			
		}catch(Exception e){ e.printStackTrace(); }
		finally { g.dispose(); }
		bf.show();
		
		// Sleep a bit before asking user to try again or exit.
		try { Thread.sleep(2000); } 
		catch (Exception e) {e.printStackTrace();	}
		
	  switch(JOptionPane.showConfirmDialog(null, "Try again?", "Game Over", JOptionPane.YES_NO_OPTION)){
	    	case 0:
	    		restartGame();
	    		break;
	    	default:
	    		System.exit(0);
	    }
	} // end endGameScreen()
	
	public void restartGame(){
		gameObjects.clear();
		gameObjects.add(mySuperTank = new Tank());
		monsterPassed = 0;
		monstersKilled= 0;
		done = false;
		clearScreen();
		// requestFocus to allow the JFrame to listen to 
		// key releases again after game restart.
		requestFocus(); 
		gameLoop();
	}// end restartGame()
	
	// Start Game
	public static void main(String[] args) {
		new SuperTankGame();
	}// end main()
}
