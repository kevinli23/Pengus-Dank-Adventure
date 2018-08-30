package com.kevinli.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.kevinli.abstract_classes.BlockObject;
import com.kevinli.ids.BlockID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.main.MainGameLoop.STATE;
import com.kevinli.manager.Handler;
import com.kevinli.states.Menu;

public class Snowflake extends BlockObject{

	private float velx = 0;
	private float vely = 0;
	private float accelx = 0;
	private float accely = 0;
	private int radius = 4;
	Handler handler;
	double angle = 0;
	int count = 0;
	float xOffset = 0;
	int windDirection = 1;
	
	public Snowflake(int x, int y, BlockID id,Handler handler) {
		super(x, y, id);
		
		if(!MainGameLoop.gameState.equals(STATE.Game))
		{			
			this.x = MainGameLoop.getRandomNumber(MainGameLoop.WIDTH);
		}else
		{
			try
			{
				this.x = MainGameLoop.getRandomNumber(MainGameLoop.mapSizeX*64);
			}catch(IllegalArgumentException e)
			{
				
			}
		}
		this.y = MainGameLoop.getRandomNumber(90)-150;
		
		this.radius = MainGameLoop.getRandomNumber(17)+4;
		vely = 0.375f + (radius/16)*0.5f;
		this.handler = handler;
		
		if(MainGameLoop.getRandomNumber(2) == 0)
		{
			windDirection = -1;
		}
	}
	
	public void collision()
	{
		if(x>MainGameLoop.mapSizeX*64 || y>MainGameLoop.mapSizeY*64)
		{
			handler.removeSnowflake(this);
		}
	}
	

	@Override
	public void tick() 
	{
		if(MainGameLoop.gameState.equals(STATE.Game) && !handler.snowflakes.isEmpty()) collision();
		if(count>=100) 
		{
			if(angle<360)
			{
				angle++;
			}else
			{
				angle = 0;
			}
			count = 0;
		}else
		{
			count++;
		}
		velx = (float)(Math.sin(angle)*(0.5+(radius/16)*0.5)*windDirection);
		accely = (float)(Menu.GRAVITY + (0.0005*radius));
		
		velx+=accelx;
		vely+=accely;
		
		x += velx;
		y += vely;
		
		accely = 0;
		
		if(y>MainGameLoop.HEIGHT && MainGameLoop.gameState.equals(STATE.Menu))
		{
			handler.removeSnowflake(this);
		}else if(MainGameLoop.gameState.equals(STATE.Game) && y>MainGameLoop.mapSizeY*64)
		{
			handler.removeSnowflake(this);
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillOval((int)x, (int)y, radius,radius);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,radius,radius);
	}

}
