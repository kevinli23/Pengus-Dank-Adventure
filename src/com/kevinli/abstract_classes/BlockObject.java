package com.kevinli.abstract_classes;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.kevinli.ids.BlockID;

/*
 * Main Purpose:
 * -------------
 * Layout/Structure for all block type objects in the game
 */

public abstract class BlockObject 
{
	//Variable Declaration
	
	//Position
	protected float x,y;
	//ID to identify different types of blocks
	protected BlockID id;
	protected int width = 64,height = 64;
	
	//End of Variable Declaration
	
	//Constructor
	public BlockObject(float x,float y,BlockID id)
	{
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	//Abstract Methods
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	//End of Abstract Methods
	
	//Getters and Setters
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

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
	public BlockID getId() {
		return id;
	}
	public void setId(BlockID id) {
		this.id = id;
	}

	//End of Getter and Setters
}
