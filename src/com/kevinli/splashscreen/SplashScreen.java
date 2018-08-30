package com.kevinli.splashscreen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import com.kevinli.main.MainGameLoop;

/*
 * Main Purpose:
 * -------------
 * The GUI for the loading/splash screen right before playing the game
 */

public class SplashScreen extends JWindow
{
	//VARIABLE DECLARATION
	
	private static final long serialVersionUID = -6552564115167570638L;
	//Image that holds the splash screen
	private ImageIcon icon;
	//Label that holds text
	private JLabel imgLabel;
	//Panel that holds the progress bar
	private JPanel southPanel;
	//South area layout, puts south panel to the south of the image
	private FlowLayout southFlow;
	//Progress Bar to show the loading
	private JProgressBar progressBar;
	//Layout to add image and flowlayout
	private BorderLayout borderLayout;
	//String path for the image
	private String imagePath = "res/sprites/static_images/load_image.png";
	
	private Thread t;
	
	//END OF VARIABLE DECLARATION
	
	//CONSTRUCTOR
	public SplashScreen()
	{
		//Loading the image icon
		try 
		{
			icon = new ImageIcon(ImageIO.read(new File(imagePath)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//Create the new objects
		borderLayout = new BorderLayout();
		imgLabel = new JLabel();
		southPanel = new JPanel();
		southFlow = new FlowLayout();
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		
		//Set the settings and layout
		try{
			init();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void init()
	{
		//Add the image / splash image
		imgLabel.setIcon(icon);
		//Set the type of layout
		getContentPane().setLayout(borderLayout);
		//Create an empty panel south of the image
		southPanel.setLayout(southFlow);
		//Set the panel's background to black
		southPanel.setBackground(Color.black);
		//Add the two into our pane
		getContentPane().add(imgLabel,BorderLayout.CENTER);
		getContentPane().add(southPanel,BorderLayout.SOUTH);
		//Set progress bar colors and settings and add it
		progressBar.setForeground(Color.GREEN);
		progressBar.setBackground(Color.black);
		progressBar.setBorderPainted(false);
		southPanel.add(progressBar, null);
		//Eh why not
		pack();
		
		
	}
	
	//SETTERS
	
	//Sets the maximum value for the progress bar
	public void setMaxProgress(int maxProgress){progressBar.setMaximum(maxProgress);}
	
	//Set the current value of the progress bar
	public void setProgress(int progress)
	{
		double percentage = ((double)progress/progressBar.getMaximum())*100;
		DecimalFormat percent = new DecimalFormat("##.##");
		percent.format(percentage);
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() 
			{
				progressBar.setValue((int)percentage);
				progressBar.setString(MainGameLoop.loadingString);
				//progressBar.setString("Loading: "+percentage+"%");
			}
			
		});
	}
	//END OF SETTERS
	
}
