package com.kevinli.objects;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kevinli.abstract_classes.BlockObject;
import com.kevinli.ids.BlockID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.manager.Texture;

/*
 * Main Purpose:
 * -------------
 * Object to handle all blocks
 */

public class Block extends BlockObject
{
	private BufferedImage image;
	private Texture tex;
	
	public Block(int x, int y, BlockID id)
	{
		super(x, y, id);
		tex = MainGameLoop.getTextureInstance();
		
		if(id.equals(BlockID.Ice))
		{
			image = tex.blocks.get("ice");
		}
		else if(id.equals(BlockID.SnowDirt))
		{
			image = tex.blocks.get("snow_dirt");
		}else if(id.equals(BlockID.Dirt))
		{
			image = tex.blocks.get("dirt");
		}else if(id.equals(BlockID.Water))
		{
			image = tex.blocks.get("water");
		}
	}

	@Override
	public void tick() 
	{
		
	}

	@Override
	public void render(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D)g;
		if(id.equals(BlockID.Water))
		{
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f));
		}
		g.drawImage(image,(int)x,(int)y,64,64,null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
	}

	@Override
	public Rectangle getBounds() 
	{
		return new Rectangle((int)x,(int)y,64,64);
	}

}
