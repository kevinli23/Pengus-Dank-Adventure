package com.kevinli.objects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kevinli.abstract_classes.BlockObject;
import com.kevinli.abstract_classes.EnemyObject;
import com.kevinli.abstract_classes.PlayerObject;
import com.kevinli.ids.BlockID;
import com.kevinli.ids.PlayerID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.main.MainGameLoop.STATE;
import com.kevinli.manager.Animation;
import com.kevinli.manager.Handler;
import com.kevinli.manager.KeyInputHandler;
import com.kevinli.manager.Texture;
import com.kevinli.states.Menu;

/*
 * Main Purpose:
 * -------------
 * Object to handle all players
 */

public class Player extends PlayerObject
{
	//VARIABLE DECLARATION
	
	public static final int SNOWBALL_TIMER = 30;
	public static final int ICEBALL_TIMER = 60;
	public static final int EAT_TIMER = 120;
	public static double pickup_multiplier = 1.0;
	
	public int health = 100;
	public int snowballs = 5;
	public int fish = 2;
	public int currency = 0;
	
	private Texture tex;
	private Handler handler;
	private Animation walk,eat,up;
	
	public String direction = "left";
	public double throwing_angle = 0;
	private boolean inWater = false;
	private boolean throwing = false;
	private boolean building = false;
	private boolean build_horizontal = true;
	public int snowballType[] = {0,0,0,0,0};
	
	private int queue = 0;
	private String command = "null";
	
	private static final float GRAVITY = 0.3f;
	private final int WIDTH = 64, HEIGHT = 64;
	private BlockID bottomBlock = null;
	public static int playerSpeed = 4;
	
	//END OF VARIABLE DECLARATION
	
	public Player(float x, float y, PlayerID id,Handler handler) 
	{
		super(x, y, id);
		tex = MainGameLoop.getTextureInstance();
		this.handler = handler;
		walk = new Animation(10,new BufferedImage[] {tex.characters.get("right_walk_penguin_1"), tex.characters.get("right_penguin"),tex.characters.get("right_walk_penguin_2")});
		eat = new Animation(15,new BufferedImage[] {tex.characters.get("eat_1"),tex.characters.get("eat_2")});
		up = new Animation(5,new BufferedImage[] {tex.characters.get("up_penguin_right"),tex.characters.get("right_penguin")});
		bufferAnimations();	
	}
	
	@Override
	public void tick() 
	{
		checkQueueStatus();
		if(MainGameLoop.gameState == STATE.Menu){menuUpdates();}
		else{gameUpdates();}
		bufferAnimations();
	}

	@Override
	public void render(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
		
		if(!inWater)
		{
			if(queue!=0)
			{
				if(command.equals("snowball") || command.equals("ice"))
				{
					if(direction.equals("right"))
					{
						g.drawImage(tex.characters.get("down_penguin_right"), (int)x,(int)y,null);
					}else
					{
						g.drawImage(tex.characters.get("down_penguin_left"), (int)x,(int)y,null);
					}
				}
				else if(command.equals("eat"))
				{
					eat.drawAnimation(g, (int)x, (int)y);
				}
			}
			else if(jumping && vely!=0)
			{
				if(direction.equals("right"))
				{
					up.drawAnimation(g2d, (int)x, (int)y);
				}
				else
				{
					up.drawFlippedAnimation(g2d, (int)x, (int)y);
				}
			}
			else if(velx!=0)
			{
				if(direction.equals("right"))
				{
					walk.drawAnimation(g, (int)x, (int)y);
				}else if(direction.equals("left"))
				{
					walk.drawFlippedAnimation(g, (int)x, (int)y);
				}
			}else 
			{
				if(direction.equals("right"))
				{
					g.drawImage(tex.characters.get("right_penguin"),(int)x,(int)y,null);
				}
				else if(direction.equals("left"))
				{
					g.drawImage(tex.characters.get("left_penguin"),(int)x,(int)y,null);
				}
				
				if(throwing) throwing(g);
		}
		}else
		{
			double angle = Math.toDegrees(Math.atan((-vely)/velx));
			if(angle>0 && Math.abs(angle)!=45)
			{
				angle-=90;
			}else if(angle<0&& Math.abs(angle)!=45)
			{
				angle+=90;
			}
//			MainGameLoop.rotateImage(x, y, tex.characters.get("swim"), (int) angle, g2d);
			if(angle>=-360 || angle<=360)
			{
				g2d.rotate(angle,x+32,y+32);
				g2d.drawImage(tex.characters.get("swim"), (int)x, (int)y, null);
				g2d.rotate(-angle, x+32, y+32);
			}else
			{
				g2d.drawImage(tex.characters.get("swim"), (int)x, (int)y, null);
			}
		}
		
	}

