package com.kevinli.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.kevinli.HUD.HUD;
import com.kevinli.ids.BlockID;
import com.kevinli.ids.PlayerID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.main.MainGameLoop.STATE;
import com.kevinli.manager.Camera;
import com.kevinli.manager.Handler;
import com.kevinli.manager.Texture;
import com.kevinli.objects.Player;
import com.kevinli.objects.Snowflake;
import com.kevinli.objects.Wall;

/*
 * Main Purpose:
 * -------------
 * Handles the rendering and ticking of objects and backgrounds while in game
 */

public class Game extends MouseAdapter
{
	//VARIABLE DECLARATION
	
	//PRIVATE VARIABLES
	
	//Objects
	private HUD hud;
	private Camera cam;
	private Texture tex;
	private Handler handler;
	
	//Values for animations
	private int count = 0;
	private int scene = -1;
	private int max_scenes = 0;
	private float composite = 0.0f;
	private boolean cutscene = true;
	private int buildx = 0,buildy = 0;
	
	//ArrayLists for locations and text
	public static ArrayList<ArrayList<String>> text = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<Integer[]>> locations = new ArrayList<ArrayList<Integer[]>>();
	
	//END OF VARIABLE DECLARATION
	
	public Game(Handler handler)
	{
		this.handler = handler;
		init();
	}
	public void init()
	{
		cam = new Camera(0,0);
		hud = new HUD();
		tex = MainGameLoop.getTextureInstance();
	}
	public void tick()
	{
		if(count>12)
		{
			handler.addSnowflake(new Snowflake(100,100,BlockID.Snowflake,handler));
			count = 0;
		}else
		{
			count++;
		}
		if(MainGameLoop.gameState == STATE.Game)
		{
			if(cutscene)
			{
				if(composite<0.99f) composite+=0.01f;
				if(count<10)
				{
					count++;
				}else if(count>=10)
				{
					handler.addBlocks(new Snowflake(100,100,BlockID.Snowflake,handler));
					count = 0;
				}
			}
			handler.tick();
			
			if(!cutscene)
			{
				cam.tick(MainGameLoop.p);
			}
		}
	}
	public void render(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		if(cutscene && scene<max_scenes)
		{
			g.setColor(new Color(0,98,128));
			g.fillRect(0, 0, MainGameLoop.WIDTH,MainGameLoop.HEIGHT);
			
			drawCutScenes(g2d,MainGameLoop.level);
		}else
		{
			g.setColor(new Color(0,90,120));
			g.fillRect(0, 0, MainGameLoop.mapSizeX*64,MainGameLoop.mapSizeY*64);
			
			g2d.translate(-cam.getX(), -cam.getY());
			handler.render(g);
			g2d.translate(cam.getX(), cam.getY());
			hud.render(g);
			
//			if(MainGameLoop.p.isBuilding())
//			{
//				if(MainGameLoop.p.snowballType[0] == 0)
//				{
//					g.setColor(Color.white);
//				}else if(MainGameLoop.p.snowballType[0] == 1)
//				{
//					g.setColor(Color.blue);
//				}
//				if(MainGameLoop.p.isBuildHorizontal())
//				{
//					g.fillRect(buildx-32, buildy-5, 64, 10);
//				}else
//				{
//					g.fillRect(buildx-5, buildy-32, 10, 64);
//				}
//			}
		}
		
	}
	
	private void drawCutScenes(Graphics2D g, int level)
	{
		if(level == 1)
		{	
			g.setColor(Color.white);
//			drawLevelOneScenes(g,scene);	
			g.setFont(MainGameLoop.gameFont_30);
			
			int numOfStrings = text.get(scene).size();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,composite));			
			for(int i = 0;i<numOfStrings;i++)
			{
				String currString = text.get(scene).get(i);	
				int tempX = locations.get(scene).get(i)[0];
				int tempY = locations.get(scene).get(i)[1];
				
				g.drawString(currString, tempX, tempY);
				
			}
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));			
		}
	}

	public void setMaxScenes(int max)
	{
		this.max_scenes = max;
	}
	
	public void mouseMoved(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		
//		if(MainGameLoop.p != null)
//		{
//			if(MainGameLoop.p.isBuilding())
//			{
//				buildx = x;
//				buildy = y;
//			}
//		}
	}
	
	public void mousePressed(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		
		if(MainGameLoop.gameState.equals(STATE.Game))
		{
			if(scene<max_scenes)
			{
				composite = 0.0f;
				scene++;
				
				if(scene == max_scenes)
				{
					if(MainGameLoop.level == 1 && cutscene && MainGameLoop.p == null)
					{
						switchToGame();
					}
				}
			}
			
//			if(MainGameLoop.p != null)
//			{
//				if(MainGameLoop.p.isBuilding() && MainGameLoop.p.snowballs>0)
//				{
//					int bx = 0, by = 0;
//					int width = 0, height = 0;
//					boolean horizontal = MainGameLoop.p.isBuildHorizontal();
//					if(horizontal)
//					{
//						bx = (int)(mx-32 + cam.getX());
//						by = (int)(my-5 + cam.getY());
//						width = 64;
//						height = 10;
//					}else {
//						bx = (int)(mx-5 + cam.getX());
//						by = (int)(my-32 + cam.getY());
//						width = 10;
//						height = 64;
//					}
//					Rectangle pBounds = MainGameLoop.p.getBounds();
//					Rectangle wallBounds = new Rectangle(bx,by,width,height);
//					if(!pBounds.intersects(wallBounds))
//					{
//						handler.addBlocks(new Wall(bx,by,BlockID.Wall,width,height,MainGameLoop.p.snowballType[0],handler));
//						MainGameLoop.p.snowballs--;
//					}
//				}
//			}
			
		}
	}
	
	public void switchToGame()
	{
//		MainGameLoop.w.frameA.removeMouseListener(this);
		handler.removeAll();
		MainGameLoop.loadImageLevelFromFile();
		MainGameLoop.game.p = new Player(MainGameLoop.startingPosition[0],MainGameLoop.startingPosition[1],PlayerID.Penguin,handler);
		handler.addPlayer(MainGameLoop.game.p);
		hud.setP(MainGameLoop.game.p);
		composite = 0.0f;
		scene = 0;
		max_scenes = 0;
		cutscene = false;	
	}
	
	public boolean isCutScene()
	{
		return cutscene;
	}
}
