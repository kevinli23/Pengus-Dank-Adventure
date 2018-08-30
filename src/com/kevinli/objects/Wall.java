package com.kevinli.objects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.kevinli.abstract_classes.BlockObject;
import com.kevinli.ids.BlockID;
import com.kevinli.manager.Handler;

public class Wall extends BlockObject{

	int type;
	float life;
	float maxlife;
	int hp;
	Color color;
	private Handler handler;
	
	public Wall(float x, float y, BlockID id,int width,int height,int type,Handler handler) {
		super(x, y, id);
		this.width = width;
		this.height = height;
		this.type = type;
		this.handler = handler;

		if(type == 0)
		{
			color = Color.white;
			maxlife = 5;
			life = 5;
		}
		else if(type == 1)
		{
			color = Color.blue;
			maxlife = 10;
			life = 10;
		}
	}

	@Override
	public void tick() 
	{
		life-=(1.0/60.0);
		if(life<=0)
		{
			handler.removeBlock(this);
		}
	}

	@Override
	public void render(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,life/maxlife));
		g.setColor(color);
		g.fillRect((int)x, (int)y, width, height);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,width,height);
	}

}
