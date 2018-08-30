package com.kevinli.manager;

import com.kevinli.abstract_classes.PlayerObject;
import com.kevinli.main.MainGameLoop;

public class Camera 
{
	//X and Y positions of the camera
	private float x,y;
	//Speed at which the camera follows you
	private float speed = 0.05f;
	
	//Constructor
	public Camera(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void tick(PlayerObject player)
	{
		try 
		{
			//Move the camera based on the player
			if(x + ((player.getX() - x)-MainGameLoop.WIDTH/2)*0.05f>=0 && x+((player.getX() - x)-MainGameLoop.WIDTH/2)*0.05f <= (MainGameLoop.mapSizeX*64)-MainGameLoop.WIDTH)
			{
				x+=((player.getX() - x)-MainGameLoop.WIDTH/2)*0.05f;
			}
			if(y+((player.getY() - y)-MainGameLoop.HEIGHT/2)*0.05>=0 && y+((player.getY() - y)-MainGameLoop.HEIGHT/2)*0.05<=((MainGameLoop.mapSizeY*64)-(MainGameLoop.HEIGHT)))
			{
				y+=((player.getY() - y)-MainGameLoop.HEIGHT/2)*0.05f;
			}
		}catch(NullPointerException e)
		{
			
		}
	}
	
	//GETTERS AND SETTERS
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
	//END OF GETTERS AND SETTERS
}
