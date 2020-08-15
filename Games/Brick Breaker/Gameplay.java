import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
	private boolean play = false;
	private int score = 0;

	private int totalBricks = 48; // change

	private Timer timer;

	private int delay = 8;
	private Timer powerup_timer = new Timer(delay - 5, null);

	private int playerX = 310;

	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;

	private int level = 1;

	private int lives = 3;

	private MapGenerator map;

	public int getPowerPos(int max) {
		Random random = new Random();
		return random.nextInt(max);
	}

	private int lifeX = getPowerPos(20);
	private int lifeY = getPowerPos(20);
	
	public void generatePowerUp() {
		for (int i = 0; i < 10; i++) {
			try {
				lifeX = getPowerPos(20);
				lifeY = getPowerPos(20);
				TimeUnit.SECONDS.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getLives() {
		return this.lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public Gameplay()
	{		
		map = new MapGenerator(4, 12); //change later to 4, 12
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
        timer=new Timer(delay,this);
		timer.start();
	}
	
	public void paint(Graphics g)
	{    		
		// background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		// drawing map
		map.draw((Graphics2D) g);
		
		// borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		// the scores 		
		g.setColor(Color.white);
		g.setFont(new Font("serif",Font.BOLD, 25));
		g.drawString(""+score, 590,30);
		
		//the lives
		g.setColor(Color.white);
		g.setFont(new Font("serif",Font.BOLD, 25));
		g.drawString(""+getLives(), 590,50);

		// the paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		// the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);

		//power-ups
		for (int i = 0; i < 15; i++) {
			g.setColor(Color.RED);
			g.fillRect(lifeX, lifeY, 20, 20);
			generatePowerUp();
		}

		// when you won the game
		if(totalBricks <= 0)
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			level += 1;
			lives = 3;
			g.setFont(new Font("serif",Font.BOLD, 30));
			g.drawString("You Won, Next Level!", 260,300);
			g.drawString("Lives: "+lives, 190, 410);

			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD, 20));           
			g.drawString("Press (Enter) to Restart", 230,350);  
		}
		
		// when you lose a live
		if(ballposY > 570 && play)
        {
			System.out.println("Total Bricks Ball Pos " + totalBricks);
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			ballposY = 210;
			lives--;
			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD, 30));
			g.drawString("Live Lost, Score: "+score, 190,300);
			g.drawString("Lives: "+lives, 190, 410);

			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD, 20));           
			g.drawString("Press (Enter) to Restart", 230,350);        
		}
		
		if (lives == 0) 
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD, 30));
			g.drawString("Game Over, Score: "+score, 190,300);

			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD, 20));           
			g.drawString("Press (Enter) to Restart", 230,350);        
		}
		
		g.dispose();
	}	

	public void keyPressed(KeyEvent e) 
	{
		int level = getLevel();
		int player_lives = getLives();
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{        
			if(playerX >= 600 && play)
			{
				playerX = 600;
			}
			else
			{
				moveRight();
			}
        }
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{          
			if(playerX < 10 && play)
			{
				playerX = 10;
			}
			else
			{
				moveLeft();
			}
        }		
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{        
	
			System.out.println("level: " + level);  
			System.out.println("player lives: " + player_lives);
			if (!play && level == 2) 
			{
				System.out.println();
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				totalBricks = 48; //change
				lives = 3;
                map = new MapGenerator(4, 13);//change 
				
				repaint();
			}

			//set up for live lost - works for all levels
			if(!play && player_lives > 0 && player_lives <= 3 && level >= 1 && level <= 10)
			{
				System.out.println("Lives Player: " + player_lives);
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
			}

			if(!play && player_lives ==0)
			{
				System.out.println(level);
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalBricks = 48; //change
				lives = 3;
                map = new MapGenerator(4, 12); //change
				
				repaint();
			}
			


        }		
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public void moveRight()
	{
		play = true;
		playerX+=20;	
	}
	
	public void moveLeft()
	{
		play = true;
		playerX-=20;	 	
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		timer.start();
		powerup_timer.start();

		int test = 1;
		if(play)
		{			
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8)))
			{
				ballYdir = -ballYdir;
				ballXdir = -2;
			} else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 70, 550, 30, 8)))
			{
				ballYdir = -ballYdir;
				ballXdir = ballXdir + 1;
			}
else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 30, 550, 40, 8)))
			{
				ballYdir = -ballYdir;
			} else if (test == 1) 
			{
				//System.out.println("power-up");
				lifeY -= 10;

				if (lifeY >= 570 || lifeY <= 0) 
				{
					lifeY = 500;
				}
			}
			
			// check map collision with the ball		
			for(int i = 0; i<map.map.length; i++)
			{
				for(int j =0; j<map.map[0].length; j++)
				{				
					if(map.map[i][j] == 1)
					{
						//scores++;
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);					
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect))
						{					
							map.setBrickValue(0, i, j);
							score+=5;	
							totalBricks--;
							
							// when ball hit right or left of brick
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)	
							{
								ballXdir = -ballXdir;
							}
							// when ball hits top or bottom of brick
							else
							{
								ballYdir = -ballYdir;				
							}
							
							break;
						}
					}
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			
			if(ballposX < 0)
			{
				ballXdir = -ballXdir;
			}
			if(ballposY < 0)
			{
				ballYdir = -ballYdir;
			}
			if(ballposX > 670)
			{
				ballXdir = -ballXdir;
			}		
			
			repaint();		
		}
	}
}