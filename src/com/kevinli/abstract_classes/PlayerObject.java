package com.kevinli.abstract_classes;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.kevinli.ids.EnemyID;
import com.kevinli.ids.PlayerID;

/*
 * Main Purpose:
 * -------------
 * Layout/Structure for all player type objects in the game
 */

public abstract class PlayerObject 
{
	//Variable Declaration
	
	//Position
	protected float x,y;
	protected float velx,vely;
	protected boolean falling,jumping;
	//ID to identify types of player
	protected PlayerID id;
	
	//End of Variable Declaration
	
	//Constructor
	public PlayerObject(float x,float y,PlayerID id)
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
	public float getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public float getVelx() {
		return velx;
	}
	
	public void setVelx(float velx) {
		this.velx = velx;
	}
	
	public float getVely() {
		return vely;
	}
	
	public void setVely(float vely) {
		this.vely = vely;
	}
	public PlayerID getId() {
		return id;
	}
	public void setId(PlayerID id) {
		this.id = id;
	}
	//End of Getters and Setters
}
