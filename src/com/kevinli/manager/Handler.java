package com.kevinli.manager;

import java.awt.Graphics;
import java.util.LinkedList;

import com.kevinli.abstract_classes.BlockObject;
import com.kevinli.abstract_classes.EnemyObject;
import com.kevinli.abstract_classes.PlayerObject;

/*
 * Main Purpose:
 * -------------
 * Handles the ticking and rendering of all game objects
 */

public class Handler 
{
	//CREATION OF VARIABLES
	
	//Create 3 linkedlists that hold the game objects
	public LinkedList<PlayerObject> players = new LinkedList<>();
	public LinkedList<EnemyObject> enemies = new LinkedList<>();
	public LinkedList<BlockObject> blocks = new LinkedList<>();	
	public LinkedList<BlockObject> snowflakes = new LinkedList<>();
	
	//END OF CREATION OF VARIABLES
	
	//Constructor
	public Handler()
	{
		
	}
	
	public void tick()
	{
		for(PlayerObject p:players) p.tick();
		for(int i = 0;i<blocks.size();i++)
		{
			blocks.get(i).tick();
		}
		for(int i = 0;i<enemies.size();i++)
		{
			enemies.get(i).tick();
		}
		for(int i = 0;i<snowflakes.size();i++)
		{
			snowflakes.get(i).tick();
		}
		
	}
	public void render(Graphics g)
	{
		for(PlayerObject p:players) p.render(g);
		for(int  i = 0;i<blocks.size();i++)
		{
			blocks.get(i).render(g);
		}
		for(int i = 0;i<enemies.size();i++)
		{
			enemies.get(i).render(g);
		}
		for(int i = 0;i<snowflakes.size();i++)
		{
			snowflakes.get(i).render(g);
		}
	}
	
	public void addBlocks(BlockObject b) {blocks.add(b);}
	public void addPlayer(PlayerObject p){players.add(p);}
	public void addEnemy(EnemyObject e){enemies.add(e);}
	public void addSnowflake(BlockObject b) {snowflakes.add(b);}
	
	public void removeBlock(BlockObject b){blocks.remove(b);}
	public void removePlayer(PlayerObject p){players.remove(p);}
	public void removeEnemy(EnemyObject e){enemies.remove(e);}
	
	public void removeSnowflake(BlockObject b)
	{
		snowflakes.remove(b);
	}
	public void removeAllSnowflakes()
	{
		snowflakes.clear();
	}
		
	public void removeAllButPlayer() {blocks.clear();enemies.clear();}
	public void removeAllBlocks(){blocks.clear();}
	public void removeAllEnemies(){enemies.clear();}
	public void removePlayer(){players.clear();}
	public void removeAll(){blocks.clear();enemies.clear();players.clear();}
	
	
}
