package com.kevinli.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kevinli.abstract_classes.BlockObject;
import com.kevinli.ids.BlockID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.manager.Texture;

public class Coin extends BlockObject{

	private Texture tex;
	private BufferedImage coinImage;
	private final int SIZE = 16;
	
	public Coin(float x, float y, BlockID id) 
	{
		super(x, y, id);
		tex = MainGameLoop.getTextureInstance();
		coinImage = tex.staticImages.get("currency");
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(coinImage,(int)x,(int)y,SIZE,SIZE,null);
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,SIZE,SIZE);
	}

}
