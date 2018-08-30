package com.kevinli.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.kevinli.ids.BlockID;
import com.kevinli.ids.PlayerID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.main.MainGameLoop.STATE;
import com.kevinli.manager.Handler;
import com.kevinli.manager.Texture;
import com.kevinli.objects.Player;
import com.kevinli.objects.Snowflake;

/*
 * Main Purpose:
 * -------------
 * Handles the rendering and ticking of objects and backgrounds while on the menu
 */

public class Menu extends MouseAdapter
{
	//VARIABLE DECLARATION
	
	//PRIVATE VARIABLES
	private Handler handler;
	private Texture tex;
	private final Color DARK_BLUE = new Color(0,98,128);
	private Color buttonColor[]  = {Color.white, Color.white, Color.white, Color.white};
	private Color helpButtonColor[] = {Color.black};
	
	//END OF VARIABLE DECLARATION
	
	private String helpIntro[] = 
	{
			"Welcome to Pengu's Dank Adventure, a game",
			"where you play as Pengu, a vengeance seeking",
			"penguin whose family was captured by walri.",
			"",
			"Embark on a journey as Pengu to rescue",
			"his family before it's too late.",
			"",
			"CONTROLS-------------------------------------",
			"",
			"--Movement--",
			"Use W to jump",
			"Use A to move left",
			"Use D to move right",
			"",
			"--Skills--",
			"Use E to eat a fish and gain 15 hp",
			"Use S to pick up and create snowballs",
			"Hold Space to charge your snowball",
			"Release Space to throw the snowball",
			"Press Q to cancel throwing the snowball",
			"",
			"--Tips--",
			"On snow, you pick up snowballs",
			"On ice, you pick up iceballs",
			"Snow balls take 2 seconds to pick up",
			"but they do 1 damage",
			"Ice balls take 5 seconds to pick up",
			"but they do 3 damage",
			"You can only hold 5 snowballs at once",
			"You can also only hold 2 fish at once",
			"Enemies drop penguin currency",
			"their usage is a mystery for you to discover",
			"You jump when you fall ontop of an enemy",
			"",
			"--UI Commands--",
			"Press M to mute/unmute the music",
			"",
			"Good Luck!"
	};
	
	public static final float GRAVITY = 0.001f;
	
	int count = 0;
	int maxHelpPageHeight = 0;
	int relativeHelpY = 0;

	//END OF VARIABLE DECLARATION
	
	public Menu(Handler handler)
	{
		this.handler = handler;
		this.tex = MainGameLoop.getTextureInstance();
	}
	public void tick()
	{
		if(count >=10)
		{
			handler.addSnowflake(new Snowflake(100,100,BlockID.Snowflake,handler));
			count = 0;
		}else
		{
			count++;
		}
	}
	
	public void render(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		if(MainGameLoop.gameState.equals(STATE.Menu)) {drawMenu(g2d);}
		else if(MainGameLoop.gameState.equals(STATE.Help)) {drawHelp(g2d);}
	}
	
	private void drawHelp(Graphics2D g2d)
	{
		g2d.setColor(new Color(165,242,243));
		g2d.fillRect(0, 0, MainGameLoop.WIDTH, MainGameLoop.HEIGHT);
		
		g2d.setColor(Color.black);
		g2d.setFont(MainGameLoop.gameFont_40);
		g2d.drawString("Help",MainGameLoop.WIDTH/2 - 70,100-relativeHelpY);
		
		g2d.setColor(helpButtonColor[0]);
		g2d.drawString("<",50,100-relativeHelpY);
		
		g2d.setColor(Color.black);
		g2d.setFont(MainGameLoop.gameFont_20);
		
		for(int i = 0;i<helpIntro.length;i++)
		{
			g2d.setColor(Color.black);
			if(i == 7)
			{
				g2d.setColor(Color.red);
			}else if(i == 9 || i == 14 || i == 21 || i == 34)
			{
				g2d.setColor(new Color(240,128,128));
			}
			g2d.drawString(helpIntro[i],50,190 + 40*i - relativeHelpY);
		}
	}
	
