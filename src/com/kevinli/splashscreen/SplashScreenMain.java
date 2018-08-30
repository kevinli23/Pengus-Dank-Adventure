package com.kevinli.splashscreen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.kevinli.main.MainGameLoop;

/*
 * Main Purpose:
 * -------------
 * Handles the creation and destruction of the actual splash screen
 */

public class SplashScreenMain 
{
	//VARIABLE DECLARATION
	private Thread t;
	private SplashScreen screen;
	private boolean running = true;
	//END OF VARIABLE DECLARATION
	
	public SplashScreenMain()
	{
		//Create the splash screen and set its settings
		screen = new SplashScreen();
		screen.setLocationRelativeTo(null);
		screen.setMaxProgress(100);
		screen.setVisible(true);

		t = new Thread(new Runnable(){
			
			@Override
			public void run() 
			{
				while(running)
				{
					screen.setProgress(MainGameLoop.loadingProgress);
				}
			}});
		t.start();
		
		MainGameLoop.loadOnMain();
		running = false;
		
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		screen.setVisible(false);
		screen.dispose();
	}
}