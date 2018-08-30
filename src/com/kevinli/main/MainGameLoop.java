//Author: Kevin Li
//Date of Last Edit: October 19th 2017 9:30 A.M.
//Game Engine Layout

package com.kevinli.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;

import org.lwjgl.LWJGLException;

import com.kevinli.ids.BlockID;
import com.kevinli.ids.EnemyID;
import com.kevinli.ids.PlayerID;
import com.kevinli.manager.Handler;
import com.kevinli.manager.KeyInputHandler;
import com.kevinli.manager.Texture;
import com.kevinli.objects.Block;
import com.kevinli.objects.Enemy;
import com.kevinli.objects.Player;
import com.kevinli.objects.Wolf;
import com.kevinli.server.GameClient;
import com.kevinli.states.Chatroom;
import com.kevinli.states.Game;
import com.kevinli.states.Login;
import com.kevinli.states.Menu;

import audio.AudioMaster;
import audio.Source;

/*
 * Main Purpose:
 * -------------
 * MainGameLoop class handles the game actions
 */

public class MainGameLoop extends Canvas implements Runnable, MouseListener, MouseMotionListener
{
	
	//VARIABLES--------------------------------------------------------------------------
	
	//Server Stuff
	public static final String STRINGIP = "159.89.113.97";
	public static final int PORT = 40000;
	public DatagramSocket socket;
	public InetAddress IP;
	public static GameClient client;
	//End of Server Stuff
	
	//CONSTANTS
	static final long serialVersionUID = -1928828378864222889L;
	public static final Font[] arial = {new Font("Arial",0,12),new Font("Arial",0,24),
										new Font("Arial",0,36),new Font("Arial",1,48)};
	
	private static final Color LIGHT_YELLOW = new Color(255,250,205);
	private static final Color TOMATO = new Color(255,99,71);
	private static final Color SPRING_GREEN = new Color(0,255,127);
	
	//Screen Dimensions
	public static int WIDTH = 800;
	public static int HEIGHT = 800;
	
	//Screen Title
	public static final String TITLE = "Pengu's Dank Adventure";
	public static File path;
	
	//ENUMS
	public enum STATE{Menu,Game,Help,Options,Login,Chatroom;}
	
	//PUBLIC VARIABLES
	public Thread t,receive;
	
	public static Window w;
	public static Player p;
	public static Game world;
	
	public static int map[][];
	public static int mapSizeX;
	public static int mapSizeY;
	public static int level = 1;
	public static int loadingProgress = 0;
	public static String loadingString = "";
	
	public static MainGameLoop game;
	
	public static boolean music = true;
	public static boolean fullscreen = false;
	
	public static STATE gameState = STATE.Login;
	
	public static int[] startingPosition = new int[2];
	
	public static Texture tex;
	public static boolean dragging = false;
	public static Font gameFont_20,gameFont_30,gameFont_40,gameFont_60;
	
	//State Objects
	public Menu menu;
	public Login login;
	
	//Music and Sounds
	public static Source BGMusicSource,MeepSource;
	
