package com.kevinli.abstract_classes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kevinli.ids.BlockID;
import com.kevinli.ids.EnemyID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.manager.Animation;
import com.kevinli.manager.Handler;
import com.kevinli.manager.Texture;

/*
 * Main Purpose:
 * -------------
 * Layout/Structure for all enemy type objects in the game
 */

public abstract class EnemyObject 
{
	//Variable Declaration
	
	//Position
	protected float x,y;
	protected final float GRAVITY = 0.3f;
	protected float velx,vely;
	protected boolean falling;
	protected int health;
	//ID to identify different types of enemies
	protected EnemyID id;
	protected Handler handler;
	protected Texture tex;
	protected int width,height;
	protected Animation a;
	protected String direction = "left";
	
	//End of Variable Declaration
	
	//Constructor
	public EnemyObject(float x, float y, EnemyID id, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.id = id;
		this.handler = handler;
		tex = MainGameLoop.getTextureInstance();
	}
	
	//Abstract Methods
	public abstract void tick();
	public abstract void render(Graphics g);
	//End of Abstract Methods
	
	//Getters and Setters
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public EnemyID getId() {
		return id;
	}
	public void setId(EnemyID id) {
		this.id = id;
	}
	public int getHealth()
	{
		return health;
	}
	public void setHealth(int health)
	{
		this.health = health;
	}
	public void reduceHealth(int damage)
	{
		health-=damage;
	}
	public Texture getTexture()
	{
		return this.tex;
	}
	public int getWidth()
	{
		return this.width;
	}
	public int getHeight()
	{
		return this.height;
	}
	//End of Getters and Setters
	
	//Other Public Methods
	public void collision()
	{
		
		boolean touched = false;
		for(int i = 0;i<handler.blocks.size();i++)
		{
			BlockObject tempObject = handler.blocks.get(i);
			if(tempObject.getId().equals(BlockID.Ice) || tempObject.getId().equals(BlockID.SnowDirt)|| tempObject.getId().equals(BlockID.Wall))
			{
				if (getTopBounds().intersects(tempObject.getBounds()))
				{
					y = tempObject.getY() + tempObject.getHeight();
					vely = 0;
				}
				if (getBottomBounds().intersects(tempObject.getBounds()))
				{
					y = tempObject.getY() - height;
					if (vely >20){
						//the bird fking dies ok?
					}
					vely = 0;
					touched = true;
				}
				if (getRightBounds().intersects(tempObject.getBounds()))
				{
					x = tempObject.getX() - width;
					velx*=-1;
					direction = "left";

				}
				if (getLeftBounds().intersects(tempObject.getBounds()))
				{
					x = tempObject.getX() + tempObject.getWidth();
					velx*=-1;
					direction = "right";
				}	
			}
			if(tempObject.getId().equals(BlockID.Invisible))
			{
				if(getRightBounds().intersects(tempObject.getBounds()) || getLeftBounds().intersects(tempObject.getBounds()))
				{
					velx*=-1;
				}
			}
		}
		falling = !touched;
	}
	
	public Rectangle getBounds() 
	{
		return new Rectangle((int)x,(int)y,width,height);
	}
	
	public Rectangle getRightBounds()
	{
		return new Rectangle((int) ((int)x+width-6),(int)y+10,(int)5,height-20);
	}
	
	public Rectangle getTopBounds() 
	{
		return new Rectangle((int)x+8,(int)y,(int)width-16,(int)10);
	}
	
	public Rectangle getLeftBounds()
	{
		return new Rectangle((int)x,(int)y+10,(int)5,(int)height-20);
	}
	
	public Rectangle getBottomBounds()
	{
		return new Rectangle((int)x+8,(int) ((int)y+(height-10)),width-16,(int)10);
	}
	
}