	//OTHER METHODS
	
	public void collision()
	{
		boolean touched = false;
		inWater = false;
		bottomBlock = BlockID.Empty;
		for(int i = 0;i<handler.blocks.size();i++)
		{
			BlockObject tempObject = handler.blocks.get(i);
			if(tempObject.getId().equals(BlockID.Ice) || tempObject.getId().equals(BlockID.SnowDirt) || tempObject.getId().equals(BlockID.Wall) || tempObject.getId().equals(BlockID.Dirt))
			{
				if (getTopBounds().intersects(tempObject.getBounds()))
				{
					y = tempObject.getY() + tempObject.getHeight();
					vely = 0;
				}
				if (getBottomBounds().intersects(tempObject.getBounds()))
				{
					y = tempObject.getY() - 63;
					if (vely >20){
						//the bird fking dies ok?
					}
					vely = 0;
					touched = true;
					bottomBlock = tempObject.getId();
				}
				if (getRightBounds().intersects(tempObject.getBounds()))
				{
					x = tempObject.getX() - 64;

				}
				if (getLeftBounds().intersects(tempObject.getBounds()))
				{
					x = tempObject.getX() + tempObject.getWidth();
				}	
			}
			if(tempObject.getId().equals(BlockID.Coin) && getBounds().intersects(tempObject.getBounds()))
			{
				handler.removeBlock(tempObject);
				currency++;
			}
			if(tempObject.getId().equals(BlockID.Water))
			{
				if(tempObject.getBounds().intersects(getBounds())) inWater = true;
			}
		}
		
		if(touched) {jumping = false;}
		falling = !touched;
		if(inWater)
		{
			playerSpeed = 2;
		}else
		{
			playerSpeed = 4;
		}
		for(int i = 0;i<handler.enemies.size();i++)
		{
			EnemyObject tempObject = handler.enemies.get(i);
			
			if (getTopBounds().intersects(tempObject.getBounds()))
			{
				y = tempObject.getY() + 64;
				vely = 0;
			}
			if (getBottomBounds().intersects(tempObject.getBounds()))
			{
				y = tempObject.getY() - 63;
				if (vely >20){
					//the bird fking dies ok?
				}
				vely*=-1.25;
				setJumping(true);
				tempObject.reduceHealth(1);
			}
			if (getRightBounds().intersects(tempObject.getBounds()))
			{
				x = tempObject.getX() - 64;
				health--;

			}
			if (getLeftBounds().intersects(tempObject.getBounds()))
			{
				x = tempObject.getX() + tempObject.getBounds().width;
				health--;
			}
		}
	}
	
	private void checkQueueStatus()
	{
		if(queue>1)
		{
			queue--;
			
			checkCommandWithBottomBlock("snowball",BlockID.SnowDirt);
			checkCommandWithBottomBlock("ice",BlockID.Ice);
			
			if(command.equals("eat")){eat.runAnimation();}
			
		}else if(queue == 1)
		{
			queue = 0;
			
			if(command.equals("snowball"))
			{
				snowballs++;
				snowballType[snowballs-1] = 0;
			}
			else if(command.equals("ice"))
			{
				snowballs++;
				snowballType[snowballs-1] = 1;
			}
			else if(command.equals("eat"))
			{
				if(health+15>100)
				{
					health = 100;
				}else {
					health+=15;
				}
				fish--;
			}
			command = "null";
		}
	}
	
	private void menuUpdates()
	{
		x += velx;

		if(x+velx < 0)
		{
			x = 0;
		}
		if(x+velx+64>MainGameLoop.WIDTH )
		{
			x = MainGameLoop.WIDTH-64;
		}
		
		if(x>=550)
		{
			x = 100;
			MainGameLoop.gameState = STATE.Chatroom;
		}
		
			
	}
	
	private void gameUpdates()
	{
		if(x+velx<0)
		{
			x = 0;
		}else if(x+WIDTH+velx>MainGameLoop.mapSizeX*64)
		{
			MainGameLoop.level++;
			handler.removeAllButPlayer();
			MainGameLoop.loadImageLevelFromFile();
			MainGameLoop.p.setX(MainGameLoop.startingPosition[0]);
			MainGameLoop.p.setY(MainGameLoop.startingPosition[1]);
		}else 
		{
			x+=velx;
		}
		y+=vely;	
		
		collision();
		
		if((falling || jumping) && !inWater)
		{
			vely+=GRAVITY;
		}
		
		if(jumping)
		{
			up.runAnimation();
		}
		
		if(vely>7)
		{
			vely = 7;
		}	
		
		if(inWater && !KeyInputHandler.keyDown[0])
		{
			vely = 1;
		}
	}
	
