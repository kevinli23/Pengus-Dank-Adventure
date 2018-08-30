package com.kevinli.main;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.kevinli.splashscreen.SplashScreenMain;

import audio.AudioMaster;

/*
 * About This Class
 * ----------------
 * Main Purpose:
 * Creates the window and sets the window settings 
 */

public class Window 
{
	public JFrame frameA,frameB;
	
	private void createFrame(String title, int width, int height, MainGameLoop game, JFrame frame, boolean fullscreen)
	{		
		if(!fullscreen)
		{
			frame.setPreferredSize(new Dimension(width, height));
			frame.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
			frame.setMinimumSize(new Dimension(width, height));
			frame.setUndecorated(true);
		}else
		{
			frame.setUndecorated(true);
		}

		//Set the default close operation to do nothing, is edited above
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//Set it so that the user can't resize the frame
		frame.setResizable(false);
		//Set the frame to appear in the middle of the screen
		frame.setLocationRelativeTo(null);
		//Set the frame to be focusable
		frame.setFocusable(true);
		frame.requestFocus();
		
	}
	
	public Window(String title,int width, int height,MainGameLoop game){
		new SplashScreenMain();
		frameA = new JFrame(title);
		
		createFrame(title, height, height, game, frameA, false);
		
		//Set the frame and button looks and feeling to the current System/OS normals
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//set the image icon of the frame
		try {
			frameA.setIconImage(ImageIO.read(new File("res/sprites/static_images/imageicon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//add the game to the Jframe
		frameA.add(game);
		//let it be visible
		frameA.setVisible(true);
		//execute the main game thread
		game.start();
		
	}
	
	//set the frame to be minimized (back to task bar)
	public void setMinimized()
	{
		frameA.setState(Frame.ICONIFIED);
	}
}
