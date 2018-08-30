package com.kevinli.manager;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * Main Purpose:
 * -------------
 * Handles the input and loading of images
 */

public class BufferedImageLoader 
{
	//Image object to hold our image
	private BufferedImage image;
	
	//Method called load image that takes in the path of the image
	public BufferedImage loadImage(String path)
	{
		try 
		{
			//Set the global image to the image from the path
			image= ImageIO.read(getClass().getResource(path));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		//Return the image that we just got
		return image;
	}
}
