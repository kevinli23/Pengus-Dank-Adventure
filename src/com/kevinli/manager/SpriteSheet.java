package com.kevinli.manager;

import java.awt.image.BufferedImage;

/*
 * Main Purpose:
 * -------------
 * Handles the cutting of buffered images
 */

public class SpriteSheet 
{
	//Global variable that stores the parent image
	private BufferedImage image;
	
	//Constructor
	public SpriteSheet(BufferedImage image){this.image=image;}
	
	//Method that grabs the image based on the coordinates
	public BufferedImage grabImage(int x,int y, int width,int height)
	{
		BufferedImage img = image.getSubimage(x,y, width, height);
		return img;
	}
	//Grab based on columns and rows
	//Used for sprite sheets with images of the same size
	//Example: tiles
	public BufferedImage grabLinearImage(int col,int row, int width,int height)
	{
		BufferedImage img = image.getSubimage((col*width)-width, (row*height)-height, width, height);
		return img;
	}
}
