package audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;


public class AudioMaster 
{
	//list of buffers for the current sound
	private static List<Integer> buffers = new ArrayList<Integer>();
	
	//initialize the AL class
	public static void init()
	{
		boolean success = false;
		while(!success)
		{
			try 
			{
				AL.create();
				success = true;
			} catch (LWJGLException e) 
			{
				System.err.println("Error when initializing music");
				System.out.println("Trying again");
			}
		}
	}
	
	//set the position and speed of the music/sounds
	public static void setListenerData()
	{
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	//clear the buffers and destroy the object
	public static void cleanUp()
	{
		for(int i = 0;i<buffers.size();i++)
		{
			AL10.alDeleteBuffers(buffers.get(i));
		}
		AL.destroy();
	}
	
	//load sounds/music from the wave file
	public static int loadSound(String file)
	{
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		
		WaveData waveFile = null;
		try {
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}
}
