package com.kevinli.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.kevinli.abstract_classes.BlockObject;
import com.kevinli.ids.BlockID;
import com.kevinli.manager.Handler;

public class Projectile extends BlockObject
{

	private int width;
	private int height;
	private int damage;
	private float velx;
	private float vely;
	private Handler handler;
	
	private Color color;
	
	public Projectile(float x, float y, float velx, float vely, Handler handler, BlockID id) {
		super(x, y, id);
		
		this.velx = velx;
		this.vely = vely;
		this.handler = handler;
		
		//Temporary
		color = Color.black;
		
		if(id.equals(BlockID.Projectile_BASIC))
		{
			damage = 1;
			width = 8;
			height = 8;
		}
		else if(id.equals(BlockID.Projectile_INTERMEDIATE))
		{
			damage = 2;
			width = 10;
			height = 10;
		}else
		{
			damage = 3;
			width = 12;
			height = 12;
		}

	}

	@Override
	public void tick() 
	{
		x+=velx;
		y+=vely;
	}

	@Override
	public void render(Graphics g) 
	{
		g.setColor(color);
		g.fillRect((int)x, (int)y, width, height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,width,height);
	}
	

	public int getDamage()
	{
		return this.damage;
	}

}
