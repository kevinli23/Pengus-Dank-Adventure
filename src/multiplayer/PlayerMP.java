package multiplayer;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.kevinli.abstract_classes.PlayerObject;
import com.kevinli.ids.PlayerID;
import com.kevinli.manager.Handler;

public class PlayerMP extends PlayerObject{

	public PlayerMP(float x, float y, PlayerID id, Handler handler, String name, int UID) {
		super(x, y, id);
	}
	
	public void render(Graphics g)
	{
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}
	
	

}