	private void drawMenu(Graphics2D g2d)
	{
		g2d.setColor(DARK_BLUE);
		g2d.fillRect(0, 0, MainGameLoop.WIDTH, MainGameLoop.HEIGHT);
		
		g2d.setColor(Color.white);
		if(!MainGameLoop.fullscreen)
		{
			g2d.setFont(MainGameLoop.gameFont_40);	
		}else
		{
			g2d.setFont(MainGameLoop.gameFont_60);
		}
		g2d.drawString("Pengu's Dank Adventure", MainGameLoop.WIDTH/2 - g2d.getFontMetrics().stringWidth("Pengu's Dank Adventure")/2, (int)(MainGameLoop.HEIGHT*(1.0/8)));
		
		int displace = 0;
		if(!MainGameLoop.fullscreen)
		{
			g2d.setFont(MainGameLoop.gameFont_30);
			displace = 70;
		}else
		{
			g2d.setFont(MainGameLoop.gameFont_40);
			displace = 90;
		}
		
		g2d.setColor(buttonColor[0]);
		g2d.drawString("Play", MainGameLoop.WIDTH/2 - displace, (int)(MainGameLoop.HEIGHT*(2.3/8)));
		
//		g2d.drawRect(330, (int)(MainGameLoop.HEIGHT*(2.05/8)), 105, 30);
		
		g2d.setColor(buttonColor[1]);
		g2d.drawString("Help", MainGameLoop.WIDTH/2 - displace, (int)(MainGameLoop.HEIGHT*(2.8/8)));
		
//		g2d.drawRect(330, (int)(MainGameLoop.HEIGHT*(2.55/8)), 95, 30);
		
		g2d.setColor(buttonColor[2]);
		g2d.drawString("Options", MainGameLoop.WIDTH/2 - displace, (int)(MainGameLoop.HEIGHT*(3.3/8)));
		
//		g2d.drawRect(330,(int)(MainGameLoop.HEIGHT*(3.05/8)), 185, 32);
		
		g2d.setColor(buttonColor[3]);
		g2d.drawString("Quit", MainGameLoop.WIDTH/2 - displace, (int)(MainGameLoop.HEIGHT*(3.8/8)));
		
//		g2d.drawRect(330,(int)(MainGameLoop.HEIGHT*(3.55/8)), 100, 32);
		
		for(int i = 0;i<MainGameLoop.WIDTH;i+=32)
		{
			for(int y = MainGameLoop.HEIGHT;y>MainGameLoop.HEIGHT- (32*5);y-=32)
			{
				g2d.drawImage(tex.blocks.get("ice"),i,y,null);
			}
		}
		
		g2d.drawImage(tex.buildings.get("igloo_left"), MainGameLoop.WIDTH - 256, MainGameLoop.HEIGHT- (32*8), null);
		
		g2d.setColor(Color.orange);
		g2d.drawString(MainGameLoop.client.getName(),MainGameLoop.WIDTH-270,MainGameLoop.HEIGHT-20);
		
	}
	
	public void mousePressed(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		
		if(MainGameLoop.gameState.equals(STATE.Menu))
		{
			if(MainGameLoop.mouseOver(mx, my, MainGameLoop.WIDTH/2 - 70, (int)(MainGameLoop.HEIGHT*(2.05/8)), 105, 30) && MainGameLoop.gameState.equals(STATE.Menu))
			{
				handler.removePlayer();
				MainGameLoop.p = null;
				MainGameLoop.loadCutScene(Game.text, Game.locations);
				MainGameLoop.gameState = MainGameLoop.STATE.Game;
			}
			
			if(MainGameLoop.mouseOver(mx, my, 330, (int)(MainGameLoop.HEIGHT*(2.55/8)), 95, 30))
			{
				handler.removePlayer();
				maxHelpPageHeight = 950;
				buttonColor[1] = Color.white;
				MainGameLoop.gameState = MainGameLoop.STATE.Help;
			}
			
			if(MainGameLoop.mouseOver(mx, my, MainGameLoop.WIDTH/2 - 70, (int)(MainGameLoop.HEIGHT*(3.55/8)), 100, 32))
			{
				System.exit(-1);
			}
		}
		
		if(MainGameLoop.gameState.equals(STATE.Help))
		{
			if(MainGameLoop.mouseOver(mx,my,50,(int)(MainGameLoop.HEIGHT*(0.6/8)), 20,35))
			{
				MainGameLoop.p = new Player(100,(int)(MainGameLoop.HEIGHT*(6.1/8)),PlayerID.Penguin,handler);
				handler.addPlayer(MainGameLoop.p);
				MainGameLoop.gameState = STATE.Menu;
			}
		}
		
	}
	
	public void mouseReleased(MouseEvent e){}
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		int rotation = e.getWheelRotation();
		
		if(MainGameLoop.gameState.equals(STATE.Help))
		{
			if(relativeHelpY+rotation>=0 && relativeHelpY+rotation<maxHelpPageHeight){relativeHelpY+=rotation*50;}
		}
	}
	
	public void mouseMoved(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		
		if(MainGameLoop.gameState.equals(STATE.Menu))
		{
			if(MainGameLoop.mouseOver(mx, my, MainGameLoop.WIDTH/2 - 70, (int)(MainGameLoop.HEIGHT*(2.05/8)), 105, 30))
			{
				buttonColor[0] = Color.orange;
			}else {
				buttonColor[0] = Color.white;
			}
			
			if(MainGameLoop.mouseOver(mx, my, MainGameLoop.WIDTH/2 - 70, (int)(MainGameLoop.HEIGHT*(2.55/8)), 95, 30))
			{
				buttonColor[1] = Color.orange;
			}else {
				buttonColor[1] = Color.white;
			}
			
			if(MainGameLoop.mouseOver(mx, my, MainGameLoop.WIDTH/2 - 70, (int)(MainGameLoop.HEIGHT*(3.05/8)), 185, 32))
			{
				buttonColor[2] = Color.orange;
			}else {
				buttonColor[2] = Color.white;
			}
			
			if(MainGameLoop.mouseOver(mx, my, MainGameLoop.WIDTH/2 - 70, (int)(MainGameLoop.HEIGHT*(3.55/8)), 100, 32))
			{
				buttonColor[3] = Color.orange;
			}else {
				buttonColor[3] = Color.white;
			}
		}
		
		if(MainGameLoop.gameState.equals(STATE.Help))
		{
			if(MainGameLoop.mouseOver(mx,my,50, 70, 20,35))
			{
				helpButtonColor[0] = Color.orange;
			}else
			{
				helpButtonColor[0] = Color.black;
			}
		}
	}
}
