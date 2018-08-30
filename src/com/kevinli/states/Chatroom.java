package com.kevinli.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.kevinli.main.MainGameLoop;
import com.kevinli.main.MainGameLoop.STATE;
import com.kevinli.manager.Texture;
import com.kevinli.server.GameClient;

public class Chatroom implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener
{
	
	//VARIABLE DECLARATION
	
	//initialize the texture object
	Texture tex;
	
	//list that stores the chat text
	ArrayList<String> globalchat = new ArrayList<String>();
	
	//current text that is being written
	String inputText = "";
	
	//whether the writing box is selected
	boolean write = false;
	
	//whether the mic is turned on to join global voice chat
	public boolean mic = true;
	
	//Game Client object
	GameClient client;
	
	//how far down the chat you have scrolled
	int relativeY = 0;
	
	//the lowest the chat can be scrolled to
	int yMin = 0;
	
	//the max the chat can be scrolled to
	int yMax = 0;
	
	//same functions as above, just for the users list
	int relativeSideY = 0;
	int ySideMax = 0;
	
	//Variables to get what you say into your microphone
	DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, MainGameLoop.format);
	TargetDataLine targetLine = null;
	
	//Color constants
	private final Color SELECTED = new Color(240,128,128);
	private final Color AZURE = new Color(240,255,255);
	
	//Date objects to display the hours and minutes
	Date date;
	DateFormat dateFormat = new SimpleDateFormat("HH:mm");
	
	//Stores the users connected to the game
	String users[];
	
	//MainGameLoop object
	MainGameLoop game;
	
	//END OF VARIABLE DECLARATION

	//Used to get the speech you say into your mic
	public void getInput()
	{
		Thread voice = new Thread() {
			public void run()
			{
				//while you have your mic boolean true
				while(mic)
				{
					try {
						//get the speech, convert to bytes, and send to server
						if(targetLine == null)
						{
							targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
							targetLine.open(MainGameLoop.format);
							targetLine.start();
						}
						
						//initialize the byte array
						int numBytesRead;
						byte[] targetData = new byte[targetLine.getBufferSize() /5];
	
						//get what was said and convert
						numBytesRead = targetLine.read(targetData, 0, targetData.length);
						if (numBytesRead == -1)	return;
						
						//prefix for server processing
						String prefixString = "/v/";
						
						byte[] prefix = prefixString.getBytes();
						byte[] send = new byte[targetData.length+prefix.length];
						
						//Add the prefix to the byte of speech
						System.arraycopy(prefix, 0, send, 0, prefix.length);
						System.arraycopy(targetData, 0, send, prefix.length, numBytesRead);
						
						//send the combined byte array to the server
						client.send(send);
						
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
			}
			}
		};
		//start the thread
		voice.start();
	}

	//Constructor
	public Chatroom(MainGameLoop game, GameClient client)
	{
		//set the login object to null
		game.login = null;
		//localize the client object
		this.client = client;
		//initialize the texture
		tex = MainGameLoop.getTextureInstance();
		//initialize the game
		this.game = game;
		
		//add component listeners
		game.addKeyListener(this);
		game.addMouseMotionListener(this);
		game.addMouseListener(this);
		game.addMouseWheelListener(this);
		
		//start the mic thread
//		getInput();
	}
	
	//draws the chatroom
	public void render(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		//fill the background
		g.setColor(Color.black);
		g.fillRect(0, 0, MainGameLoop.WIDTH, MainGameLoop.HEIGHT);
		
		//set color and text style
		g.setColor(new Color(0,98,128));
		g.setFont(new Font("Arial",1,24));
		
		//draw the chat
		drawGlobalChat(g2d);
		
		//fill a black box to the right, where the users will be placed
		g.setColor(Color.black);
		g.fillRect((int)(MainGameLoop.WIDTH*0.75), 0, (int)(MainGameLoop.WIDTH*0.25), (int)(MainGameLoop.HEIGHT*0.92));

		//background for the global chat tab
		g.setColor(Color.black);
		g.fillRect(0, 40, (int)(MainGameLoop.WIDTH*0.37385), 60);
		
		//if in global chat, set background color to selected
		g.setColor(SELECTED);
		g.fillRect((int)(MainGameLoop.WIDTH*0.37385), 40, (int)(MainGameLoop.WIDTH*0.37385), 60);
		
		//fill the input text field to a white rectangle
		g.setColor(Color.white);
		g.fillRect(0, (int)(MainGameLoop.HEIGHT*0.05)+(int)(MainGameLoop.HEIGHT*0.87), (int)(MainGameLoop.WIDTH*0.80), (int)(MainGameLoop.HEIGHT*0.13)-(int)(MainGameLoop.HEIGHT*0.05));
		
		//if selected, draw a red rectangle ontop of the white rectangle
		if(write)
		{
			g.setColor(Color.red);
			g.drawRect(0, (int)(MainGameLoop.HEIGHT*0.05)+(int)(MainGameLoop.HEIGHT*0.87), (int)(MainGameLoop.WIDTH*0.80), (int)(MainGameLoop.HEIGHT*0.129)-(int)(MainGameLoop.HEIGHT*0.05));
		}
		
		//Set color and font to a tint of blue
		g.setColor(new Color(0,98,128));
		g.setFont(new Font("Arial",1,20));
		
		//draw the text that is being inputted
		int offset = inputText.length()/50;
		String inputDisplay = inputText.substring(offset*50, inputText.length());
		g.drawString(inputDisplay, 5, (int)(MainGameLoop.HEIGHT*0.10)+(int)(MainGameLoop.HEIGHT*0.865));
		g.setColor(Color.black);
		g.drawLine(g2d.getFontMetrics().stringWidth(inputDisplay)+10, (int)(MainGameLoop.WIDTH*0.935), g2d.getFontMetrics().stringWidth(inputDisplay)+10, (int)(MainGameLoop.WIDTH*0.98));
		
		//set color to white
		
		//draw and list all the connected users
		for(int i = 0;i<users.length;i++)
		{
			String username = users[i];
			
			g.setColor(Color.white);
			if(username.equals(client.getName())) 
			{
				g.setColor(Color.green);
			}

			g.drawString(username, (int)(MainGameLoop.WIDTH*0.76), relativeSideY+75 + 60*i);
			
			g.setColor(Color.white);
			g.drawRect((int)(MainGameLoop.WIDTH*0.75), relativeSideY+40+60*i, (int)(MainGameLoop.WIDTH*0.3), 60);
		}
		//draw a line dividing chat from users
		g.drawLine((int)(MainGameLoop.WIDTH*0.75), 0, (int)(MainGameLoop.WIDTH*0.75), (int)(MainGameLoop.HEIGHT*0.92));
		
		//draw the globalchat tab and blank tab
		g.drawRect(0, 40, (int)(MainGameLoop.WIDTH*0.37385), 60);
		g.drawRect((int)(MainGameLoop.WIDTH*0.37385), 40, (int)(MainGameLoop.WIDTH*0.37385), 60);
		
		g.drawString("Global Chat", (int)(MainGameLoop.WIDTH*0.12), (int)(MainGameLoop.WIDTH*0.10));
		
		//draw the send button
		g.setColor(Color.orange);
		g.fillRect((int)(MainGameLoop.WIDTH*0.8), (int)(MainGameLoop.HEIGHT*0.92), (int)(MainGameLoop.WIDTH*0.2), (int)(MainGameLoop.HEIGHT*0.08));
		
		//draw the send string
		g.setColor(new Color(0,98,128));
		g.setFont(new Font("Arial",1,24));
		g.drawString("Send", (int)(MainGameLoop.WIDTH*0.859), (int)(MainGameLoop.HEIGHT*0.97));
		
		//draw the mic
		g.drawImage(tex.staticImages.get("mic"),(int)(MainGameLoop.WIDTH*0.75),(int)(MainGameLoop.HEIGHT*0.9225),null);
	
	}
	
	//method to draw the global chat
	private void drawGlobalChat(Graphics g2d)
	{
		//loop through chat and draw it
		for(int i = 0;i<globalchat.size();i++)
		{
			//get the message
			String message = globalchat.get(i);
			
			//process based off what their prefixes are
			
			//GT stands for Global+Time
			//Is the first line of a block of text
			if(message.startsWith("GT")) {
				g2d.setColor(Color.green);
				String time = message.substring(2, 7);
				g2d.drawString(time, 5, 130-relativeY+30*i);
				
				g2d.setColor(Color.white);
				message = message.substring(8);
				g2d.drawString(message, 5+g2d.getFontMetrics().stringWidth(time+" "), 130-relativeY + 30*i);
			}
			//Every other line of a block of text with Global prefix
			else if(message.startsWith("G"))
			{
				g2d.drawString(message.substring(1), 5, 130-relativeY + 30*i);	
			}
			//Whisper+Time+Self
			//first line of the text displayed when message is sent to a client
			else if(message.startsWith("WTS"))
			{
				g2d.setColor(Color.MAGENTA);
				String time = message.substring(3, 8);
				g2d.drawString(time, 5, 130-relativeY+30*i);
				
				g2d.setColor(Color.yellow);
				message = message.substring(9);
				g2d.drawString(message, 5+g2d.getFontMetrics().stringWidth(time+" "), 130-relativeY + 30*i);
				
			}
			//Every other line of a whisper-self block of text
			else if(message.startsWith("WS"))
			{
				g2d.setColor(Color.yellow);
				g2d.drawString(message.substring(2), 5, 130-relativeY + 30*i);
			}
			//Whisper + Time
			//first line of the whisper received from another client, displays the sending client's name and text
			else if(message.startsWith("WT")) {
				g2d.setColor(Color.MAGENTA);
				String time = message.substring(2, 7);
				g2d.drawString(time, 5, 130-relativeY+30*i);
				
				g2d.setColor(Color.yellow);
				message = message.substring(8);
				g2d.drawString(message, 5+g2d.getFontMetrics().stringWidth(time+" "), 130-relativeY + 30*i);
			}
			//all other lines of a whisper block of text
			else if(message.startsWith("W"))
			{
				g2d.setColor(Color.yellow);
				g2d.drawString(message.substring(1), 5, 130-relativeY + 30*i);
			}
		}
	}
	
	//Updates game logic
	//Unneeded due to fact that everything is based off server logic
	public void tick()
	{
	}
	
	//adds message to the arraylist and update the max scrollable y range
	public void addMessage(String message)
	{
		globalchat.add(message);
		yMax = (globalchat.size()-21)*30;
		if(yMax<0) yMax = 0;
		
		if(globalchat.size()>21)
		{
			relativeY+=30;
		}
	}

	//method that comes with the KeyListener, depending on what is pressed, it executes actions
	//if write is true, every character from A-Z|a-z and " ", /, and : are added to inputText
	//backspaces removes the last character in the inputText string
	//Enter sends and clears the inputText
	//Escape returns the user back to the main menu
	@Override
	public void keyPressed(KeyEvent ke) {
		int key = ke.getKeyCode();
		
		if(write)
		{
			if((key>=KeyEvent.VK_A && key<=KeyEvent.VK_Z) || (key>=KeyEvent.VK_0 && key<=KeyEvent.VK_9) || (key == KeyEvent.VK_SPACE) || (key == KeyEvent.VK_SLASH)
					||(key == KeyEvent.VK_SEMICOLON) || (key == KeyEvent.VK_COMMA)||(key == KeyEvent.VK_QUOTE))
			{
				inputText+= ke.getKeyChar();
			}
			if(key == KeyEvent.VK_BACK_SPACE && inputText.length()>0)
			{
				inputText = inputText.substring(0, inputText.length()-1);
			}
			if(key == KeyEvent.VK_ENTER && !inputText.equals(""))
			{
				send();
			}
		}
		
		if(key == KeyEvent.VK_ESCAPE)
		{
			returnToMenu();
		}
		
	}
	
	//reset all variables and set gamestate back to menu
	public void returnToMenu()
	{
		globalchat.clear();
		String inputText = "";
		boolean write = false;
		mic = true;
		int relativeY = 0;
		int yMin = 0;
		int yMax = 0;
		int relativeSideY = 0;
		int ySideMax = 0;

		MainGameLoop.gameState = STATE.Menu;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(MainGameLoop.mouseOver(mx, my, 0, (int)(MainGameLoop.HEIGHT*0.05)+(int)(MainGameLoop.HEIGHT*0.85), (int)(MainGameLoop.WIDTH*0.79), (int)(MainGameLoop.HEIGHT*0.15)-(int)(MainGameLoop.HEIGHT*0.05)))
		{
			write = !write;
		}
		
//		if(MainGameLoop.mouseOver(mx, my, (int)(MainGameLoop.WIDTH*0.75), 0, (int)(MainGameLoop.WIDTH*0.25), (int)(MainGameLoop.HEIGHT*0.92))){
//			int target = (my + relativeSideY - 40)/60;
//			whisperClient = users[target];
//			whisper = true;
//			getInput();
//		}
		
//		if(MainGameLoop.mouseOver(mx, my, 0, 40, (int)(MainGameLoop.WIDTH*0.37385), 60))
//		{
//			whisper = false;
//		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void send()
	{
		String message = "";
		boolean sendable = false;
		
//		if(!whisper) 
		if(!inputText.startsWith("/w ")) {
			message = "/m/"+getTime()+" "+client.getName()+": "+inputText+"/e/";
			sendable = true;
		}else {
			String info[] = inputText.split("/w |: ");
			for(int i = 0;i<users.length;i++)
			{
				if(users[i].equals(info[1]))
				{
					message = "/w/"+info[1]+"/n/"+getTime()+"/n/"+client.getName()+"/n/"+info[2]+"/e/";
					String displayMessage = getTime()+">>>"+info[1]+": "+info[2];
					addTextToChatroom(displayMessage,"whisper-self");
					sendable = true;
					break;
				}
			}
		}
//		else message = "/w/"+whisperClient+"/n/"+getTime()+"/n/"+client.getName()+"/n/"+inputText+"/e/";
//		if(whisper)
//		{
//			whisperchat.add(getTime()+" "+client.getName()+": "+inputText);
//		}
		inputText = "";
		
		
		if(sendable) client.send(message.getBytes());
	}
	
	//get the current time in the format hour:minutes
	private String getTime()
	{
		date = new Date();
		return dateFormat.format(date);
	}

	//Part of the MouseListener implementation, handles all mouse wheel movement
	//if scroll up or down when yMax > 0, it will scroll down/up the chat
	//same applies to the users list
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int click = e.getWheelRotation()*15;
		int mx = e.getX();
		int my = e.getY();
		
		if(MainGameLoop.mouseOver(mx,my,0,0,(int)(MainGameLoop.WIDTH*0.75),(int)(MainGameLoop.HEIGHT*0.92)))
		{
			if(relativeY+click >= yMin && relativeY+click <= yMax)
			{
				relativeY+=click;
			}		
		}
		if(MainGameLoop.mouseOver(mx, my, (int)(MainGameLoop.WIDTH*0.75), 0, (int)(MainGameLoop.WIDTH*0.25), (int)(MainGameLoop.HEIGHT*0.92))){
			if(relativeSideY+click >= 0 && relativeSideY+click <= ySideMax){
				relativeSideY+=click;
			}
		}
	}
	
	//updates the list of users
	public void updateUsers(String[] data)
	{
		users = data;
		if(users.length>12)
		{
			ySideMax = (users.length-12)*60;
		}
	}
	
	//Adds a block of message text to the global chat array
	public void addTextToChatroom(String message,String type)
	{
		//their prefix, used for when they are drawn onscreen
		String initialPrefix = "";
		String otherPrefix = "";
		
		//if it's a global message -> GT and G
		//if it's a whisper -> WT and W
		//if it's a whisper that's displayed on your screen -> WTS and WS
		
		if(type.equals("global"))
		{
			initialPrefix = "GT";
			otherPrefix = "G";
		}else if(type.equals("whisper"))
		{
			initialPrefix = "WT";
			otherPrefix = "W";
		}else if(type.equals("whisper-self")) {
			initialPrefix = "WTS";
			otherPrefix = "WS";
		}
		
		//message length (number of characters)
		int length = message.length();
		//max characters per line -> 43
		int chars = 43;
		//number of lines required
		int lines = message.length()/chars;
		
		if(length>=chars) 
		{
			//add message with the initial prefix
			addMessage(initialPrefix+message.substring(0,chars));
			//every other line add the other prefix
			for(int i = 1;i<lines;i++)
			{
				addMessage(otherPrefix+message.substring(i*chars, i*chars + chars));
			}
		}else {			
			//if there is only one line, just add it
			addMessage(initialPrefix+message);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
