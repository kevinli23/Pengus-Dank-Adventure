package com.kevinli.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.kevinli.abstract_classes.BlockObject;
import com.kevinli.abstract_classes.EnemyObject;
import com.kevinli.ids.BlockID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.manager.Handler;

public class Snowball extends BlockObject{
	
	private Handler handler;
	private final int RADIUS= 15;
	private float velx = 0;
	private float vely = 0;
	private double angle = 0;
	private double velocity = 0;
	private int damage = 0;
	private Color color = null;
	int type;

	public Snowball(float x, float y, BlockID id, double angle, double velocity,Handler handler, int type) 
	{
		super(x, y, id);
		this.angle = angle;
		this.velocity = velocity;
		this.handler = handler;
		this.type = type;
		velx = (float) (Math.cos(angle)*velocity);
		vely = (float) (Math.sin(Math.abs(angle))*velocity);
		
		if(velocity>0)
		{
			vely*=-1;
		}
		
		if(type == 0) 
		{
			damage = 1;
			color = Color.white;
		}
		else if(type == 1) 
		{
			damage = 3;
			color = new Color(57,159,249);
		}
	}

	@Override
	public void tick()
	{
		vely+=0.3f;
		
		x+=velx;
		y+=vely;
		
		collision();
	}
	
	public void collision()
	{

		for(int i = 0;i<handler.blocks.size();i++)
		{
			if(getBounds().intersects(handler.blocks.get(i).getBounds()) && !handler.blocks.get(i).getId().equals(BlockID.Invisible))
			{
				handler.removeSnowflake(this);
			}
		}
		
		for(int i = 0;i<handler.enemies.size();i++)
		{
			EnemyObject tempObject = handler.enemies.get(i);
			
			if(tempObject.getBounds().intersects(getBounds()))
			{
				handler.removeSnowflake(this);
				tempObject.reduceHealth(damage);
			}
		}
	}

	@Override
	public void render(Graphics g) 
	{
		g.setColor(color);
		g.fillOval((int)x, (int)y, RADIUS, RADIUS);
	}

	@Override
	public Rectangle getBounds() 
	{
		return new Rectangle((int)x,(int)y,RADIUS,RADIUS);
	}
	

}
