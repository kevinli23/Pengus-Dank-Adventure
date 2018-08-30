package com.kevinli.objects;

import java.awt.Color;
import java.awt.Graphics;

import com.kevinli.abstract_classes.EnemyObject;
import com.kevinli.ids.EnemyID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.manager.Handler;

public class Wolf extends EnemyObject{

	private final int WIDTH = 128;
	private final int HEIGHT = 64;
	
	public Wolf(float x, float y, EnemyID id, Handler handler) {
		super(x, y, id, handler);
		velx = -1;
		width = WIDTH;
		height = HEIGHT;
	}

	@Override
	public void tick() 
	{
//		int diff = Math.abs((int)(MainGameLoop.p.getX()-x));
//		int direction = (int)(velx/(Math.abs((int)velx)));
//		if(diff<=300)
//		{
//			velx=direction*3;
//		}else
//		{
//			velx = direction;
//		}
		int pX = (int)MainGameLoop.p.getX();
		int diff = (int)x - pX;
		int o_direction = (int)(velx/(Math.abs((int)velx)));
		int direction = 1;
		
		if(diff>0){direction = -1;}
		
		if(direction<0)
		{
			diff-=64;
		}else
		{
			diff-=WIDTH;
		}
		
		if(diff<=100)
		{
			velx = direction*2;
		}else
		{
			velx = o_direction;
		}
		
		x+=velx;
		y+=vely;
		
		if(falling)
		{
			vely+=GRAVITY;
		}
		
		collision();
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.green);
		g.fillRect((int)x, (int)y, WIDTH,HEIGHT);
		
	}

}
