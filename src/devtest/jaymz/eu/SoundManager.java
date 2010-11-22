package devtest.jaymz.eu;

import android.media.SoundPool;
import android.media.AudioManager;
import android.content.Context;
import java.util.HashMap;

public class SoundManager {

    private  SoundPool soundPool;
    private  HashMap poolMap;
    private  AudioManager  audioManager;
    private  Context context;

    public void initSounds(Context appContext) {
        context = appContext;
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        poolMap = new HashMap();
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void addSound(int index, int SoundID) {
        poolMap.put(index, soundPool.load(context, SoundID, 1));
    }

    public void playSound(int index) {
        float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        soundPool.play(index, streamVolume, streamVolume, 1, 0, 1f);
    }

    public void playLoopedSound(int index) {
        float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        soundPool.play(index, streamVolume, streamVolume, 1, -1, 1f);
    }

}