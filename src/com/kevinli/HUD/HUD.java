package com.kevinli.HUD;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.kevinli.main.MainGameLoop;
import com.kevinli.manager.Texture;
import com.kevinli.objects.Player;

/*
 * Main Purpose:
 * -------------
 * Heads Up Display for the game, which holds the settings, health, mana, exp, etc.
 */

public class HUD 
{
	//VARIABLE DECLARATION
	//Player Object
	Player p;
	//Image holding a picture of a penguin
	BufferedImage mugshot = null;
	//texture object
	Texture tex;
	//END OF VARIABLE DECLARATION
	
	//Constructor
	public HUD()
	{
		//initialize the objects in the fields
		p = MainGameLoop.p;
		tex = MainGameLoop.getTextureInstance();
		mugshot = tex.staticImages.get("mugshot");
	}
	
	public void tick()
	{
		
	}
	//draws the components of the HUD
	public void render(Graphics g)
	{
		try {
			Graphics2D g2d = (Graphics2D) g;
			g.drawImage(mugshot,5,45,83,70,null);
			
			//draw the player's health
			g.setColor(Color.red);
			g.fillRect(93, 45, (int)((p.health*1.0/100.0)*100), 24);
			//box surrounding health bar
			g.setColor(Color.black);
			g.drawRect(93, 45, 100, 24);
			
			//draw the outline for the snowballs
			for(int i = 0;i<5;i++)
			{
				g.drawOval(95+(i*20), 75, 15,15);
			}
			
			//draw the string for the players health
			g.setColor(Color.white);
			g.setFont(new Font("Arial",1,15));
			g.drawString(""+(int)p.health+"/100", 116,62);
			
			//draw the snowballs
			for(int i = 0;i<p.snowballs;i++)
			{
				if(MainGameLoop.p.snowballType[i] == 0) {g.setColor(Color.white);}
				else if(MainGameLoop.p.snowballType[i] == 1) {g.setColor(new Color(57,159,249));}
				
				g.fillOval(95+(i*20), 75, 15,15);
			}
			
			//draw the fishes
			for(int i = 0;i<p.fish;i++)
			{
				g.setColor(Color.orange);
				g2d.fillPolygon(new int[] {105 + i*50,95 + i*50,95+i*50,105+i*50},new int[] {102,97,112,107},4);
				g.fillOval(103+i*50, 97, 37, 15);
			}
			
			//outline for the fishes
			g.setColor(Color.black);
			for(int i = 0;i<2;i++)
			{
				g.setColor(Color.black);
				g2d.drawPolygon(new int[] {105 + i*50,95 + i*50,95+i*50,105+i*50},new int[] {102,97,112,107},4);
				g.drawOval(103+i*50, 97, 37, 15);	
			}
			
			//draw the loading bar
			if(p.getQueue()>0)
			{
				
				g.setColor(Color.black);
				g.drawRect(5, 117, 83, 10);
				
				g.setColor(Color.green);
				double value = 0;
				//duration and value depends on what type of snowball is being picked up
				if(MainGameLoop.p.getCommand().equals("ice"))
				{
					value = (int)((p.getQueue()*1.0/(Player.ICEBALL_TIMER*Player.pickup_multiplier))*83.0);
				}else if(MainGameLoop.p.getCommand().equals("snowball"))
				{
					value = (int)((p.getQueue()*1.0/(Player.SNOWBALL_TIMER*Player.pickup_multiplier))*83.0);
				}else if(MainGameLoop.p.getCommand().equals("eat"))
				{
					value = (int)((p.getQueue()*1.0/(Player.EAT_TIMER*Player.pickup_multiplier))*83.0);
				}
				//draw the timer bar
				if(value>=0)
				{
					g.fillRect(5, 117, (int)value, 10);
				}
			}
			
			//draw the currency icon
			g.drawImage(tex.staticImages.get("currency"), MainGameLoop.WIDTH-47,50,32,32,null);
			//Draw the amount of currenncy (in text)
			g.setColor(Color.white);
			g.drawString("x "+MainGameLoop.p.getCurrency(),MainGameLoop.WIDTH-75,72);
			
			//draw the music speakers to the bottom right
			g.drawImage(tex.staticImages.get("speakers_on"),MainGameLoop.WIDTH-75,MainGameLoop.HEIGHT-90,null);
			//draw line through it if music is muted
			if(!MainGameLoop.music)
			{
				g.setColor(Color.black);
				g2d.setStroke(new BasicStroke(5));
				g2d.drawLine(MainGameLoop.WIDTH-70, MainGameLoop.HEIGHT-48, MainGameLoop.WIDTH-15, MainGameLoop.HEIGHT-83);
			}
			
		}catch(NullPointerException e)
		{
			
		}
		

	}
	
	//Setter Method
	public void setP(Player p)
	{
		this.p = p;
	}
}