	public void adjustToFulLScreen()
	{
		x = MainGameLoop.WIDTH-230;
		y = MainGameLoop.HEIGHT-190;
	}
	
	private float trajectoryX(float velocity, double angle, int time)
	{
		return (float)(x+16 + velocity*Math.cos(angle)*time);
	}
	
	private float trajectoryY(float velocity, double angle, int time)
	{
		if(direction.equals("right"))
		{
			return (float)(y+25 - velocity*Math.sin(angle)*time + 0.3*(time)*(time));
		}else
		{
			return (float)(y+25 + velocity*Math.sin(angle)*time + 0.3*(time)*(time));
		}
	}

	public void throwing(Graphics g)
	{
		float x_s[] = new float[12];
		float y_s[] =  new float[12];
		
		for(int i = 0;i<12;i++)
		{
			float velocity = 0.0f;
			if(direction.equals("left"))
			{
				velocity = -15.0f;
			}else
			{
				velocity = 15.0f;
			}
			x_s[i] = trajectoryX(velocity,throwing_angle,i);
			y_s[i] = trajectoryY(velocity,throwing_angle,i);
		}
		
		g.setColor(Color.black);
		
		for(int i = 0;i<8;i++)
		{
			g.fillOval((int)(x_s[i]),(int)(y_s[i]), 10,10);
		}
	}
	
	private void building(Graphics g)
	{
		g.setColor(Color.white);
		int width = 0, height = 0;
		float bx = 0, by = 0;
		
		if(build_horizontal){width = 64;height = 10;}
		else {width = 10;height = 64;}
	}
	
	public void throwSnowball(double angle, double velocity)
	{
		if(snowballs>0)
		{
			Snowball sb = new Snowball(x+32,y+32,BlockID.Snowball,angle,velocity,handler,snowballType[0]);
			snowballs--;
			for(int i = 1;i<5;i++)
			{
				snowballType[i-1] = snowballType[i];
			}
			handler.snowflakes.add(sb);
		}
	}
	
	private void checkCommandWithBottomBlock(String com, BlockID id){if(command.equals(com) && !bottomBlock.equals(id)) {queue = 0;command = "null";}}
	
	private void bufferAnimations(){walk.runAnimation();eat.runAnimation();up.runAnimation();}
	
	//END OF OTHER METHODS ------------------------------------------------------------------------------------------------------------------
	
	//BEGINNING OF GETTERS
	
	public int getQueue() {return queue;}
	
	public int getHealth() {return health;}
	
	public int getCurrency() {return currency;}
	
	public boolean inWater() {return inWater;}
	
	public boolean isFalling() {return falling;}
	
	public boolean isJumping() {return jumping;}
	
	public boolean isThrowing(){return throwing;}
	
	public boolean isBuilding() {return building;}
	
	public boolean isBuildHorizontal() {return build_horizontal;}

	public String getCommand() {return command;}
	
	public String getDirection() {return direction;}
	
	public BlockID getBottomBlockID() {return bottomBlock;}
	
	@Override
	public Rectangle getBounds() {return new Rectangle((int)x,(int)y,WIDTH,HEIGHT);}
	
	public Rectangle getTopBounds() {return new Rectangle((int)x+8,(int)y,(int)47,(int)10);}
	
	public Rectangle getLeftBounds() {return new Rectangle((int)x,(int)y+10,(int)5,(int)44);}
	
	public Rectangle getBottomBounds(){return new Rectangle((int)x+8,(int) ((int)y+(HEIGHT-10)),47,(int)10);}
	
	public Rectangle getRightBounds() {return new Rectangle((int) ((int)x+WIDTH-6),(int)y+10,(int)5,(int)44);}
	
	//END OF GETTERS --------------------------------------------------------------------------------------------------------------------
	
	//BEGINNING OF SETTERS -----------------------------------------------------------------------------------------------------------------
	
	public void setJumping(boolean b) {this.jumping = b;}
		
	public void setThrowing(boolean b) {this.throwing = b;}
	
	public void setBuilding(boolean b) {this.building = b;}
	
	public void setBuildHorizontal(boolean b) {this.build_horizontal = b;}
	
	public void setHealth(int health) {this.health=health;}
	
	public void setQueue(int duration) {this.queue = duration;}
	
	public void setCurrency(int amount) {this.currency = amount;}
	
	public void setCommand(String command) {this.command = command;}
	
	public void setDirection(String direction) {this.direction = direction;}
	
	
	//END OF SETTERS -----------------------------------------------------------------------------------------------------------------
}
