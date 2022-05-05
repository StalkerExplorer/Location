package com.software1002developer.gps;

//класс с ToneGenerator
import android.media.AudioManager;
import android.media.ToneGenerator;

public class Tone
{
	ToneGenerator toneGenerator;
	int toneType;
	int durationMs;

	public Tone(int tone, int duration){
		toneType=tone;
		durationMs=duration;
		int streamType = AudioManager.STREAM_MUSIC;
		int volume = ToneGenerator.MAX_VOLUME;
		toneGenerator = new ToneGenerator(streamType, volume);
	}

	public void tone(){//сам тональник, который пищит
		toneGenerator.startTone(toneType, durationMs);
	}

	public void setToneType(int tone){//установим звук
		toneType=tone;
	}

	public void setDuration(int duration){//установим длительность
		durationMs=duration;
	}
}
	
	
