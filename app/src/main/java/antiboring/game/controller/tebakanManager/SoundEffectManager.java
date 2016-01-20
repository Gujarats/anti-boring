package antiboring.game.controller.tebakanManager;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
 * Created by Gujarat Santana on 27/12/15.
 */
public class SoundEffectManager {

    public static SoundEffectManager instance;

    private SoundEffectManager() {}

    public static SoundEffectManager getInstance() {
        if (instance == null) instance = new SoundEffectManager();
        return instance;
    }

    public void playCorrectAnswer(Activity activity){
        MediaPlayer m = new MediaPlayer();
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = activity.getAssets().openFd("get_coin.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
