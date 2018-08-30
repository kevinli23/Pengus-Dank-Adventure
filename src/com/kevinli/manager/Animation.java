package com.kevinli.manager;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kevinli.main.MainGameLoop;

/*
 * Main Purpose:
 * -------------
 * Sets up the loop of images to create an animation
 */

public class Animation {
	
	//VARIABLE DECLARATION
	
	//Speed at which they flip through the images
	//each value of speed represents 1/60 s.
	private int speed;
	//How many images are there/ frames to loop through
	private int frames;
	
	//Variable that holds how many seconds have passed
	//since the last switch in image
	private int index = 0;
	//Variable that holds which image index the object is on
	private int count = 0;
	//Is the animation running
	private boolean isRunning = false;
	
	//Images for the animation
	private BufferedImage[] images;
	//Current image to be ticked or rendered
	private BufferedImage currentImg;
	
	//END OF VARIABLE DECLARATION
	
	//Constructor
	public Animation(int speed, BufferedImage image[]){
		
		this.speed = speed;
		//Add the frames to how many images there are
		frames = image.length; 
		//Set the maximum of the array
		images = new BufferedImage[frames];
		
		//Loop through and add the iamges
		for (int i =0;i<frames;i++)
		{
			images[i] = image[i];
		}
	}
	
	//Checks to see if the current animation is running
	public boolean isRunning() {return isRunning;}
	
	public void addFrame(){}
	
	//Starts the animation
	public void runAnimation()
	{
		//Add to the index every 1/60 of a second
		index++;
		//If the index counter is greater than the speed
		//then draw the next frame and reset index
		if(index>speed){
			index = 0;
			nextFrame();
		}
		
	}
	//Called when the next frame must be called
	public void nextFrame()
	{
		//If the count is less than the total frames, just add
		if(count < frames)
		{
		   currentImg = images[count];
		   count++;
		}
		//Otherwise reset the counter back to 0, the first image
		else 
		{
			count = 0;
		}
	}
	
	//Draw the current frame
	public void drawAnimation(Graphics g,int x,int y){g.drawImage(currentImg,x,y,null);}
	
	public void drawFlippedAnimation(Graphics g, int x, int y) {g.drawImage(MainGameLoop.flipImageHorizontally(currentImg),x,y,null);}
	
	//Draw the current frame to a given scale
	public void drawAnimation(Graphics g,int x,int y,int scaleX,int scaleY){g.drawImage(currentImg,x,y,scaleX,scaleY,null);}
	
}
