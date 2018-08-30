package audio;

import org.lwjgl.openal.AL10;

public class Source 
{
	private int sourceId;
	private int buffer;
	private float volume = 1.0f;
	private float pitch = 1.0f;
	private float x,y,z;
	
	public Source(int buffer,float volume, float pitch, float x, float y, float z)
	{
		this.volume = volume;
		this.pitch = pitch;
		this.x = x;
		this.y = y;
		this.z = z;
		
		sourceId = AL10.alGenSources();
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x,y,z);
		this.buffer = buffer;
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
	}
	
	public void play()
	{
		AL10.alSourcePlay(sourceId);
	}
	
	public void pause()
	{
		AL10.alSourcePause(sourceId);
	}
	
	public void adjustVolume(float amount)
	{
		if(volume+amount>=0 && volume+amount<=1) this.volume += amount;
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}
	
	public void delete()
	{
		AL10.alDeleteSources(sourceId);
	}
	
	public void setPitch(float pitch)
	{
		this.pitch = pitch;
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}
	
	public float getVolume()
	{
		return this.volume;
	}
}
