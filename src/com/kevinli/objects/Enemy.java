package com.kevinli.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kevinli.abstract_classes.BlockObject;
import com.kevinli.abstract_classes.EnemyObject;
import com.kevinli.ids.BlockID;
import com.kevinli.ids.EnemyID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.manager.Animation;
import com.kevinli.manager.Handler;
import com.kevinli.manager.Texture;

/*
 * Main Purpose:
 * -------------
 * Object to handle all enemies
 */

public class Enemy extends EnemyObject
{
	private int MAX_HEALTH = 0;
	private int value = 0;
	
	public Enemy(float x, float y, EnemyID id, Handler handler) 
	{
		super(x, y, id,handler);
		if(id.equals(EnemyID.Basic))
		{
			width = tex.enemies.get("basic_enemy_still").getWidth();
			height = tex.enemies.get("basic_enemy_still").getHeight();
			a = new Animation(12,new BufferedImage[] {tex.enemies.get("basic_enemy_left"),tex.enemies.get("basic_enemy_right")});
			MAX_HEALTH = 10;
			health = 10;
			value = 1;
		}
		velx=-1;
		
	}

	@Override
	public void tick() 
	{
		x+=velx;
		y+=vely;
		
		if(falling)
		{
			vely+=GRAVITY;
		}
		collision();
		if(health<=0)
		{
			for(int i = 0;i<value;i++)
			{
				handler.addBlocks(new Coin(x+24,y+48,BlockID.Coin));
			}
			handler.removeEnemy(this);
		}
		a.runAnimation();
		
	}

	@Override
	public void render(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
		if(velx<0)
		{
			a.drawAnimation(g, (int)x, (int)y);
		}else if(velx>0)
		{
			a.drawFlippedAnimation(g, (int)x, (int)y);
		}
		
//		g.setColor(Color.red);
		g.fillRect((int)x, (int)y-15, (int)(width*(health*1.0/MAX_HEALTH)), 10);
//		g2d.draw(getTopBounds());
//		g2d.draw(getLeftBounds());
//		g2d.draw(getBottomBounds());
//		g2d.draw(getRightBounds());
	}

}
