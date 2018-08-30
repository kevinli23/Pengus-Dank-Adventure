package com.kevinli.manager;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.kevinli.main.MainGameLoop;

/*
 * Main Purpose:
 * -------------
 * Handles the loading and distribution of images from their base images
 */

public class Texture 
{
	//Create an instance of sprite sheet and images for the spritesheet
	SpriteSheet snowflake_sheet, blocks_sheet,buildings_sheet,char_sheet,other_sheet,enemy_sheet;
	private BufferedImage snowflake_pic = null, block_pic, building_pic,char_pic,other_pic,enemy_pic;
	
	//Store images in either an array or hashmap
	public HashMap<String,BufferedImage> menuSprites = new HashMap<String,BufferedImage>();
	public HashMap<String,BufferedImage> blocks = new HashMap<String,BufferedImage>();
	public HashMap<String,BufferedImage> buildings = new HashMap<String,BufferedImage>();
	public HashMap<String,BufferedImage> characters = new HashMap<String,BufferedImage>();
	public HashMap<String,BufferedImage> staticImages = new HashMap<String,BufferedImage>();
	public HashMap<String,BufferedImage> enemies = new HashMap<String,BufferedImage>();
	
	
	public Texture()
	{
		//Used to load the images
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try
		{
			//Load the image
			snowflake_pic = loader.loadImage("/sprites/menu_sprites/snowflakes/snowflake.png");
			block_pic = loader.loadImage("/sprites/blocks/blocks.png");
			building_pic = loader.loadImage("/sprites/buildings/igloo.png");
			char_pic = loader.loadImage("/sprites/characters/penguin.png");
			other_pic = loader.loadImage("/sprites/static_images/other.png");
			enemy_pic = loader.loadImage("/sprites/characters/enemies.png");
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		//Create a sprite sheet out of the image
		snowflake_sheet = new SpriteSheet(snowflake_pic);
		blocks_sheet = new SpriteSheet(block_pic);
		buildings_sheet = new SpriteSheet(building_pic);
		char_sheet = new SpriteSheet(char_pic);
		other_sheet = new SpriteSheet(other_pic);
		enemy_sheet = new SpriteSheet(enemy_pic);
		getTextures();
	}
	public void getTextures()
	{
		//Get our sprites and images based on the sprite sheet
		
		menuSprites.put("snowflake", snowflake_sheet.grabImage(0, 0, 13, 14));
		
		blocks.put("ice", blocks_sheet.grabImage(0, 0, 32, 32));
		blocks.put("snow_dirt",blocks_sheet.grabImage(32, 0, 32,32));
		blocks.put("dirt", blocks_sheet.grabImage(32, 32, 32, 32));
		blocks.put("water_top", blocks_sheet.grabImage(0, 64, 64, 64));
		blocks.put("water", blocks_sheet.grabImage(64, 64, 64, 64));
		
		buildings.put("igloo_left", buildings_sheet.grabImage(0, 0, 256, 128));
		
		characters.put("right_penguin", char_sheet.grabImage(0, 0, 64, 64));
		characters.put("left_penguin", char_sheet.grabImage(64, 0, 64, 64));
		
		characters.put("up_penguin_right", char_sheet.grabImage(128, 0, 64, 64));
		
		characters.put("down_penguin_right", char_sheet.grabImage(0, 192, 64, 64));
		characters.put("down_penguin_left", MainGameLoop.flipImageHorizontally(char_sheet.grabImage(0, 192, 64, 64)));
		
		characters.put("right_walk_penguin_1", char_sheet.grabImage(0, 64, 64, 64));
		characters.put("right_walk_penguin_2", char_sheet.grabImage(64, 64, 64, 64));
		
		characters.put("eat_1", char_sheet.grabImage(128, 64, 64, 64));
		characters.put("eat_2", char_sheet.grabImage(192, 64, 64, 64));
		
		characters.put("swim", char_sheet.grabImage(128, 128, 64, 64));
		
		staticImages.put("family_full", other_sheet.grabImage(0, 0, 400,400));
		staticImages.put("mugshot", other_sheet.grabImage(400, 0, 21,16));
		staticImages.put("currency", other_sheet.grabImage(400, 16, 64, 64));
		staticImages.put("speakers_on", other_sheet.grabImage(400, 80, 64, 64));
		staticImages.put("mic", other_sheet.grabImage(400, 144, 32, 64));
		
		enemies.put("basic_enemy_still", enemy_sheet.grabImage(0, 0, 32, 64));
		enemies.put("basic_enemy_left", enemy_sheet.grabImage(32, 0, 32, 64));
		enemies.put("basic_enemy_right", enemy_sheet.grabImage(64, 0, 32, 64));
	}
}
