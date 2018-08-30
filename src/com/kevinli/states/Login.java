package com.kevinli.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.kevinli.main.MainGameLoop;
import com.kevinli.server.GameClient;

/*
 * Main Purpose:
 * -------------
 * Login client that lets the user access their account	
 */

public class Login extends MouseAdapter
{
	//Instance of the main class
	MainGameLoop game;
	//The user's entered username and password
	private String username = "" ,password = "" ,appearPassword = "";
	private boolean topSelected = false;
	private boolean botSelected = false;
	private Color loginButton[] = {Color.white,Color.black};
	private boolean loginFail = false;
	
	//Constructor
	public Login(MainGameLoop game)
	{
		this.game = game;
	}
	
	public void tick()
	{
		
	}
	
	public void checkLoginCredentials(String username, String password)
	{
		if(MainGameLoop.client == null)
		{
			MainGameLoop.client = new GameClient(username,password);
			game.receive();
		}else
		{
			MainGameLoop.client.setUsername(username);
			MainGameLoop.client.setPassword(password);
			MainGameLoop.client.sendLoginInfo();
		}
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, MainGameLoop.WIDTH, MainGameLoop.HEIGHT);
		
		g.setFont(new Font("Arial",1,20));
		g.setColor(Color.white);
		g.fillRect(MainGameLoop.WIDTH/2 - 100, 270, 200, 40);
		g.fillRect(MainGameLoop.WIDTH/2 - 100, 370, 200, 40);
		g.drawString("Username",MainGameLoop.WIDTH/2 - g.getFontMetrics().stringWidth("Username")/2 - 50,250);
		g.drawString("Password",MainGameLoop.WIDTH/2 - g.getFontMetrics().stringWidth("Password")/2 - 50,350);
		
		g.setColor(Color.black);
		g.drawString(username, MainGameLoop.WIDTH/2 - 100, 295);
		g.drawString(appearPassword, MainGameLoop.WIDTH/2 - 100, 395);
		
		g.setColor(loginButton[0]);
		g.fillRoundRect(MainGameLoop.WIDTH/2 - 75, 450, 150, 50, 10, 10);
		g.setColor(loginButton[1]);
		g.drawString("Login", MainGameLoop.WIDTH/2 - g.getFontMetrics().stringWidth("Login")/2-3, 480);
		
		if(topSelected)
			g.setColor(Color.red);
		else
			g.setColor(Color.white);
		
		g.drawRect(MainGameLoop.WIDTH/2 - 100, 270, 200, 40);
		
		if(botSelected)
			g.setColor(Color.red);
		else
			g.setColor(Color.white);
		
		g.drawRect(MainGameLoop.WIDTH/2 - 100, 370, 200, 40);	
		
		if(loginFail)
		{
			g.setColor(Color.red);
			g.drawString("Failed Login", 342, 530);
		}
	}
	
	//MouseEvents
	public void mousePressed(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		
		if(MainGameLoop.mouseOver(mx, my, MainGameLoop.WIDTH/2 - 100, 270, 200, 40))
		{
			topSelected = !topSelected;
			if(topSelected) botSelected = false;
		}
		if(MainGameLoop.mouseOver(mx, my, MainGameLoop.WIDTH/2 - 100, 370, 200, 40))
		{
			botSelected = !botSelected;
			if(botSelected) topSelected = false;
		}
		
		if(!MainGameLoop.mouseOver(mx, my, MainGameLoop.WIDTH/2 - 100, 270, 200, 40) && !MainGameLoop.mouseOver(mx, my, MainGameLoop.WIDTH/2 - 100, 370, 200, 40))
		{
			topSelected = false;
			botSelected = false;
		}
		if(MainGameLoop.mouseOver(mx,my,MainGameLoop.WIDTH/2 - 75, 450, 150, 50))
		{
			checkLoginCredentials(username, password);
		}
	}
	
	public void mouseMoved(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		
		if(MainGameLoop.mouseOver(mx,my,MainGameLoop.WIDTH/2 - 75, 450, 150, 50))
		{
			loginButton[0] = Color.orange;
			loginButton[1] = Color.blue;
		}
		else
		{
			loginButton[0] = Color.white;
			loginButton[1] = Color.black;			
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{
	}
	
	public void getCharacter(char s)
	{
		if(topSelected)
		{
			username+=s;
		}
		if(botSelected)
		{
			password+=s;
			appearPassword+="*";
		}
	}
	
	public void removeCharacter()
	{
		if(topSelected) 
		{
			if(username.length() > 0)
				username = username.substring(0,username.length()-1);
		}
		else if (botSelected) 
		{
			if(password.length() > 0)
			{
				password = password.substring(0,password.length()-1);
				appearPassword = appearPassword.substring(0,appearPassword.length()-1);
			}
		}
	}
	
	public void switchFocus()
	{
		if(topSelected || botSelected)
		{
			topSelected = !topSelected;
			botSelected = !botSelected;
		}
	}
	
	public void loginFail()
	{
		loginFail = true;
	}
	//End of MouseEvents
}