	public static AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, false);
	public DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);
	
	//PRIVATE VARIABLES
	private Graphics g;
	private Chatroom chatroom;
	
	private static Random rand;
	private static Handler handler;
	
	private boolean running = false;
	
	private Color buttonColors[] = {Color.red,Color.yellow,Color.green};
	//END OF PRIVATE VARIABLES
	
	//End of Variables--------------------------------------------------------------------
	
	//loads the audo using the AudioMaster Class
	//loads the background music and a sound
	public static void loadAudio() throws LWJGLException
	{
		
		loadingString = "Initializing Audio Player";
		AudioMaster.init();
		AudioMaster.setListenerData();
		loadingProgress = 5;
		
		loadingString = "Loading Sounds";
		int buffer = AudioMaster.loadSound("res/music/meep.wav");
		loadingProgress = 15;
		MeepSource = new Source(buffer,1.0f,1.75f,0,0,0);
		loadingProgress = 25;
		
		loadingString = "Loading Music";
		int buffer2 = AudioMaster.loadSound("res/music/BGMUSIC.wav");
		loadingProgress = 85;
		BGMusicSource = new Source(buffer2,0.5f,1.0f,0,0,0);
		
		loadingProgress = 99;
		loadingString = "Initializing Game";
//		playBGMusic();
	}
	
	//Plays the background music
	public static void playBGMusic()
	{
		BGMusicSource.play();
	}
	
	//initialize the textures and audio
	//called right after the splashscreen ends
	public static void loadOnMain()
	{
		tex = new Texture();
		try {
			loadAudio();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	//used to run exe's in same directory
	private static void runExe()
	{
		try {
			Runtime.getRuntime().exec("cmd /c start abc.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//main method assigns path, creates a new instance of itself, and creates the game window
	public static void main(String[] args) 
	{
		path = new File("");
		game = new MainGameLoop();
		w = new Window(TITLE,WIDTH,HEIGHT,game);
	}
	
	//Starts the main thread and sets the running boolean to true
	public synchronized void start()
	{
		//If the program is already running, don't execute the body
		if(running) return;
		//Set running to true
		running = true;
		//Create a new main thread
		t = new Thread(this,"Main Game Thread");
		//Stop the thread
		t.start();
	}
	
	//Kills the main thread and sets running to false
	public synchronized void stop()
	{
		//Set running to false
		running = false;
		//Stop the thread
		try 
		{
			t.join();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	//Called when the program starts, initializes object, fonts, and window settings
	private void init()
	{
		//Read in the True Type Font and derive fonts 20 30 40 and 60
		InputStream stream20 = ClassLoader.getSystemClassLoader().getResourceAsStream("fonts/AgentOrange.ttf");
		InputStream stream30 = ClassLoader.getSystemClassLoader().getResourceAsStream("fonts/AgentOrange.ttf");
		InputStream stream40 = ClassLoader.getSystemClassLoader().getResourceAsStream("fonts/AgentOrange.ttf");
		InputStream stream60 = ClassLoader.getSystemClassLoader().getResourceAsStream("fonts/AgentOrange.ttf");
		try {
			gameFont_20 = Font.createFont(Font.TRUETYPE_FONT, stream20).deriveFont(20f);
			gameFont_30 = Font.createFont(Font.TRUETYPE_FONT, stream30).deriveFont(30f);
			gameFont_40 = Font.createFont(Font.TRUETYPE_FONT, stream40).deriveFont(40f);
			gameFont_60 = Font.createFont(Font.TRUETYPE_FONT, stream60).deriveFont(60f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//End of deriving fonts
		
		//Initialize server objects (socket and IP)
		try {
			socket = new DatagramSocket();
			IP = InetAddress.getByName(STRINGIP);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Create new Objects
		handler = new Handler();
		rand = new Random();
		menu = new Menu(handler);
		world = new Game(handler);
		login = new Login(game);
		p = new Player(100,(int)(MainGameLoop.HEIGHT*(6.1/8)),PlayerID.Penguin,handler);
		handler.addPlayer(p);
		//End of new Objects
		
		//Adding input handlers to the frame
		this.addKeyListener(new KeyInputHandler(this, handler));
		this.addMouseListener(menu);
		this.addMouseWheelListener(menu);
		this.addMouseMotionListener(menu);
		this.addMouseMotionListener(world);
		this.addMouseListener(world);
		this.addMouseListener(game);
		this.addMouseMotionListener(game);
		this.addMouseListener(login);
		this.addMouseMotionListener(login);
		
	}
	
	//logic updates for the game
	public void tick()
	{
		//Do the proper tick command depnding on which state the user is on
		if(gameState.equals(STATE.Login)) {login.tick();}
		if(gameState.equals(STATE.Chatroom)) {chatroom.tick();}
		if(!gameState.equals(STATE.Game)&&!(gameState.equals(STATE.Login))&&!gameState.equals(STATE.Chatroom)){menu.tick();handler.tick();}
		else if(gameState.equals(STATE.Game))
		{
			if(!(menu == null))
			{
				menu = null;
			}
			world.tick();
		}
	}
	
	//graphics update for the game
	public void render()
	{
		//Creates a buffer strategy
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			//Create 3 buffer strategies
			this.createBufferStrategy(3);
			return;
		}
		//Set our graphics instance to the graphics from the buffer strategy
		g = bs.getDrawGraphics();
		//Create a graphics 2d instance
		Graphics2D g2d = (Graphics2D) g;
		
		//renders the images based on what state the user is on
		if(gameState.equals(STATE.Login)) {login.render(g);}
		else if(gameState.equals(STATE.Chatroom)) {chatroom.render(g);}
		else if(!gameState.equals(STATE.Game)){menu.render(g);handler.render(g);}
		else if(gameState.equals(STATE.Game)){world.render(g);}
		
		drawTopBar(g);
		
		//dispose graphics
		g.dispose();
		g2d.dispose();
		//show buffer strategy
		bs.show();
	}
	
	//Draws the top bar with the three buttons and handles the dragging color changes
	private void drawTopBar(Graphics g)
	{
		if(!dragging)
		{
			g.setColor(new Color(135,206,250));
		}
		else 
		{
			g.setColor(new Color(64,224,208));
		}
		
		g.fillRect(0, 0, WIDTH, 40);
		
		g.setColor(buttonColors[0]);
		g.fillOval(WIDTH-30, 10, 20, 20);
		
		g.setColor(buttonColors[1]);
		g.fillOval(WIDTH-60, 10, 20, 20);
		
		g.setColor(buttonColors[2]);
		g.fillOval(WIDTH-90, 10, 20, 20);
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial",1,20));
		g.drawString("Pengu's Dank Adventure", 45, 27);
	}
	
	//creates a thread that listens for information send from the server
	public void receive()
	{
		receive = new Thread("receive") 
		{
			public void run()
			{
				while(running)
				{
					DatagramPacket packet = client.receive();
					process(packet);
				}
			}
		};
		receive.start();	
	}
	
	//process the information sent by the server and act accordingly
	public void process(DatagramPacket packet)
	{
		//convert the data to a string
		String message = new String(packet.getData());
		
		//successful connection, play the music, set the id, and change the game state to the menu
		if(message.startsWith("/c/"))
		{
			message = message.split("/c/|/e/")[1];
			String temp[] = message.split("!");
			client.setID(Integer.parseInt(temp[0]));
			playBGMusic();
			chatroom = new Chatroom(game,client);
			gameState = STATE.Menu;
//			client.setName(temp[1]);
		}
		
		//show message if login failed
		if(message.startsWith("Failed Login"))
		{
			login.loginFail();
		}
		
		//set this client's name
		if(message.startsWith("/n/"))
		{
			client.setName(message.split("/n/|/e/")[1]);
		}
		
		//ping from server, respond back with /i/ and id message
		if(message.startsWith("/ping/server"))
		{
			client.send(("/ping/"+client.getID()+"/e/").getBytes());
		}
		
		//message from a user in the chatroom
		if(message.startsWith("/m/"))
		{
			message = message.split("/m/|/e/")[1];
			chatroom.addTextToChatroom(message,"global");
		}
		
		//a list of online users (display names)
		if(message.startsWith("/u/"))
		{
			String u[];
			u = message.split("/u/|/n/|/e/");
			chatroom.updateUsers(Arrays.copyOfRange(u, 1, u.length-1));
		}

		//whisper sent by a specific client connected
		if(message.startsWith("/w/"))
		{
			String info[] = message.split("/w/|/n/|/e/");
			String text = info[2] + " "+info[3]+": "+info[4];
			chatroom.addTextToChatroom(text,"whisper");
			
		}
		
		//received voice chat with will be processed and played
		if(message.startsWith("/v/")&& gameState.equals(STATE.Chatroom))
		{
			if(chatroom.mic)
			{	
				byte[] data = packet.getData();
				byte[] targetData = new byte[1600];
	
				System.arraycopy(data,3,targetData,0,1600);
				receiveSound(targetData,targetData.length);
			}
		}
	}

	//run method that is called when main thread is started
	@Override
	public void run()
	{
		//call the init method
		init();
		this.requestFocus();
		//logic to make the game update and render 60 times per second
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		long timer2 = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;

		//while the game is running, render and update game logic
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				try {
					tick();
					render();
				} catch (Exception e) {
					e.printStackTrace();
				}
				updates++;
				frames++;
				delta--;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//render();
			//frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				//System.out.println(frames+" "+updates);
				timer += 1000;
				// fps = frames;
				frames = 0;
				updates = 0;

			}

		}
		//if no longer running, stop the main thread
		stop();
	}
	
	//OTHER STATIC METHODS
	
	//Method to load levels through text files or images
	public static void loadImageLevel(BufferedImage image)
	{
		int w = image.getWidth();
		int h = image.getHeight();
		
		for (int xx = 0; xx < h; xx++) 
		{
			for (int yy = 0; yy < w; yy++) 
			{
				int pixel = image.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				//Now check the color values 0-255
				//Example: if color is black
				if(red == 0 && green == 0 && blue == 0)
				{
					//Do something
				}
			}
		}
	}
	
	//Method to flip the image horizontally (face the other direction)
	public static BufferedImage flipImageHorizontally(BufferedImage original) 
	{
		BufferedImage newImage = original;
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-original.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		newImage = op.filter(newImage, null);

		return newImage;
	}
	
	//Method to rotate the image at a certain axis
	public static void rotateImage(float x, float y, BufferedImage image, int degrees, Graphics2D g2d)
	{	
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(degrees));
//		System.out.println(degrees);
		g2d.drawImage(image,at,null);
	}
	
	//Method to determine if the mouse is over a current spot on the screen
	public static boolean mouseOver(int mx,int my,int x , int y, int width, int height)
	{
		if (mx > x && mx < x + width){
			if(my>y&& my<y+height ){
				return true;
			}else return false;
		}else return false;
	}
	
	//loads a specific cutscene for a specific level
	public static void loadCutScene(ArrayList<ArrayList<String>> text, ArrayList<ArrayList<Integer[]>> location)
	{
		//initialize the file object
		File file = new File(new String("res/cutscenes/"+level+".txt"));
		try
		{
			//initialize the buffered reader
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			try
			{
				//first values are the amount of text, and the 
				int amount = Integer.parseInt(br.readLine());
				//max amount of screens
				int screens = Integer.parseInt(br.readLine());
				world.setMaxScenes(screens);
				//add the text and location to the arrays
				for(int i = 0;i<screens;i++)
				{
					text.add(new ArrayList<String>());
					location.add(new ArrayList<Integer[]>());
				}
				HashSet<Integer> seen = new HashSet();
				for(int i = 0;i<amount;i++)
				{
					String a = br.readLine();
					String b[] = a.split(":");
					
					String sentence = b[0];
					int x = Integer.parseInt(b[1]);
					int y = Integer.parseInt(b[2]);
					int index = Integer.parseInt(b[3]);
					
					
					text.get(index-1).add(sentence);
					location.get(index-1).add(new Integer[] {x,y});
					
				}
				
			}catch(IOException io)
			{
				io.printStackTrace();
			}
			
			
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void loadImageLevelFromFile()
	{
		File file = new File(new String("res/levels/level"+level+".txt"));
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String a;
			int index = 0;
			
			try {
				
				mapSizeX = Integer.parseInt(br.readLine());
				mapSizeY = Integer.parseInt(br.readLine());
				map = new int[mapSizeY][mapSizeX];
				
				for(int yy = 0;yy<mapSizeY;yy++)
				{
					a = br.readLine();
					//System.out.println(a);
					int ind = 0;
					for(int xx = 0;xx<mapSizeX;xx++)
					{
						int num = 0;
						String piece = a.substring(ind,ind+2);
						if(piece.endsWith(" ")){num = Integer.parseInt(piece.substring(0,1));ind+=2;}
						if(!piece.endsWith(" ")){num = Integer.parseInt(piece.substring(0,2));ind+=3;}
						
						map[yy][xx]=num;
						
//						if(num == 0)
//						{
//							handler.addBlocks(new Block(xx*64,yy*64,BlockID.Empty));
//						}
						if(num == 1)
						{
							startingPosition[0] = xx*64;
							startingPosition[1] = yy*64;
						}
						if(num == 2)
						{
							handler.addBlocks(new Block(xx*64,yy*64,BlockID.Dirt));
						}
						if(num == 3)
						{
							handler.addBlocks(new Block(xx*64,yy*64,BlockID.SnowDirt));
						}
						if(num == 4)
						{
							handler.addBlocks(new Block(xx*64,yy*64,BlockID.Ice));
						}
						if(num == 5)
						{
							handler.addBlocks(new Block(xx*64,yy*64,BlockID.Invisible));
						}
						if(num == 7)
						{
							handler.addBlocks(new Block(xx*64,yy*64,BlockID.Water));
						}
						if(num == 8)
						{
							handler.addEnemy(new Wolf(xx*64,yy*64,EnemyID.Wolf,handler));
						}
						if(num == 9)
						{
							handler.addEnemy(new Enemy(xx*64,yy*64,EnemyID.Basic,handler));
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//process the sound byte array from the /v/ message
	public void receiveSound(byte[] targetData, int numBytesRead)
	{
		Thread soundIn = new Thread() {
			public void run()
			{
				try {
					//Open the source file, and then write(play) it
					SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
					sourceLine.open(format);
					sourceLine.start();
					sourceLine.write(targetData, 0, numBytesRead);
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		//execute the thread
		soundIn.start();
	}
	//END OF OTHER STATIC METHODS
	
	//GETTERS AND SETTERS
	
	//Return the game's instance of the player
	public Player getPlayer() {return this.p;}
	
	public Game getWorld() {return this.world;}
	
	//Return the game's instance of textures
	public static Texture getTextureInstance(){return tex;}
	
	public static int getRandomNumber(int range) {return rand.nextInt(range);}

	//part of the MouseListener implementation
	//Is called whenever the mouse is moved
	public void mouseMoved(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		
		if(mouseOver(mx,my,WIDTH-30,10,20,20))
		{
			buttonColors[0] = TOMATO;
		}else
		{
			buttonColors[0] = Color.red;
		}
		
		if(mouseOver(mx,my,WIDTH-60,10,20,20))
		{
			buttonColors[1] = LIGHT_YELLOW;
		}else
		{
			buttonColors[1] = Color.yellow;
		}
		
		if(mouseOver(mx,my,WIDTH-90,10,20,20))
		{
			buttonColors[2] = SPRING_GREEN;
		}else {
			buttonColors[2] = Color.green;
		}

	}

	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		int mx = arg0.getX();
		int my = arg0.getY();
		
		if(mouseOver(mx,my,0,0,WIDTH,40) && !fullscreen)
		{
			w.frameA.setLocation(arg0.getXOnScreen()-WIDTH/2, arg0.getYOnScreen()-20);
			dragging = true;
		}
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

	@Override
	public void mousePressed(MouseEvent e) {
		
		int mx = e.getX();
		int my = e.getY();
		
		if(mouseOver(mx,my,WIDTH-30,10,20,20))
		{
			if(!MainGameLoop.gameState.equals(STATE.Login))
			{
				MainGameLoop.client.close();
			}
	    	AudioMaster.cleanUp();
	    	MainGameLoop.BGMusicSource.delete();
	    	System.exit(0);
		}
		if(mouseOver(mx,my,WIDTH-60,10,20,20))
		{
			w.setMinimized();
		}
		if(mouseOver(mx,my,WIDTH-90,10,30,20))
		{
			fullscreen = !fullscreen;
			if(fullscreen)
			{
				
				w.frameA.setVisible(false);
				WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
				HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
				w.frameA.setExtendedState(JFrame.MAXIMIZED_BOTH);
				
				if(gameState.equals(STATE.Menu))
				{
					p.setY((int)(HEIGHT*(6.1/8)));
				}
				
				w.frameA.setVisible(true);
			}else
			{
				WIDTH = 800;
				HEIGHT = 800;
				if(gameState.equals(STATE.Menu))
				{
					p.setY((int)(HEIGHT*(6.1/8)));
				}
				w.frameA.setSize(WIDTH,HEIGHT);
				w.frameA.setLocationRelativeTo(null);
			}
			
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		dragging = false;
		
	}	

	//END OF GETTERS AND SETTERS
	

}
