package com.kevinli.manager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/*
 * Main Purpose:
 * -------------
 * Handle the Input for the player while in game
 */

import com.kevinli.ids.BlockID;
import com.kevinli.ids.PlayerID;
import com.kevinli.main.MainGameLoop;
import com.kevinli.main.MainGameLoop.STATE;
import com.kevinli.objects.Player;

public class KeyInputHandler extends KeyAdapter 
{
	//VARIABLE DECLARATION
	
	//PRIVATE VARIABLES
	private Handler handler;
	private MainGameLoop game;
	private int playerSpeed;
	//Represents WASD
	public static boolean keyDown[] = new boolean[4];
	private int keyValues[] = {KeyEvent.VK_W,KeyEvent.VK_A,KeyEvent.VK_S,KeyEvent.VK_D};
	private boolean holdE = false;
	
	//END OF VARIABLE DECLARATION
	
	//Constructor
	public KeyInputHandler(MainGameLoop game,Handler handler)
	{
		this.handler = handler;
		this.game = game;
		playerSpeed = MainGameLoop.p.playerSpeed;
		//Set all the keys to be false
		for (int i = 0;i<4;i++){keyDown[i] = false;}
	}
	
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		if(!MainGameLoop.gameState.equals(STATE.Login) && !MainGameLoop.gameState.equals(STATE.Chatroom))
		{
			if(key == KeyEvent.VK_M)
			{
				MainGameLoop.music = !MainGameLoop.music;
				if(MainGameLoop.music)
				{
					MainGameLoop.BGMusicSource.play();
				}else {
					MainGameLoop.BGMusicSource.pause();
				}
			}
			
			if(key == KeyEvent.VK_UP) {MainGameLoop.BGMusicSource.adjustVolume(0.1f);}
			if(key == KeyEvent.VK_DOWN) {MainGameLoop.BGMusicSource.adjustVolume(-0.1f);}
			
	//		if(key == KeyEvent.VK_F)
	//		{
	//			MainGameLoop.w.frameA.setVisible(false);
	//			MainGameLoop.w.frameA.remove(game);
	//			MainGameLoop.w.frameB.add(game);
	//			MainGameLoop.w.frameB.setVisible(true);
	//			MainGameLoop.w.frameB.requestFocusInWindow();
	//			
	//			MainGameLoop.WIDTH = MainGameLoop.w.frameB.getWidth();
	//			MainGameLoop.HEIGHT = MainGameLoop.w.frameB.getHeight();
	//			if(MainGameLoop.gameState.equals(STATE.Menu))
	//			{
	//				MainGameLoop.p.adjustToFulLScreen();
	//			}
	//		}
	//		if(key == KeyEvent.VK_ESCAPE)
	//		{
	//			MainGameLoop.w.frameB.setVisible(false);
	//			MainGameLoop.w.frameB.remove(game);
	//			MainGameLoop.w.frameA.add(game);
	//			MainGameLoop.w.frameA.setVisible(true);
	//			MainGameLoop.w.frameA.requestFocusInWindow();
	//			
	//			MainGameLoop.WIDTH = MainGameLoop.w.frameA.getWidth();
	//			MainGameLoop.HEIGHT = MainGameLoop.w.frameA.getHeight();			
	//			if(MainGameLoop.gameState.equals(STATE.Menu))
	//			{
	//				MainGameLoop.p.adjustToFulLScreen();
	//			}
	//		}
			if(MainGameLoop.gameState.equals(STATE.Game))
			{
				playerSpeed = MainGameLoop.p.playerSpeed;
				if(key == KeyEvent.VK_B)
				{
					MainGameLoop.p.setBuilding(!MainGameLoop.p.isBuilding());
				}
				if(key == KeyEvent.VK_R && MainGameLoop.p.isBuilding())
				{
					MainGameLoop.p.setBuildHorizontal(!MainGameLoop.p.isBuildHorizontal());
				}
				if(key == KeyEvent.VK_ESCAPE)
				{
					if(MainGameLoop.world.isCutScene()){MainGameLoop.world.switchToGame();}
				}
				
				if(key == KeyEvent.VK_Q && MainGameLoop.p.isThrowing()){MainGameLoop.p.setThrowing(false);MainGameLoop.p.throwing_angle=0;}
				
				if(key == KeyEvent.VK_SPACE)
				{
					MainGameLoop.p.setThrowing(true);
					if(MainGameLoop.p.throwing_angle<Math.PI/2)	MainGameLoop.p.throwing_angle+=0.05;
				}
				if(key == KeyEvent.VK_E)
				{
					holdE = true;
					if(MainGameLoop.p.getQueue() == 0 && MainGameLoop.p.fish>0 && MainGameLoop.p.health<100)
					{
						MainGameLoop.p.setCommand("eat");
						MainGameLoop.p.setQueue(Player.EAT_TIMER);
					}
				}
				if(key == KeyEvent.VK_RIGHT && MainGameLoop.p.snowballs>1)
				{
					int end = MainGameLoop.p.snowballType[MainGameLoop.p.snowballs-1];
					for(int i = MainGameLoop.p.snowballs-1;i>=0;i--)
					{
						if(i!=0)
						{
							MainGameLoop.p.snowballType[i] = MainGameLoop.p.snowballType[i-1];
						}else 
						{
							System.out.println("final end"+" "+end);
							MainGameLoop.p.snowballType[0] = end;
						}
					}
					
	//				for(int i = 0;i<MainGameLoop.p.snowballType.length;i++)
	//				{
	//					System.out.print(MainGameLoop.p.snowballType[i]+" ");
	//				}
				}
			}
			for(int i = 0;i<keyValues.length;i++)
			{
				if(key == keyValues[i])
				{
					keyDown[i]=true;
					if(i == 0)
					{
						if(!MainGameLoop.p.inWater())
						{
							if(!game.p.isJumping() && !game.p.isFalling())
							{
								game.getPlayer().setVely(-playerSpeed*2-3);game.p.setJumping(true);
							}
						}else 
						{
							game.getPlayer().setVely(-playerSpeed);
						}
					}
					if(i == 1) {game.getPlayer().setVelx(-playerSpeed);game.getPlayer().setDirection("left");}
	
					if(i == 2  && MainGameLoop.p.snowballs<5 &&MainGameLoop.p.getQueue() == 0) 
					{
						if(MainGameLoop.p.getBottomBlockID().equals(BlockID.SnowDirt))	
						{
							MainGameLoop.p.setCommand("snowball");
							MainGameLoop.p.setQueue((int)(Player.SNOWBALL_TIMER*Player.pickup_multiplier));
						}else if(MainGameLoop.p.getBottomBlockID().equals(BlockID.Ice))
						{
							MainGameLoop.p.setCommand("ice");
							MainGameLoop.p.setQueue((int)(Player.ICEBALL_TIMER*Player.pickup_multiplier));
						}
					}
					if(i == 3) {game.getPlayer().setVelx(playerSpeed);game.getPlayer().setDirection("right");}
				}
			}
		}else
		{
			if(MainGameLoop.gameState.equals(STATE.Login))
			{
				char s = e.getKeyChar();
				
				if( (key >= KeyEvent.VK_A && key <= KeyEvent.VK_Z) || (key>= KeyEvent.VK_NUMPAD0 && key <= KeyEvent.VK_NUMPAD9) )
				{
					game.login.getCharacter(s);			
				}
				
				if(key == KeyEvent.VK_BACK_SPACE)
				{
					game.login.removeCharacter();
				}
				
				if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP)
				{
					game.login.switchFocus();
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		for(int i = 0;i<keyValues.length;i++)
		{
			if(key == keyValues[i]){keyDown[i]=false;}
		}
		
		if (!keyDown[1] && !keyDown[3]){game.getPlayer().setVelx(0);}
		if (!keyDown[0] && !keyDown[2] && MainGameLoop.p.inWater()){game.getPlayer().setVely(1);}
		
		if(!keyDown[2])
		{
			MainGameLoop.p.setCommand("null");
			MainGameLoop.p.setQueue(0);
		}
		
		if(!holdE)
		{
			MainGameLoop.p.setCommand("null");
			MainGameLoop.p.setQueue(0);
		}
		
		if(key == KeyEvent.VK_SPACE && MainGameLoop.p.isThrowing())
		{
			double vel = 0.0;
			
			if(MainGameLoop.p.direction.equals("right"))
			{
				vel = 10;
			}else {
				vel = -10;
			}
			
			MainGameLoop.p.throwSnowball(MainGameLoop.p.throwing_angle,vel);
			MainGameLoop.p.setThrowing(false);
			MainGameLoop.p.throwing_angle = 0;
		}
	}
	
}
